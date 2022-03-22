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

package cn.herodotus.engine.oauth2.server.authorization.service;

import cn.herodotus.engine.assistant.core.exception.transaction.TransactionRollbackException;
import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.data.jpa.repository.HerodotusRegisteredClientRepository;
import cn.herodotus.engine.oauth2.data.jpa.utils.OAuth2AuthorizationUtils;
import cn.herodotus.engine.oauth2.server.authorization.dto.OAuth2ApplicationDto;
import cn.herodotus.engine.oauth2.server.authorization.entity.OAuth2Application;
import cn.herodotus.engine.oauth2.server.authorization.entity.OAuth2Scope;
import cn.herodotus.engine.oauth2.server.authorization.repository.OAuth2ApplicationRepository;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: OAuth2ApplicationService </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:06
 */
@Service
public class OAuth2ApplicationService extends BaseLayeredService<OAuth2Application, String> {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ApplicationService.class);

    private final RegisteredClientRepository registeredClientRepository;
    private final HerodotusRegisteredClientRepository herodotusRegisteredClientRepository;
    private final OAuth2ApplicationRepository applicationRepository;

    @Autowired
    public OAuth2ApplicationService(RegisteredClientRepository registeredClientRepository, HerodotusRegisteredClientRepository herodotusRegisteredClientRepository, OAuth2ApplicationRepository applicationRepository) {
        this.registeredClientRepository = registeredClientRepository;
        this.herodotusRegisteredClientRepository = herodotusRegisteredClientRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public BaseRepository<OAuth2Application, String> getRepository() {
        return this.applicationRepository;
    }

    @Transactional(rollbackFor = TransactionRollbackException.class)
    @Override
    public OAuth2Application saveOrUpdate(OAuth2Application entity) {
        OAuth2Application application = super.saveOrUpdate(entity);
        if (ObjectUtils.isNotEmpty(application)) {
            registeredClientRepository.save(toRegisteredClient(application));
            log.debug("[Herodotus] |- OAuth2ApplicationService saveOrUpdate.");
            return application;
        } else {
            log.error("[Herodotus] |- OAuth2ApplicationService saveOrUpdate error, rollback data!");
            throw new NullPointerException("save or update OAuth2Application failed");
        }
    }

    @Transactional(rollbackFor = TransactionRollbackException.class)
    @Override
    public void deleteById(String id) {
        super.deleteById(id);
        herodotusRegisteredClientRepository.deleteById(id);
        log.debug("[Herodotus] |- OAuth2ApplicationService deleteById.");
    }

    @Transactional(rollbackFor = TransactionRollbackException.class)
    public OAuth2Application assign(String applicationId, String[] scopeIds) {

        Set<OAuth2Scope> scopes = new HashSet<>();
        for (String scopeId : scopeIds) {
            OAuth2Scope scope = new OAuth2Scope();
            scope.setScopeId(scopeId);
            scopes.add(scope);
        }

        OAuth2Application oldApplication = findById(applicationId);
        oldApplication.setScopes(scopes);

        OAuth2Application newApplication = saveOrUpdate(oldApplication);
        log.debug("[Herodotus] |- OAuth2ApplicationService assign.");
        return newApplication;
    }

    public static OAuth2ApplicationDto toDto(OAuth2Application entity) {
        OAuth2ApplicationDto dto = new OAuth2ApplicationDto();
        dto.setApplicationId(entity.getApplicationId());
        dto.setApplicationName(entity.getApplicationName());
        dto.setAbbreviation(entity.getAbbreviation());
        dto.setLogo(entity.getLogo());
        dto.setHomepage(entity.getHomepage());
        dto.setApplicationType(entity.getApplicationType());
        dto.setClientId(entity.getClientId());
        dto.setClientSecret(entity.getClientSecret());
        dto.setRedirectUris(entity.getRedirectUris());
        dto.setAuthorizationGrantTypes(StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes()));
        dto.setClientAuthenticationMethods(StringUtils.commaDelimitedListToSet(entity.getClientAuthenticationMethods()));
        dto.setRequireProofKey(entity.getRequireProofKey());
        dto.setRequireAuthorizationConsent(entity.getRequireAuthorizationConsent());
        dto.setJwkSetUrl(entity.getJwkSetUrl());
        dto.setAccessTokenValidity(entity.getAccessTokenValidity());
        dto.setReuseRefreshTokens(entity.getReuseRefreshTokens());
        dto.setRefreshTokenValidity(entity.getRefreshTokenValidity());
        dto.setSignature(entity.getSignature());
        dto.setScopes(entity.getScopes());
        dto.setReserved(entity.getReserved());
        dto.setDescription(entity.getDescription());
        dto.setReversion(entity.getReversion());
        dto.setRanking(entity.getRanking());
        dto.setStatus(entity.getStatus());
        dto.setClientSecretExpiresAt(entity.getClientSecretExpiresAt());
        return dto;
    }

    public static OAuth2Application toEntity(OAuth2ApplicationDto dto) {
        OAuth2Application entity = new OAuth2Application();
        entity.setApplicationId(dto.getApplicationId());
        entity.setApplicationName(dto.getApplicationName());
        entity.setAbbreviation(dto.getAbbreviation());
        entity.setLogo(dto.getLogo());
        entity.setHomepage(dto.getHomepage());
        entity.setApplicationType(dto.getApplicationType());
        entity.setClientId(dto.getClientId());
        entity.setClientSecret(dto.getClientSecret());
        entity.setRedirectUris(dto.getRedirectUris());
        entity.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(dto.getAuthorizationGrantTypes()));
        entity.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(dto.getClientAuthenticationMethods()));
        entity.setRequireProofKey(dto.getRequireProofKey());
        entity.setRequireAuthorizationConsent(dto.getRequireAuthorizationConsent());
        entity.setJwkSetUrl(dto.getJwkSetUrl());
        entity.setAccessTokenValidity(dto.getAccessTokenValidity());
        entity.setReuseRefreshTokens(dto.getReuseRefreshTokens());
        entity.setRefreshTokenValidity(dto.getRefreshTokenValidity());
        entity.setSignature(dto.getSignature());
        entity.setClientSecretExpiresAt(dto.getClientSecretExpiresAt());
        entity.setScopes(dto.getScopes());
        entity.setReserved(dto.getReserved());
        entity.setDescription(dto.getDescription());
        entity.setReversion(dto.getReversion());
        entity.setRanking(dto.getRanking());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    private RegisteredClient toRegisteredClient(OAuth2Application application) {

        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(application.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(application.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(application.getRedirectUris());
        Set<OAuth2Scope> clientScopes = application.getScopes();

        return RegisteredClient.withId(application.getApplicationId())
                // 客户端id 需要唯一
                .clientId(application.getClientId())
                // 客户端密码
                .clientSecret(application.getClientSecret())
                .clientSecretExpiresAt(DateUtil.toInstant(application.getClientSecretExpiresAt()))
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .scopes((scopes) -> clientScopes.forEach(clientScope -> scopes.add(clientScope.getScopeCode())))
                .clientSettings(
                        ClientSettings.builder()
                                // 是否需要用户确认一下客户端需要获取用户的哪些权限
                                // 比如：客户端需要获取用户的 用户信息、用户照片 但是此处用户可以控制只给客户端授权获取 用户信息。
                                .requireAuthorizationConsent(application.getRequireAuthorizationConsent())
                                .requireProofKey(application.getRequireProofKey())
                                .build()
                )
                .tokenSettings(
                        TokenSettings.builder()
                                // accessToken 的有效期
                                .accessTokenTimeToLive(application.getAccessTokenValidity())
                                // refreshToken 的有效期
                                .refreshTokenTimeToLive(application.getRefreshTokenValidity())
                                // 是否可重用刷新令牌
                                .reuseRefreshTokens(application.getReuseRefreshTokens())
                                .build()
                )
                .build();
    }
}
