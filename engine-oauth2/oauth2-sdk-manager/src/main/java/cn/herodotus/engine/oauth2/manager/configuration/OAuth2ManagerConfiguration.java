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

package cn.herodotus.engine.oauth2.manager.configuration;

import cn.herodotus.engine.oauth2.manager.service.HerodotusAuthorizationConsentService;
import cn.herodotus.engine.oauth2.manager.service.HerodotusAuthorizationService;
import cn.herodotus.engine.oauth2.manager.service.HerodotusRegisteredClientService;
import cn.herodotus.engine.oauth2.manager.storage.JpaOAuth2AuthorizationConsentService;
import cn.herodotus.engine.oauth2.manager.storage.JpaOAuth2AuthorizationService;
import cn.herodotus.engine.oauth2.manager.storage.JpaRegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.UUID;

/**
 * <p>Description: OAuth2 Manager 模块配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/26 12:35
 */
@Configuration(proxyBeanMethods = false)
@EntityScan(basePackages = {
        "cn.herodotus.engine.oauth2.manager.entity"
})
@EnableJpaRepositories(basePackages = {
        "cn.herodotus.engine.oauth2.manager.repository",
})
@ComponentScan(basePackages = {
        "cn.herodotus.engine.oauth2.manager.service",
})
public class OAuth2ManagerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OAuth2ManagerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine OAuth2 Manager] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisteredClientRepository registeredClientRepository(HerodotusRegisteredClientService herodotusRegisteredClientService, PasswordEncoder passwordEncoder) {

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                // 客户端id 需要唯一
                .clientId("articles-client")
                // 客户端密码
                .clientSecret(passwordEncoder.encode("123456"))
                // 可以基于 basic 的方式和授权服务器进行认证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权码
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // 刷新token
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc")
                .redirectUri("http://192.168.101.10:8847/herodotus-cloud-upms/open/authorized")
                .scope(OidcScopes.OPENID)
                .scope("articles.read")
                .clientSettings(
                        ClientSettings.builder()
                                // 是否需要用户确认一下客户端需要获取用户的哪些权限
                                // 比如：客户端需要获取用户的 用户信息、用户照片 但是此处用户可以控制只给客户端授权获取 用户信息。
                                .requireAuthorizationConsent(true)
                                .build()
                )
                .tokenSettings(
                        TokenSettings.builder()
                                // accessToken 的有效期
                                .accessTokenTimeToLive(Duration.ofHours(1))
                                // refreshToken 的有效期
                                .refreshTokenTimeToLive(Duration.ofDays(3))
                                // 是否可重用刷新令牌
                                .reuseRefreshTokens(true)
                                .build()
                )
                .build();
        JpaRegisteredClientRepository jpaRegisteredClientRepository = new JpaRegisteredClientRepository(herodotusRegisteredClientService, passwordEncoder);
        if (null == jpaRegisteredClientRepository.findByClientId("articles-client")) {
            jpaRegisteredClientRepository.save(registeredClient);
        }

        log.debug("[Herodotus] |- Bean [Jpa Registered Client Repository] Auto Configure.");
        return jpaRegisteredClientRepository;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationService authorizationService(HerodotusAuthorizationService herodotusAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService = new JpaOAuth2AuthorizationService(herodotusAuthorizationService, registeredClientRepository);
        log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Service] Auto Configure.");
        return jpaOAuth2AuthorizationService;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationConsentService authorizationConsentService(HerodotusAuthorizationConsentService herodotusAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
        JpaOAuth2AuthorizationConsentService jpaOAuth2AuthorizationConsentService =  new JpaOAuth2AuthorizationConsentService(herodotusAuthorizationConsentService, registeredClientRepository);
        log.debug("[Herodotus] |- Bean [Jpa OAuth2 Authorization Consent Service] Auto Configure.");
        return jpaOAuth2AuthorizationConsentService;
    }
}
