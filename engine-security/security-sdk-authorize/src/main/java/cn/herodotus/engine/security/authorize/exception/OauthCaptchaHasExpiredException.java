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

package cn.herodotus.engine.security.authorize.exception;

import cn.herodotus.engine.security.core.exception.PlatformAuthenticationException;

/**
 * <p>Description: Oauth2 使用的验证码不匹配错误 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/24 12:04
 */
public class OauthCaptchaHasExpiredException extends PlatformAuthenticationException {

    public OauthCaptchaHasExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OauthCaptchaHasExpiredException(String msg) {
        super(msg);
    }
}
