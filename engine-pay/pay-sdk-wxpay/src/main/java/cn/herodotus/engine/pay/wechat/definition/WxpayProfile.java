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

import com.google.common.base.MoreObjects;

/**
 * <p>Description: 微信支付必要的配置属性信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 14:26
 */
public class WxpayProfile {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;
    /**
     * 微信支付商户号
     */
    private String mchId;
    /**
     * 微信支付商户密钥
     */
    private String mchKey;
    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    private String subAppId;
    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    private String subMchId;
    /**
     * apiV3 秘钥值.
     */
    private String apiV3Key;
    /**
     * apiV3 证书序列号值
     */
    private String certSerialNo;
    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    private String keyPath;
    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateCertPath;
    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateKeyPath;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getApiV3Key() {
        return apiV3Key;
    }

    public void setApiV3Key(String apiV3Key) {
        this.apiV3Key = apiV3Key;
    }

    public String getCertSerialNo() {
        return certSerialNo;
    }

    public void setCertSerialNo(String certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getPrivateCertPath() {
        return privateCertPath;
    }

    public void setPrivateCertPath(String privateCertPath) {
        this.privateCertPath = privateCertPath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("mchId", mchId)
                .add("mchKey", mchKey)
                .add("subAppId", subAppId)
                .add("subMchId", subMchId)
                .add("apiV3Key", apiV3Key)
                .add("certSerialNo", certSerialNo)
                .add("keyPath", keyPath)
                .add("privateCertPath", privateCertPath)
                .add("privateKeyPath", privateKeyPath)
                .toString();
    }
}
