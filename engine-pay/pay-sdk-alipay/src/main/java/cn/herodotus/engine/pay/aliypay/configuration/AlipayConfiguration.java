/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 Zhenggengwei<码匠君>, herodotus@aliyun.com
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

package cn.herodotus.engine.pay.aliypay.configuration;

import cn.herodotus.engine.pay.aliypay.annotation.ConditionalOnAlipay;
import cn.herodotus.engine.pay.aliypay.definition.AlipayPaymentTemplate;
import cn.herodotus.engine.pay.aliypay.definition.AlipayProfileStorage;
import cn.herodotus.engine.pay.aliypay.properties.AlipayProperties;
import cn.herodotus.engine.pay.aliypay.support.AlipayDefaultProfileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 支付宝配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/8 21:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAlipay
@EnableConfigurationProperties(AlipayProperties.class)
@ComponentScan(basePackages = {
        "cn.herodotus.engine.pay.aliypay.controller"
})
public class AlipayConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AlipayConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Pay Alipay] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public AlipayProfileStorage alipayProfileDefaultStorage(AlipayProperties alipayProperties) {
        AlipayDefaultProfileStorage alipayDefaultProfileStorage = new AlipayDefaultProfileStorage(alipayProperties);
        log.debug("[Herodotus] |- Bean [Alipay Profile Default Storage] Auto Configure.");
        return alipayDefaultProfileStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public AlipayPaymentTemplate alipayPaymentTemplate(AlipayProfileStorage alipayProfileStorage, AlipayProperties alipayProperties) {
        AlipayPaymentTemplate alipayPaymentTemplate = new AlipayPaymentTemplate(alipayProfileStorage, alipayProperties);
        log.trace("[Herodotus] |- Bean [Alipay Payment Template] Auto Configure.");
        return alipayPaymentTemplate;
    }

}
