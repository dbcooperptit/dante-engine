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

package interceptor;

import cn.herodotus.engine.assistant.core.constants.HttpHeaders;
import domain.WebSocketPrincipal;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import properties.WebSocketProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * <p>Description: 设置认证用户信息 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:52
 */
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

    private static final Logger log = LoggerFactory.getLogger(WebSocketHandshakeHandler.class);

    private WebSocketProperties webSocketProperties;

    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        Principal principal = request.getPrincipal();
        if (ObjectUtils.isNotEmpty(principal)) {
            log.debug("[Herodotus] |- Get user principal from request, value is  [{}].", principal.getName());
            return principal;
        }

        Object user = null;
        HttpServletRequest httpServletRequest = getHttpServletRequest(request);
        if (ObjectUtils.isNotEmpty(httpServletRequest)) {
            user = httpServletRequest.getAttribute(webSocketProperties.getPrincipalAttribute());
            if (ObjectUtils.isEmpty(user)) {
                user = httpServletRequest.getParameter(webSocketProperties.getPrincipalAttribute());
                if (ObjectUtils.isEmpty(user)) {
                    user = httpServletRequest.getHeader(HttpHeaders.X_HERODOTUS_SESSION);
                } else {
                    log.debug("[Herodotus] |- Get user principal [{}] from request parameter, use parameter  [{}]..", user, webSocketProperties.getPrincipalAttribute());
                }
            } else {
                log.debug("[Herodotus] |- Get user principal [{}] from request attribute, use attribute  [{}]..", user, webSocketProperties.getPrincipalAttribute());
            }
        }

        if (ObjectUtils.isEmpty(user)) {
            HttpSession httpSession = getSession(request);
            if (ObjectUtils.isNotEmpty(httpSession)) {
                user = httpSession.getAttribute(webSocketProperties.getPrincipalAttribute());
                if (ObjectUtils.isEmpty(user)) {
                    user = httpSession.getId();
                } else {
                    log.debug("[Herodotus] |- Get user principal [{}] from httpsession, use attribute  [{}].", user, webSocketProperties.getPrincipalAttribute());
                }
            } else {
                log.error("[Herodotus] |- Cannot find session from websocket request.");
                return null;
            }
        } else {
            log.debug("[Herodotus] |- Get user principal [{}] from request header X_HERODOTUS_SESSION.", user);
        }

        return new WebSocketPrincipal((String) user);
    }

    private HttpServletRequest getHttpServletRequest(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
            return serverRequest.getServletRequest();
        }

        return null;
    }

    private HttpSession getSession(ServerHttpRequest request) {
        HttpServletRequest httpServletRequest = getHttpServletRequest(request);
        if (ObjectUtils.isNotEmpty(httpServletRequest)) {
            return httpServletRequest.getSession(false);
        }
        return null;
    }
}
