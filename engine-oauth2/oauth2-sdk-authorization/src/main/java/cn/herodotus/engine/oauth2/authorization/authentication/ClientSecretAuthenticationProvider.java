/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oauth2.authorization.authentication;

import cn.herodotus.engine.oauth2.core.definition.domain.HerodotusGrantedAuthority;
import cn.herodotus.engine.oauth2.core.definition.service.ClientDetailsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;

/**
 * <p>Description: 扩展的 ClientSecretAuthenticationProvider </p>
 * <p>
 * 增加 Client 权限数据
 *
 * @author : gengwei.zheng
 * @date : 2022/4/2 10:37
 */
public class ClientSecretAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(ClientSecretAuthenticationProvider.class);

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";
    private final RegisteredClientRepository registeredClientRepository;
    private final CodeVerifierAuthenticator codeVerifierAuthenticator;
    private final ClientDetailsService clientDetailsService;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a {@code ClientSecretAuthenticationProvider} using the provided parameters.
     *
     * @param registeredClientRepository the repository of registered clients
     * @param authorizationService       the authorization service
     */
    public ClientSecretAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                              OAuth2AuthorizationService authorizationService, ClientDetailsService clientDetailsService) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(clientDetailsService, "clientDetailsService cannot be null");
        this.registeredClientRepository = registeredClientRepository;
        this.codeVerifierAuthenticator = new CodeVerifierAuthenticator(authorizationService);
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * Sets the {@link PasswordEncoder} used to validate
     * the {@link RegisteredClient#getClientSecret() client secret}.
     * If not set, the client secret will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}.
     *
     * @param passwordEncoder the {@link PasswordEncoder} used to validate the client secret
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2ClientAuthenticationToken clientAuthentication =
                (OAuth2ClientAuthenticationToken) authentication;

        if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientAuthentication.getClientAuthenticationMethod()) &&
                !ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientAuthentication.getClientAuthenticationMethod())) {
            return null;
        }

        String clientId = clientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
        }

        if (!registeredClient.getClientAuthenticationMethods().contains(
                clientAuthentication.getClientAuthenticationMethod())) {
            throwInvalidClient("authentication_method");
        }

        if (clientAuthentication.getCredentials() == null) {
            throwInvalidClient("credentials");
        }

        String clientSecret = clientAuthentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_SECRET);
        }

        // Validate the "code_verifier" parameter for the confidential client, if available
        this.codeVerifierAuthenticator.authenticateIfAvailable(clientAuthentication, registeredClient);

        Map<String, Object> additionalParameters = clientAuthentication.getAdditionalParameters();
        if (MapUtils.isNotEmpty(additionalParameters) && additionalParameters.containsKey(OAuth2ParameterNames.GRANT_TYPE)) {
            String grantType = (String) additionalParameters.get(OAuth2ParameterNames.GRANT_TYPE);
            if (StringUtils.isNotBlank(grantType) && AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantType)) {
                Set<HerodotusGrantedAuthority> authorities = clientDetailsService.findAuthoritiesById(registeredClient.getClientId());
                if (CollectionUtils.isNotEmpty(authorities)) {
                    log.debug("[Herodotus] |- Custom client secret authentication provider assign authorities.");
                    return new OAuth2ClientAuthenticationToken(registeredClient,
                            clientAuthentication.getClientAuthenticationMethod(), clientAuthentication.getCredentials(), authorities);
                }
            }
        }

        log.debug("[Herodotus] |- There are no authorities assigned to OAuth2ClientAuthenticationToken.");
        return new OAuth2ClientAuthenticationToken(registeredClient,
                clientAuthentication.getClientAuthenticationMethod(), clientAuthentication.getCredentials());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName,
                ERROR_URI
        );
        throw new OAuth2AuthenticationException(error);
    }
}
