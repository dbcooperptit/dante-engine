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

package cn.herodotus.engine.security.authorize.service;

import cn.herodotus.engine.rest.crypto.domain.SecretKey;
import cn.herodotus.engine.rest.crypto.enhance.InterfaceCryptoProcessor;
import cn.herodotus.engine.rest.crypto.exception.SessionInvalidException;
import cn.herodotus.engine.security.core.definition.service.HerodotusClientDetailsService;
import cn.herodotus.engine.security.core.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 请求加密服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:08
 */
@Service
public class InterfaceSecurityService {

    private static final Logger log = LoggerFactory.getLogger(InterfaceSecurityService.class);

    private static final String PKCS1_BEGIN = "-----BEGIN RSA PUBLIC KEY-----";
    private static final String PKCS1_END = "-----END RSA PUBLIC KEY-----";
    private static final String PKCS8_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String PKCS8_END = "-----END PUBLIC KEY-----";


    @Autowired
    private InterfaceCryptoProcessor interfaceCryptoProcessor;
    @Autowired
    private HerodotusClientDetailsService herodotusClientDetailsService;

    /**
     * 检查终端是否是合法终端
     *
     * @param clientId     OAuth2 终端ID
     * @param clientSecret OAuth2 终端密码
     */
    private ClientDetails validateClient(String clientId, String clientSecret) {
        ClientDetails clientDetails = herodotusClientDetailsService.getOauthClientDetails(clientId);

        boolean isMatch = false;
        if (ObjectUtils.isNotEmpty(clientDetails)) {
            isMatch = SecurityUtils.matches(clientSecret, clientDetails.getClientSecret());
        }

        if (!isMatch) {
            throw new InvalidClientException("Illegal Client Info");
        }

        return clientDetails;
    }

    public SecretKey createSecretKey(String clientId, String clientSecret, String sessionId) {
        // 检测终端是否是有效终端
        ClientDetails clientDetails = this.validateClient(clientId, clientSecret);
        return interfaceCryptoProcessor.createSecretKey(sessionId, clientDetails.getAccessTokenValiditySeconds());
    }

    /**
     * 前端用后端PublicKey加密前端PublicKey后，将该值传递给后端，用于加密 AES KEY
     *
     * @param sessionId          Session 标识
     * @param confidentialBase64 前端用后端PublicKey加密前端PublicKey。前端使用node-rsa加密后的数据是base64编码
     * @return 前端RSA PublicKey 加密后的 AES Key
     */
    public String exchange(String sessionId, String confidentialBase64) {
        try {
            return interfaceCryptoProcessor.exchange(sessionId, confidentialBase64);
        } catch (SessionInvalidException e) {
            throw new InvalidTokenException("Token is expired!");
        }
    }

    public String appendPkcs8PublicKeyPadding(String key) {
        return interfaceCryptoProcessor.convertPublicKeyToPkcs8Padding(key);
    }
}
