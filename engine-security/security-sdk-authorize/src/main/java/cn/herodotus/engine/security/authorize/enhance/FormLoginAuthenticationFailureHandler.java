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

package cn.herodotus.engine.security.authorize.enhance;

import cn.herodotus.engine.security.authorize.exception.OauthCaptchaArgumentIllegalException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaHasExpiredException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaIsEmptyException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaMismatchException;
import cn.herodotus.engine.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description : 表单登录失败处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/1/26 18:08
 */
public class FormLoginAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(FormLoginAuthenticationFailureHandler.class);

    public static final String ERROR_MESSAGE_KEY = "SPRING_SECURITY_LAST_EXCEPTION_CUSTOM_MESSAGE";

    private Map<String, String> exceptionDictionary;

    public FormLoginAuthenticationFailureHandler(SecurityProperties securityProperties) {
        this.initExceptionMappings(securityProperties.getLogin().getFailureUrl());
        this.initExceptionDictionary();
    }

    private void initExceptionMappings(String failureUrl) {
        Map<String, String> exceptionMappings = new HashMap<>(8);
        exceptionMappings.put(UsernameNotFoundException.class.getName(), failureUrl);
        exceptionMappings.put(DisabledException.class.getName(), failureUrl);
        exceptionMappings.put(AccountExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(CredentialsExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(BadCredentialsException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaArgumentIllegalException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaHasExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaMismatchException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaIsEmptyException.class.getName(), failureUrl);
        this.setExceptionMappings(exceptionMappings);
    }

    private void initExceptionDictionary() {
        this.exceptionDictionary = new HashMap<>(8);
        exceptionDictionary.put("UsernameNotFoundException", "用户名/密码无效");
        exceptionDictionary.put("DisabledException", "用户已被禁用");
        exceptionDictionary.put("AccountExpiredException", "账号已过期");
        exceptionDictionary.put("CredentialsExpiredException", "凭证已过期");
        exceptionDictionary.put("BadCredentialsException", "用户名/密码无效");
        exceptionDictionary.put("OauthCaptchaArgumentIllegalException", "请输入验证码！");
        exceptionDictionary.put("OauthCaptchaHasExpiredException", "验证码已过期！请刷新重试");
        exceptionDictionary.put("OauthCaptchaMismatchException", "验证码输入错误！");
        exceptionDictionary.put("OauthCaptchaIsEmptyException", "请输入验证码！");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        String errorMessage = "请刷新重试！";

        String exceptionName = e.getClass().getSimpleName();
        if (StringUtils.isNotEmpty(exceptionName)) {
            if (exceptionDictionary.containsKey(exceptionName)) {
                errorMessage = exceptionDictionary.get(exceptionName);
            } else {
                log.warn("[Herodotus] |- Form Login Authentication Failur eHandler,  Can not find the exception name [{}] in dictionary, please do optimize ", exceptionName);
            }
        }

        if (this.isUseForward()) {
            httpServletRequest.setAttribute(ERROR_MESSAGE_KEY, errorMessage);
        } else {
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null || this.isAllowSessionCreation()) {
                httpServletRequest.getSession().setAttribute(ERROR_MESSAGE_KEY, errorMessage);
            }
        }

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }
}
