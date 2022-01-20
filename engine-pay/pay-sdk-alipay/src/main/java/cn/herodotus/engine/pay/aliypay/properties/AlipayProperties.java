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

package cn.herodotus.engine.pay.aliypay.properties;

import cn.herodotus.engine.pay.aliypay.definition.AlipayProfile;
import cn.herodotus.engine.pay.core.constants.PayConstants;
import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>Description: 支付宝配置属性 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 18:10
 */
@ConfigurationProperties(prefix = PayConstants.PROPERTY_PAY_ALIPAY)
public class AlipayProperties {
    /**
     * 是否开启支付宝使用
     */
    private Boolean enabled;
    /**
     * 是否是沙箱模式，默认值：false。
     */
    private Boolean sandbox = false;
    /**
     * 是否是证书模式，默认值：false。
     * false，公钥模式；true，证书模式；
     */
    private Boolean certMode = false;
    /**
     * 默认的 Profile 自定义唯一标识 Key
     */
    private String defaultProfile;
    /**
     * 支付宝支付信息配置，支持多个。以自定义唯一标识作为 Key。
     */
    private Map<String, AlipayProfile> profiles;
    /**
     * 用户确认支付后，支付宝通过 get 请求 returnUrl（商户入参传入），返回同步返回参数。
     */
    private String returnUrl;
    /**
     * 交易成功后，支付宝通过 post 请求 notifyUrl（商户入参传入），返回异步通知参数。
     */
    private String notifyUrl;
    /**
     * 支付通知处理服务的服务名
     */
    private String destination;

    public Boolean getSandbox() {
        return sandbox;
    }

    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public Boolean getCertMode() {
        return certMode;
    }

    public void setCertMode(Boolean certMode) {
        this.certMode = certMode;
    }

    public String getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(String defaultProfile) {
        this.defaultProfile = defaultProfile;
    }

    public Map<String, AlipayProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, AlipayProfile> profiles) {
        this.profiles = profiles;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("enabled", enabled)
                .add("sandbox", sandbox)
                .add("certMode", certMode)
                .add("defaultProfile", defaultProfile)
                .add("returnUrl", returnUrl)
                .add("notifyUrl", notifyUrl)
                .add("destination", destination)
                .toString();
    }
}
