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

package cn.herodotus.engine.security.log.listener;

import cn.herodotus.engine.security.log.domain.Signin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;

/**
 * <p>Description: 登出成功监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/18 17:58
 */
public class AuthenticationFailureListener extends AbstractAuthenticationListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String message;
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            //提供的凭据是错误的，用户名或者密码错误
            message = "提供的凭据是错误的，用户名或者密码错误";
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            message = "验证通过，但是密码过期";
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            message = "验证过了但是账户被禁用";
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            message = "验证通过了，但是账号已经过期";
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            message = "账户被锁定";
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            message = "配置错误";
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            // 代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            message = "代理不受信任";
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            // 其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            message = "内部发生的异常";
        } else {
            message = "其他未知错误";
        }

        Authentication authentication = event.getAuthentication();
        Signin signin = this.getSignin(authentication);
        signin.setStatus(0);
        signin.setRemark(message);
        signin.setTimestamp(event.getTimestamp());

        log.debug("[Herodotus] |- Current signin user is [{}]", signin);
    }
}
