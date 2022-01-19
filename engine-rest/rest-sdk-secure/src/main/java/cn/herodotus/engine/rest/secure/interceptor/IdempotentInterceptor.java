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

package cn.herodotus.engine.rest.secure.interceptor;

import cn.herodotus.engine.assistant.core.constants.HttpHeaders;
import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.herodotus.engine.rest.core.annotation.Idempotent;
import cn.herodotus.engine.rest.secure.exception.RepeatSubmissionException;
import cn.herodotus.engine.rest.secure.stamp.IdempotentStampManager;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * <p>Description: 幂等拦截器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/22 15:31
 */
public class IdempotentInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(IdempotentInterceptor.class);

    private static final String IDEMPOTENT_ATTRIBUTE = "Idempotent";

    private IdempotentStampManager idempotentStampManager;

    public void setIdempotentStampManager(IdempotentStampManager idempotentStampManager) {
        this.idempotentStampManager = idempotentStampManager;
    }

    private String generateKey(String sessionId, String url, String method) {

        if (StringUtils.isNotBlank(sessionId)) {
            String key = SecureUtil.md5(sessionId + SymbolConstants.COLON + url + SymbolConstants.COLON + method);
            log.debug("[Herodotus] |- IdempotentInterceptor key is [{}].", key);
            return key;
        } else {
            log.warn("[Herodotus] |- IdempotentInterceptor cannot create key, because sessionId is null.");
            return null;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.trace("[Herodotus] |- IdempotentInterceptor preHandle postProcess.");

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        if (idempotent != null) {
            // 幂等性校验, 根据缓存中是否存在Token进行校验。
            // 如果缓存中没有Token，通过放行, 同时在缓存中存入Token。
            // 如果缓存中有Token，意味着同一个操作反复操作，认为失败则抛出异常, 并通过统一异常处理返回友好提示
            String sessionId = request.getHeader(HttpHeaders.X_HERODOTUS_SESSION);
            if (StringUtils.isBlank(sessionId)) {
                HttpSession session = request.getSession();
                sessionId = session.getId();
            }

            String key = generateKey(sessionId, request.getRequestURI(), request.getMethod());
            if (StringUtils.isNotBlank(key)) {
                String token = idempotentStampManager.get(key);
                if (StringUtils.isBlank(token)) {
                    Duration configuredDuration = Duration.ZERO;
                    String annotationExpire = idempotent.expire();
                    if (StringUtils.isNotBlank(annotationExpire)) {
                        try {
                            configuredDuration = Duration.parse(annotationExpire);
                        } catch (DateTimeParseException e) {
                            log.warn("[Herodotus] |- AccessLimited duration value is incorrect, on api [{}].", request.getRequestURI());
                        }
                    }

                    if (!configuredDuration.isZero()) {
                        idempotentStampManager.create(key, configuredDuration);
                    } else {
                        idempotentStampManager.create(key);
                    }

                    request.setAttribute(IDEMPOTENT_ATTRIBUTE, key);

                    return true;
                } else {
                    throw new RepeatSubmissionException("Don't Repeat Submission");
                }
            }
        }

        return true;
    }
}
