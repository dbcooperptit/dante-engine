/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.protect.core.support;


import cn.herodotus.engine.assistant.core.support.PropertyFinder;
import cn.herodotus.engine.assistant.core.support.PropertyResolver;
import cn.herodotus.engine.protect.core.constants.ProtectConstants;
import org.springframework.core.env.Environment;

/**
 * <p>Description: 策略模块配置获取器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/1 19:23
 */
public class CryptoPropertyFinder extends PropertyFinder {

    public static String getCryptoStrategy(Environment environment, String defaultValue) {
        return PropertyResolver.getProperty(environment, ProtectConstants.ITEM_PROTECT_CRYPTO_STRATEGY, defaultValue);
    }

    public static String getCryptoStrategy(Environment environment) {
        return PropertyResolver.getProperty(environment, ProtectConstants.ITEM_PROTECT_CRYPTO_STRATEGY);
    }
}
