/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oauth2.granter.customizer;

import cn.herodotus.engine.security.core.definition.domain.HerodotusUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Description: 自定义 TokenCustomizer </p>
 * <p>
 * 用于自定义的 Herodotus User Details 解析。如果使用 Security 默认的 {@link org.springframework.security.core.userdetails.User} 则不需要使用该类
 *
 * @author : gengwei.zheng
 * @date : 2022/2/23 22:17
 */
public class HerodotusTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {

        AbstractAuthenticationToken token = null;

        Authentication authenticataion = SecurityContextHolder.getContext().getAuthentication();

        if (authenticataion instanceof OAuth2ClientAuthenticationToken) {
            token = (OAuth2ClientAuthenticationToken) authenticataion;
        }

        if (token != null) {

            if (token.isAuthenticated() && OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {

                Authentication authentication = context.getPrincipal();

                if (authentication != null) {

                    if (authentication instanceof UsernamePasswordAuthenticationToken) {
                        HerodotusUser principal = (HerodotusUser) authentication.getPrincipal();
                        String userId = principal.getUserId();
                        Set<String> authorities = principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());

                        Map<String, Object> userAttributes = new HashMap<>();
                        userAttributes.put("userId", userId);

                        Set<String> contextAuthorizedScopes = context.getAuthorizedScopes();

                        JwtClaimsSet.Builder jwtClaimSetBuilder = context.getClaims();

                        if (CollectionUtils.isEmpty(contextAuthorizedScopes)) {
                            jwtClaimSetBuilder.claim(OAuth2ParameterNames.SCOPE, authorities);
                        }

                        jwtClaimSetBuilder.claims(claims ->
                                userAttributes.entrySet().stream()
                                        .forEach(entry -> claims.put(entry.getKey(), entry.getValue()))
                        );
                    }

                    if (authentication instanceof OAuth2ClientAuthenticationToken) {
                        OAuth2ClientAuthenticationToken OAuth2ClientAuthenticationToken = (OAuth2ClientAuthenticationToken) authentication;
                        Map<String, Object> additionalParameters = OAuth2ClientAuthenticationToken.getAdditionalParameters();

                        // customize the token according to your need for this kind of authentication
                        if (!CollectionUtils.isEmpty(additionalParameters)) {

                        }

                    }

                }
            }
        }

    }
}
