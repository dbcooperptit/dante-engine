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

package cn.herodotus.engine.cache.redis.configuration;

import cn.herodotus.engine.cache.redis.annotation.ConditionalOnRedisSessionSharing;
import cn.herodotus.engine.cache.redis.session.HerodotusCookieWebSessionIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.FlushMode;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.session.WebSessionIdResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Description: 基于 Redis 的 Session 共享配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/5/23 22:21
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnRedisSessionSharing
public class RedisSessionConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Cache Redis Session] Auto Configure.");
    }

    /**
     * 指定 flushMode 为 IMMEDIATE 表示立即将 session 写入 redis
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HttpServletRequest.class)
    @EnableRedisHttpSession(flushMode = FlushMode.IMMEDIATE)
    public static class RedisHttpSessionConfiguration {
        @PostConstruct
        public void postConstruct() {
            log.debug("[Herodotus] |- SDK [Engine Cache Redis Http Session] Auto Configure.");
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ServerResponse.class)
    @EnableRedisWebSession(saveMode = SaveMode.ALWAYS)
    public static class RedisWebSessionConfiguration {
        @PostConstruct
        public void postConstruct() {
            log.debug("[Herodotus] |- SDK [Engine Cache Redis Web Session] Auto Configure.");
        }

        @Bean
        public WebSessionIdResolver webSessionIdResolver() {
            return new HerodotusCookieWebSessionIdResolver();
        }
    }
}
