/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.caffeine.configuration;

import cn.herodotus.engine.cache.caffeine.enhance.HerodotusCaffeineCacheManager;
import cn.herodotus.engine.cache.core.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * <p>Description: Caffeine配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/12 17:20
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({CacheProperties.class})
public class CaffeineConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CaffeineConfiguration.class);

    @Resource
    private CacheProperties cacheProperties;

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Cache Caffeine] Auto Configure.");
    }

    @Bean
    public Caffeine<Object, Object> caffeine() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getDuration(), cacheProperties.getUnit());

        log.trace("[Herodotus] |- Bean [Caffeine] Auto Configure.");

        return caffeine;
    }

    @Bean
    @ConditionalOnMissingBean(CaffeineCacheManager.class)
    public CaffeineCacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
        HerodotusCaffeineCacheManager herodotusCaffeineCacheManager = new HerodotusCaffeineCacheManager(cacheProperties);
        herodotusCaffeineCacheManager.setCaffeine(caffeine);
        log.trace("[Herodotus] |- Bean [Caffeine Cache Manager] Auto Configure.");
        return herodotusCaffeineCacheManager;
    }
}
