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

package com.alibaba.cloud.sentinel;

import cn.herodotus.engine.facility.sentinel.configuration.FacilitySentinelConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Optional;

/**
 * <p>Description: 重新定义Sentinel配置 </p>
 * <p>
 * 解决 Sentinel依赖循环问题
 *
 * @author : gengwei.zheng
 * @date : 2021/12/3 18:12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(FacilitySentinelConfiguration.SentinelWebConfiguration.class)
public class SentinelWebAutoConfiguration extends WebMvcConfigurationSupport {

    private static final Logger log = LoggerFactory
            .getLogger(SentinelWebAutoConfiguration.class);

    @Autowired
    private SentinelProperties properties;

    @Autowired
    private Optional<SentinelWebInterceptor> sentinelWebInterceptorOptional;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!sentinelWebInterceptorOptional.isPresent()) {
            return;
        }
        SentinelProperties.Filter filterConfig = properties.getFilter();
        registry.addInterceptor(sentinelWebInterceptorOptional.get())
                .order(filterConfig.getOrder())
                .addPathPatterns(filterConfig.getUrlPatterns());
        log.info(
                "[Sentinel Starter] register SentinelWebInterceptor with urlPatterns: {}.",
                filterConfig.getUrlPatterns());
    }
}
