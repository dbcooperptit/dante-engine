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

package cn.herodotus.engine.pay.wechat.configuration;

import cn.herodotus.engine.pay.wechat.annotation.ConditionalOnWxpay;
import cn.herodotus.engine.pay.wechat.definition.WxpayPaymentTemplate;
import cn.herodotus.engine.pay.wechat.definition.WxpayProfileStorage;
import cn.herodotus.engine.pay.wechat.properties.WxpayProperties;
import cn.herodotus.engine.pay.wechat.support.WxpayDefaultProfileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 微信支付 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/11 12:34
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWxpay
@EnableConfigurationProperties(WxpayProperties.class)
public class WxpayConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WxpayConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Pay Wxpay] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public WxpayProfileStorage wxpayDefaultProfileStorage(WxpayProperties wxpayProperties) {
        WxpayDefaultProfileStorage wxpayDefaultProfileStorage = new WxpayDefaultProfileStorage(wxpayProperties);
        log.debug("[Herodotus] |- Bean [Wxpay Default Profile Storage] Auto Configure.");
        return wxpayDefaultProfileStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxpayPaymentTemplate wxpayPaymentTemplate(WxpayProfileStorage wxpayProfileStorage, WxpayProperties wxpayProperties) {
        WxpayPaymentTemplate wxpayPaymentTemplate = new WxpayPaymentTemplate(wxpayProfileStorage, wxpayProperties);
        log.trace("[Herodotus] |- Bean [Wxpay Payment Template] Auto Configure.");
        return wxpayPaymentTemplate;
    }
}
