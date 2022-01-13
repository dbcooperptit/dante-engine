/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.jetcache.configuration;

import cn.herodotus.engine.cache.jetcache.enhance.JetCacheBuilder;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.support.ConfigProvider;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.autoconfigure.AutoConfigureBeans;
import com.alicp.jetcache.autoconfigure.JetCacheProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: JetCacheConfiguration </p>
 *
 * 新增JetCache配置，解决JetCache依赖循环问题
 *
 * @author : gengwei.zheng
 * @date : 2021/12/4 10:44
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JetCacheProperties.class)
@EnableCreateCacheAnnotation
public class JetCacheConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JetCacheConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Cache JetCache] Auto Configure.");
    }

    @Bean
    public AutoConfigureBeans autoConfigureBeans() {
        AutoConfigureBeans autoConfigureBeans = new AutoConfigureBeans();
        log.trace("[Herodotus] |- Bean [Auto Configure Beans] Auto Configure.");
        return autoConfigureBeans;
    }

    @Bean
    public GlobalCacheConfig globalCacheConfig(AutoConfigureBeans autoConfigureBeans, JetCacheProperties jetCacheProperties) {
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setHiddenPackages(jetCacheProperties.getHiddenPackages());
        globalCacheConfig.setStatIntervalMinutes(jetCacheProperties.getStatIntervalMinutes());
        globalCacheConfig.setAreaInCacheName(jetCacheProperties.isAreaInCacheName());
        globalCacheConfig.setPenetrationProtect(jetCacheProperties.isPenetrationProtect());
        globalCacheConfig.setEnableMethodCache(jetCacheProperties.isEnableMethodCache());
        globalCacheConfig.setLocalCacheBuilders(autoConfigureBeans.getLocalCacheBuilders());
        globalCacheConfig.setRemoteCacheBuilders(autoConfigureBeans.getRemoteCacheBuilders());
        log.trace("[Herodotus] |- Bean [Global Cache Config] Auto Configure.");
        return globalCacheConfig;
    }

    @Bean
    @ConditionalOnBean(GlobalCacheConfig.class)
    public ConfigProvider configProvider() {
        SpringConfigProvider springConfigProvider = new SpringConfigProvider();
        log.trace("[Herodotus] |- Bean [Spring Config Provider] Auto Configure.");
        return springConfigProvider;
    }

    @Bean
    @ConditionalOnBean(SpringConfigProvider.class)
    @ConditionalOnMissingBean
    public JetCacheBuilder jetCacheBuilder(SpringConfigProvider springConfigProvider) {
        JetCacheBuilder jetCacheBuilder = new JetCacheBuilder(springConfigProvider);
        log.trace("[Herodotus] |- Bean [Jet Cache Builder] Auto Configure.");
        return jetCacheBuilder;
    }
}
