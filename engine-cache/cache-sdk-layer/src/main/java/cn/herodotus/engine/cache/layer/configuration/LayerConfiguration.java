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
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.cache.layer.configuration;

import cn.herodotus.engine.cache.caffeine.configuration.CaffeineConfiguration;
import cn.herodotus.engine.cache.core.properties.CacheProperties;
import cn.herodotus.engine.cache.layer.enhance.HerodotusCacheManager;
import cn.herodotus.engine.cache.redis.configuration.RedisConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 自定义多级缓存配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/13 22:49
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CacheProperties.class)
@Import({CaffeineConfiguration.class, RedisConfiguration.class})
public class LayerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LayerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Cache Layer] Auto Configure.");
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public HerodotusCacheManager herodotusCacheManager(CaffeineCacheManager caffeineCacheManager, RedisCacheManager redisCacheManager, CacheProperties cacheProperties) {
        HerodotusCacheManager herodotusCacheManager = new HerodotusCacheManager();
        herodotusCacheManager.setCaffeineCacheManager(caffeineCacheManager);
        herodotusCacheManager.setRedisCacheManager(redisCacheManager);
        herodotusCacheManager.setDesensitization(cacheProperties.getDesensitization());
        herodotusCacheManager.setClearRemoteOnExit(cacheProperties.getClearRemoteOnExit());
        herodotusCacheManager.setAllowNullValues(cacheProperties.getAllowNullValues());
        log.trace("[Herodotus] |- Bean [Herodotus Cache Manager] Auto Configure.");
        return herodotusCacheManager;
    }
}
