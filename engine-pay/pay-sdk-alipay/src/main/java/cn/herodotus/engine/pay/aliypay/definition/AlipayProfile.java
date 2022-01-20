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

package cn.herodotus.engine.pay.aliypay.definition;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: 支付宝支付配置信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 18:08
 */
public class AlipayProfile implements Serializable {

    /**
     * APPID 即创建应用后生成
     *
     * 代调用接口的本质是第三方应用代其他应用调用接口。调用接口规则为“谁调用就传入谁的APPID”。因此，在代调用场景下公共参数 app_id 需要传入 第三方应用 APPID。
     */
    private String appId;
    /**
     * 开发者私钥，由开发者自己生成。
     */
    private String appPrivateKey;
    /**
     * 支付宝公钥，由支付宝生成。(普通公钥模式使用)
     */
    private String alipayPublicKey;
    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    private String signType = "RSA2";
    private String charset = StandardCharsets.UTF_8.toString();
    /**
     * 应用公钥证书文件本地路径。(公钥证书模式使用)
     */
    private String appCertPath;
    /**
     * 支付宝公钥证书文件本地路径。(公钥证书模式使用)
     */
    private String alipayCertPath;
    /**
     * 支付宝根证书文件本地路径。(公钥证书模式使用)
     */
    private String alipayRootCertPath;

    public AlipayProfile() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAppCertPath() {
        return appCertPath;
    }

    public void setAppCertPath(String appCertPath) {
        this.appCertPath = appCertPath;
    }

    public String getAlipayCertPath() {
        return alipayCertPath;
    }

    public void setAlipayCertPath(String alipayCertPath) {
        this.alipayCertPath = alipayCertPath;
    }

    public String getAlipayRootCertPath() {
        return alipayRootCertPath;
    }

    public void setAlipayRootCertPath(String alipayRootCertPath) {
        this.alipayRootCertPath = alipayRootCertPath;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("appId", appId)
                .add("appPrivateKey", appPrivateKey)
                .add("alipayPublicKey", alipayPublicKey)
                .add("signType", signType)
                .add("charset", charset)
                .add("appCertPath", appCertPath)
                .add("alipayCertPath", alipayCertPath)
                .add("alipayRootCertPath", alipayRootCertPath)
                .toString();
    }
}
