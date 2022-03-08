/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020-2030 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.oauth2.manager.service;

import cn.herodotus.engine.assistant.core.exception.transaction.TransactionRollbackException;
import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseLayeredService;
import cn.herodotus.engine.oauth2.data.jpa.repository.HerodotusRegisteredClientRepository;
import cn.herodotus.engine.oauth2.data.jpa.utils.OAuth2AuthorizationUtils;
import cn.herodotus.engine.oauth2.manager.entity.OAuth2Application;
import cn.herodotus.engine.oauth2.manager.entity.OAuth2Scope;
import cn.herodotus.engine.oauth2.manager.repository.OAuth2ApplicationRepository;
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
