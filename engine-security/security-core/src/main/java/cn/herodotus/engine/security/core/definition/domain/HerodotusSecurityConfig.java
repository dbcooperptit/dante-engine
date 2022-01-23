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

package cn.herodotus.engine.security.core.definition.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 自定义SecurityConfig </p>
 * <p>
 * 自定义SecurityConfig，主要为了构建无参数构造函数，以解决序列化出错问题
 *
 * @author : gengwei.zheng
 * @date : 2021/9/11 15:57
 */
public class HerodotusSecurityConfig implements ConfigAttribute {

    private final String attrib;

    public HerodotusSecurityConfig(String config) {
        Assert.hasText(config, "You must provide a configuration attribute");
        this.attrib = config;
    }

    @Override
    public String getAttribute() {
        return this.attrib;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HerodotusSecurityConfig that = (HerodotusSecurityConfig) o;
        return Objects.equal(attrib, that.attrib);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attrib);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("attrib", attrib)
                .toString();
    }

    public static List<ConfigAttribute> createListFromCommaDelimitedString(String access) {
        return createList(StringUtils.commaDelimitedListToStringArray(access));
    }

    public static List<ConfigAttribute> createList(String... attributeNames) {
        Assert.notNull(attributeNames, "You must supply an array of attribute names");
        List<ConfigAttribute> attributes = new ArrayList<>(attributeNames.length);
        for (String attribute : attributeNames) {
            attributes.add(new HerodotusSecurityConfig(attribute.trim()));
        }
        return attributes;
    }
}
