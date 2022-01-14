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

package cn.herodotus.engine.definition.core.constants;

/**
 * <p>Description: 认证授权等安全相关常量值 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/18 8:37
 */
public interface SecurityConstants {

    String ENDPOINT_OAUTH_TOKEN = "/oauth/token";

    String ENDPOINT_OAUTH_AUTHORIZE = "/oauth/authorize";

    String ENDPOINT_OAUTH_CHECK_TOKEN = "/oauth/check_token";

    String ENDPOINT_OAUTH_IDENTITY_PROFILE = "/identity/profile";

    /**
     * Oauth2 四种模式类型
     */
    String AUTHORIZATION_CODE = "authorization_code";
    String IMPLICIT = "implicit";
    String PASSWORD = "password";
    String CLIENT_CREDENTIALS = "client_credentials";
    String REFRESH_TOKEN = "refresh_token";
    String SOCIAL_AUTHENTICATION = "social_authentication";

    String OPEN_API_SECURITY_SCHEME_BEARER_NAME = "HERODOTUS_AUTH";

    /**
     * 访问 /oauth/authorize地址的两种 response_type类型值
     */

    String TOKEN = "token";

    String BEARER_TYPE = "Bearer";
    String BEARER_TOKEN = BEARER_TYPE + SymbolConstants.SPACE;

    String BASIC_TYPE = "Basic";
    String BASIC_TOKEN = BASIC_TYPE + SymbolConstants.SPACE;

    String ROLE_PREFIX = "ROLE_";
    String AUTHORITY_PREFIX = "OP_";
    String AUTHORITY_PREFIX_API = "API_";

    String OPEN_ID = "openid";
    String LICENSE = "license";

    String CODE = "code";
}
