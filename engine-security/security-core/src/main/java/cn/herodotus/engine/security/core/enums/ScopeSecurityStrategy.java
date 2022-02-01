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

package cn.herodotus.engine.security.core.enums;

/**
 * <p>Description: TODO </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/5 10:10
 */
public enum ScopeSecurityStrategy {

    /**
     * 使用OAuth2表达式进行权限校验
     *
     * OAuth2自身的权限表达式：
     * · #oauth2.clientHasRole
     * · #oauth2.clientHasAnyRole
     * · #oauth2.hasScope
     * · #oauth2.hasAnyScope
     * · #oauth2.hasScopeMatching
     * · #oauth2.hasAnyScopeMatching
     * · #oauth2.denyOAuthClient
     * · #oauth2.isOAuth
     * · #oauth2.isUser
     * · #oauth2.isClient
     */
    SECURITY_EXPRESSION,

    /**
     * 使用Role Voter进行基于角色的权限校验
     */
    SCOPE_VOTER;
}
