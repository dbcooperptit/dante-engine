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

package cn.herodotus.engine.oauth2.core.enums;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 认证模式枚举 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/16 14:39
 */
public enum GrantType {

    /**
     * enum
     */
    AUTHORIZATION_CODE(BaseConstants.AUTHORIZATION_CODE, "Authorization Code 模式"),
    PASSWORD(BaseConstants.PASSWORD, "Password 模式"),
    CLIENT_CREDENTIALS(BaseConstants.CLIENT_CREDENTIALS, "Client Credentials 模式"),
    REFRESH_TOKEN(BaseConstants.REFRESH_TOKEN, "Refresh Token 模式"),
    SOCIAL_AUTHENTICATION(BaseConstants.SOCIAL_CREDENTIALS, "Social Credentials 模式");

    private final String grant;
    private final String description;

    private static final Map<Integer, GrantType> indexMap = new HashMap<>();
    private static final List<Map<String, Object>> toJsonStruct = new ArrayList<>();

    static {
        for (GrantType grantType : GrantType.values()) {
            indexMap.put(grantType.ordinal(), grantType);
            toJsonStruct.add(grantType.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", grantType.getGrant())
                            .put("key", grantType.name())
                            .put("text", grantType.getDescription())
                            .put("index", grantType.ordinal())
                            .build());
        }
    }

    GrantType(String grant, String description) {
        this.grant = grant;
        this.description = description;
    }

    public String getGrant() {
        return grant;
    }

    public String getDescription() {
        return description;
    }

    public static GrantType getGrant(Integer grant) {
        return indexMap.get(grant);
    }

    public static List<Map<String, Object>> getToJsonStruct() {
        return toJsonStruct;
    }
}
