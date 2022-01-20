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

package cn.herodotus.engine.pay.wechat.definition;

import cn.herodotus.engine.pay.core.exception.PaymentProfileIdIncorrectException;
import cn.herodotus.engine.pay.core.exception.PaymentProfileNotFoundException;
import cn.herodotus.engine.pay.wechat.properties.WxpayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 微信支付模版 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/11 12:10
 */
public class WxpayPaymentTemplate {

    private static final Logger log = LoggerFactory.getLogger(WxpayPaymentTemplate.class);

    private final WxpayProfileStorage wxpayProfileStorage;
    private final WxpayProperties wxpayProperties;

    public WxpayPaymentTemplate(WxpayProfileStorage wxpayProfileStorage, WxpayProperties wxpayProperties) {
        this.wxpayProfileStorage = wxpayProfileStorage;
        this.wxpayProperties = wxpayProperties;
    }

    private WxpayProfileStorage getWxpayProfileStorage() {
        return wxpayProfileStorage;
    }

    private WxpayProperties getWxpayProperties() {
        return wxpayProperties;
    }

    private WxpayProfile getProfile(String identity) {
        WxpayProfile wxpayProfile = getWxpayProfileStorage().getProfile(identity);
        if (ObjectUtils.isNotEmpty(wxpayProfile)) {
            return wxpayProfile;
        } else {
            throw new PaymentProfileNotFoundException("Payment profile for " + identity + " not found.");
        }
    }

    private WxpayPaymentExecuter getProcessor(Boolean sandbox, WxpayProfile wxpayProfile) {

        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxpayProfile.getAppId());
        payConfig.setMchId(wxpayProfile.getMchId());
        payConfig.setMchKey(wxpayProfile.getMchKey());
        payConfig.setSubAppId(wxpayProfile.getSubAppId());
        payConfig.setSubMchId(wxpayProfile.getSubMchId());
        payConfig.setKeyPath(wxpayProfile.getKeyPath());

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(sandbox);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return new WxpayPaymentExecuter(wxPayService);
    }

    public WxpayPaymentExecuter getProcessor(String identity) {

        String id = StringUtils.isNotBlank(identity) ? identity : getWxpayProperties().getDefaultProfile();

        if (StringUtils.isBlank(id)) {
            throw new PaymentProfileIdIncorrectException("Payment profile incorrect, or try to set default profile id.");
        }

        WxpayProfile wxpayProfile = getProfile(identity);
        return getProcessor(getWxpayProperties().getSandbox(), wxpayProfile);
    }

}
