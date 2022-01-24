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

package cn.herodotus.engine.web.core.utils;

import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>Description:  Http与Servlet工具类. </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/4 11:39
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

    private static final Logger log = LoggerFactory.getLogger(WebUtils.class);

    public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public static PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     */
    public static String getCookieValue(String name) {
        HttpServletRequest request = WebUtils.getRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieValue(request, name);
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    public static void renderJson(HttpServletResponse response, Object object) {
        renderJson(response, JSON.toJSONString(object), MediaType.APPLICATION_JSON.toString());
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    public static void renderJson(HttpServletResponse response, String string, String type) {
        try {
            response.setContentType(type);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().print(string);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error("[Herodotus] |- Render response to Json error!");
        }
    }

    /**
     * 获取ip
     *
     * @return {String}
     */
    public String getIP() {
        return getIP(WebUtils.getRequest());
    }

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {String}
     */
    public String getIP(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(cn.herodotus.engine.assistant.core.constants.HttpHeaders.PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(cn.herodotus.engine.assistant.core.constants.HttpHeaders.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(cn.herodotus.engine.assistant.core.constants.HttpHeaders.HTTP_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(cn.herodotus.engine.assistant.core.constants.HttpHeaders.HTTP_X_FORWARDED_FOR);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(cn.herodotus.engine.assistant.core.constants.HttpHeaders.X_REAL_IP);
        }
        if (StringUtils.isBlank(ip) || cn.herodotus.engine.assistant.core.constants.HttpHeaders.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.isBlank(ip) ? null : ip.split(SymbolConstants.COMMA)[0];
    }

    public static HttpServletRequest toHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    public static HttpServletResponse toHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }


    private static boolean isHeaderContainMediaType(ServletRequest request, String headerType, String mediaType) {
        String header = toHttp(request).getHeader(headerType);
        return StringUtils.isNotEmpty(header) && header.contains(mediaType);
    }

    private static boolean isHeaderContainJson(ServletRequest request, String headerType) {
        return isHeaderContainMediaType(request, headerType, MediaType.APPLICATION_JSON_VALUE);
    }

    private static boolean isContentTypeHeaderContainJson(ServletRequest request) {
        return isHeaderContainJson(request, HttpHeaders.CONTENT_TYPE);
    }

    private static boolean isAcceptHeaderContainJson(ServletRequest request) {
        return isHeaderContainJson(request, HttpHeaders.ACCEPT);
    }

    private static boolean isContainAjaxFlag(ServletRequest request) {
        String xRequestedWith = WebUtils.toHttp(request).getHeader(HttpHeaders.X_REQUESTED_WITH);
        return XML_HTTP_REQUEST.equalsIgnoreCase(xRequestedWith);
    }

    public static boolean isAjaxRequest(ServletRequest request) {

        //使用HttpServletRequest中的header检测请求是否为ajax, 如果是ajax则返回json, 如果为非ajax则返回view(即ModelAndView)
        if (isContentTypeHeaderContainJson(request) || isAcceptHeaderContainJson(request) || isContainAjaxFlag(request)) {
            log.trace("[Herodotus] |- Is Ajax Request!!!!!");
            return true;
        }

        log.trace("[Herodotus] |- Not a Ajax Request!!!!!");
        return false;
    }

    public static boolean isStaticResourcesRequest(HttpServletRequest request) {
        String requestPath = request.getServletPath();
        if (StringUtils.endsWith(requestPath, "html")) {
            return false;
        } else {
            return isStaticResources(requestPath);
        }
    }

    public static boolean isStaticResources(String uri) {
        ResourceUrlProvider resourceUrlProvider = SpringUtil.getBean(ResourceUrlProvider.class);
        String staticUri = resourceUrlProvider.getForLookupPath(uri);
        return staticUri != null;
    }

    /**
     * 判断路径是否与路径模式匹配
     *
     * @param pattern 路径模式
     * @param path    url
     * @return 是否匹配
     */
    public static boolean isPathMatch(String pattern, String path) {
        return getPathMatcher().match(pattern, path);
    }

    /**
     * 判断路径是否与路径模式匹配
     *
     * @param patterns 路径模式数组
     * @param path     url
     * @return 是否匹配
     */
    public static boolean isPathMatch(String[] patterns, String path) {
        return isPathMatch(Arrays.asList(patterns), path);
    }

    /**
     * 判断路径是否与路径模式匹配
     *
     * @param patterns 路径模式字符串List
     * @param path     url
     * @return 是否匹配
     */
    public static boolean isPathMatch(List<String> patterns, String path) {
        PathMatcher pathMatcher = getPathMatcher();
        for (String pattern : patterns) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从Map中获取url匹配的对象。
     *
     * @param path 传入的url
     * @param map  以路径匹配pattern为key的map
     * @param <T>  返回的对象
     * @return T
     */
    public static <T> T getPathMatchedObject(String path, Map<String, T> map) {
        PathMatcher pathMatcher = getPathMatcher();
        for (String key : map.keySet()) {
            if (pathMatcher.match(key, path)) {
                return map.get(key);
            }
        }

        return null;
    }

    public static String getRequestPayload(ServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader();) {
            char[] buffer = new char[1024];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, length);
            }
        } catch (IOException e) {
            log.error("[Herodotus] |- Get Request Payload Error!");
        }

        return stringBuilder.toString();
    }
}
