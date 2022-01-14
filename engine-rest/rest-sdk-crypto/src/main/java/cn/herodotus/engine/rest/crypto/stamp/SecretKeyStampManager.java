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

package cn.herodotus.engine.rest.crypto.stamp;

import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.herodotus.engine.rest.core.constants.RestCacheConstants;
import cn.herodotus.engine.rest.crypto.domain.SecretKey;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 数据加密秘钥缓存 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:14
 */
public class SecretKeyStampManager extends AbstractStampManager<String, SecretKey> {

    private static final Logger log = LoggerFactory.getLogger(SecretKeyStampManager.class);

    @CreateCache(name = RestCacheConstants.CACHE_NAME_TOKEN_SECURE_KEY, cacheType = CacheType.BOTH)
    protected Cache<String, SecretKey> cache;

    @Override
    protected Cache<String, SecretKey> getCache() {
        return this.cache;
    }

    @Override
    public SecretKey nextStamp(String key) {
        // 生成 AES 秘钥
        String aesKey = RandomUtil.randomStringUpper(16);

        // 生成 RSA 公钥和私钥
        RSA rsa = SecureUtil.rsa();

        SecretKey secretKey = new SecretKey();
        secretKey.setSessionId(key);
        secretKey.setAesKey(aesKey);
        secretKey.setPrivateKeyBase64(rsa.getPrivateKeyBase64());
        secretKey.setPublicKeyBase64(rsa.getPublicKeyBase64());

        log.debug("[Herodotus] |- Generate secret key, value is : [{}]", secretKey);
        return secretKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
