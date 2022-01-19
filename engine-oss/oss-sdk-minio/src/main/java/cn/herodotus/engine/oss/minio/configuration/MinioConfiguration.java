/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2021 Zhenggengwei<码匠君>, herodotus@aliyun.com
 *
 * This file is part of Herodotus Cloud.
 *
 * Herodotus Cloud is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Cloud is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with with Herodotus Cloud;
 * if no see <https://gitee.com/herodotus/herodotus-cloud>
 *
 * - Author: Zhenggengwei<码匠君>
 * - Contact: herodotus@aliyun.com
 * - License: GNU Lesser General Public License (LGPL)
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.oss.minio.configuration;

import cn.herodotus.engine.oss.minio.annotation.ConditionalOnMinioEnabled;
import cn.herodotus.engine.oss.minio.core.MinioClientPool;
import cn.herodotus.engine.oss.minio.core.MinioManager;
import cn.herodotus.engine.oss.minio.core.MinioTemplate;
import cn.herodotus.engine.oss.minio.properties.MinioProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: Minio配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 11:30
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMinioEnabled
@EnableConfigurationProperties({MinioProperties.class})
public class MinioConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

    @PostConstruct
    public void init() {
        log.debug("[Herodotus] |- SDK [Engine Oss Minio] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioClientPool minioClientPool(MinioProperties minioProperties) {
        MinioClientPool minioClientPool = new MinioClientPool(minioProperties);
        log.trace("[Herodotus] |- Bean [Minio Client Pool] Auto Configure.");
        return minioClientPool;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MinioClientPool.class)
    public MinioTemplate minioTemplate(MinioClientPool minioClientPool) {
        MinioTemplate minioTemplate = new MinioTemplate(minioClientPool);
        log.trace("[Herodotus] |- Bean [Minio Template] Auto Configure.");
        return minioTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(MinioTemplate.class)
    public MinioManager minioManager(MinioTemplate minioTemplate, MinioProperties minioProperties) {
        MinioManager minioManager = new MinioManager(minioTemplate, minioProperties);
        log.trace("[Herodotus] |- Bean [Minio Manager] Auto Configure.");
        return minioManager;
    }
}
