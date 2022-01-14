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

package cn.herodotus.engine.rest.secure.configuration;

import cn.herodotus.engine.rest.secure.interceptor.AccessLimitedInterceptor;
import cn.herodotus.engine.rest.secure.interceptor.IdempotentInterceptor;
import cn.herodotus.engine.rest.secure.interceptor.XssHttpServletFilter;
import cn.herodotus.engine.rest.secure.properties.StampProperties;
import cn.herodotus.engine.rest.secure.stamp.AccessLimitedStampManager;
import cn.herodotus.engine.rest.secure.stamp.IdempotentStampManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: 接口安全配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/4 17:28
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({StampProperties.class})
public class InterfaceSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(InterfaceSecurityConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public IdempotentStampManager idempotentStampManager(StampProperties stampProperties) {
        IdempotentStampManager idempotentStampManager = new IdempotentStampManager();
        idempotentStampManager.setStampProperties(stampProperties);
        log.trace("[Herodotus] |- Bean [Idempotent Stamp Manager] Auto Configure.");
        return idempotentStampManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessLimitedStampManager accessLimitedStampManager(StampProperties stampProperties) {
        AccessLimitedStampManager accessLimitedStampManager = new AccessLimitedStampManager();
        accessLimitedStampManager.setStampProperties(stampProperties);
        log.trace("[Herodotus] |- Bean [Access Limited Stamp Manager] Auto Configure.");
        return accessLimitedStampManager;
    }



    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(IdempotentStampManager.class)
    public IdempotentInterceptor idempotentInterceptor(IdempotentStampManager idempotentStampManager) {
        IdempotentInterceptor idempotentInterceptor = new IdempotentInterceptor();
        idempotentInterceptor.setIdempotentStampManager(idempotentStampManager);
        log.trace("[Herodotus] |- Bean [Idempotent Interceptor] Auto Configure.");
        return idempotentInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AccessLimitedStampManager.class)
    public AccessLimitedInterceptor accessLimitedInterceptor(AccessLimitedStampManager accessLimitedStampManager) {
        AccessLimitedInterceptor accessLimitedInterceptor = new AccessLimitedInterceptor();
        accessLimitedInterceptor.setAccessLimitedStampManager(accessLimitedStampManager);
        log.trace("[Herodotus] |- Bean [Access Limited Interceptor] Auto Configure.");
        return accessLimitedInterceptor;
    }



    @Bean
    @ConditionalOnMissingBean
    public XssHttpServletFilter xssHttpServletFilter() {
        XssHttpServletFilter xssHttpServletFilter = new XssHttpServletFilter();
        log.trace("[Herodotus] |- Bean [Xss Http Servlet Filter] Auto Configure.");
        return xssHttpServletFilter;
    }


}
