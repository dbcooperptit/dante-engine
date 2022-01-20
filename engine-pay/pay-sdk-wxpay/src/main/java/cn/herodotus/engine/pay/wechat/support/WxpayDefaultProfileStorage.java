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

package cn.herodotus.engine.pay.wechat.support;

import cn.herodotus.engine.pay.wechat.definition.WxpayProfile;
import cn.herodotus.engine.pay.wechat.definition.WxpayProfileStorage;
import cn.herodotus.engine.pay.wechat.properties.WxpayProperties;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>Description: 微信支付配置，配置文件文件存储 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 14:57
 */
@Component
public class WxpayDefaultProfileStorage extends WxpayProfileStorage {

    private final WxpayProperties wxpayProperties;

    public WxpayDefaultProfileStorage(WxpayProperties wxpayProperties) {
        this.wxpayProperties = wxpayProperties;
    }

    private WxpayProperties getWxpayProperties() {
        return wxpayProperties;
    }

    @Override
    public WxpayProfile getProfile(String identity) {
        Map<String, WxpayProfile> profiles = getWxpayProperties().getProfiles();
        if (MapUtils.isNotEmpty(profiles)) {
            return profiles.get(identity);
        }

        return null;
    }
}
