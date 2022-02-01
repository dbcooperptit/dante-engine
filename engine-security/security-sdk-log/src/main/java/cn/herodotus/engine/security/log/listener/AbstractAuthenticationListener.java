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

import cn.herodotus.engine.security.core.definition.domain.HerodotusUserDetails;
import cn.herodotus.engine.security.log.domain.Signin;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 认证监听基础类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/19 16:36
 */
public abstract class AbstractAuthenticationListener {

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> getDetail(Authentication authentication) {
        Object details = authentication.getDetails();

        if (details instanceof LinkedHashMap) {
            return (LinkedHashMap<String, String>) details;
        }
        return null;
    }

    protected HerodotusUserDetails  getUserDetails(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof HerodotusUserDetails) {
            return  (HerodotusUserDetails) principal;
        }
        return null;
    }

    protected UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
    }

    protected String getIp(HttpServletRequest request) {
        return ServletUtil.getClientIP(request, "");
    }

    protected Signin getSignin(Authentication authentication) {

        Signin signin = new Signin();

        HttpServletRequest request = getRequest();

        if (ObjectUtils.isNotEmpty(request)) {
            UserAgent userAgent = getUserAgent(request);
            if (ObjectUtils.isNotEmpty(userAgent)) {
                //获取ip
                signin.setIp(getIp(request));
                // 获取客户端操作系统
                signin.setOs(userAgent.getOs().getName());
                // 获取客户端浏览器
                signin.setBrowser( userAgent.getBrowser().getName());
                signin.setEngine(userAgent.getEngine().getName());
                signin.setPlatform(userAgent.getPlatform().getName());
                signin.setMobile(userAgent.isMobile());
            }
        }

        HerodotusUserDetails herodotusUserDetails = getUserDetails(authentication);
        if (ObjectUtils.isNotEmpty(herodotusUserDetails)) {
            signin.setUserId(herodotusUserDetails.getUserId());
            signin.setUserName(herodotusUserDetails.getUsername());
        }

        return signin;
    }
}
