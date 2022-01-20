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

package cn.herodotus.engine.pay.aliypay.support;

import cn.herodotus.engine.pay.aliypay.definition.AlipayProfile;
import cn.herodotus.engine.pay.aliypay.definition.AlipayProfileStorage;
import cn.herodotus.engine.pay.aliypay.properties.AlipayProperties;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * <p>Description: 支付宝配置默认存储 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 18:40
 */
public class AlipayDefaultProfileStorage extends AlipayProfileStorage {

    private final AlipayProperties alipayProperties;

    public AlipayDefaultProfileStorage(AlipayProperties alipayProperties) {
        this.alipayProperties = alipayProperties;
    }

    @Override
    public AlipayProfile getProfile(String identity) {
        Map<String, AlipayProfile> profiles = alipayProperties.getProfiles();
        if (MapUtils.isNotEmpty(profiles)) {
            return profiles.get(identity);
        }

        return null;
    }
}
