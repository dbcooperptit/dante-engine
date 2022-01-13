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

package cn.herodotus.engine.definition.common.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> Description : ThreadLocal工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/5 17:22
 */
public class ThreadLocalContextUtils {

    private static final String TENANT_ID = "tenantId";

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        set(TENANT_ID, tenantId);
    }

    public static String getTenantId() {
        return getString(TENANT_ID);
    }

    public static String getString(String attribute) {
        Object object = get(attribute);
        if (ObjectUtils.isNotEmpty(object) && object instanceof String) {
            return (String) object;
        }

        return null;
    }

    /**
     * 获得线程中保存的属性.
     *
     * @param attribute 属性名称
     * @return 属性值
     */
    public static Object get(String attribute) {
        Map<String, Object> map = threadLocal.get();
        if (MapUtils.isEmpty(map)) {
            return null;
        }

        return map.get(attribute);
    }

    public static void set(String attribute, Object value) {
        Map<String, Object> map = threadLocal.get();

        if (MapUtils.isEmpty(map)) {
            map = new ConcurrentHashMap<>(8);
        }
        map.put(attribute, value);
        threadLocal.set(map);
    }

    /**
     * 清除线程中保存的数据
     */
    public static void clear() {
        threadLocal.remove();
    }

}
