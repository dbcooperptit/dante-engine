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

import cn.herodotus.engine.rest.core.annotation.AccessLimited;
import cn.herodotus.engine.rest.core.exception.FrequentRequestsException;
import cn.herodotus.engine.rest.secure.stamp.AccessLimitedStampManager;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.format.DateTimeParseException;

/**
 * <p>Description: 访问防刷拦截器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/25 22:09
 */
public class AccessLimitedInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AccessLimitedInterceptor.class);

    private AccessLimitedStampManager accessLimitedStampManager;

    public void setAccessLimitedStampManager(AccessLimitedStampManager accessLimitedStampManager) {
        this.accessLimitedStampManager = accessLimitedStampManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.trace("[Herodotus] |- AccessLimitedInterceptor preHandle postProcess.");

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        AccessLimited accessLimited = method.getAnnotation(AccessLimited.class);
        if (ObjectUtils.isNotEmpty(accessLimited)) {

            int annotationMaxTimes = accessLimited.maxTimes();
            String annotationDuration = accessLimited.duration();
            Duration configuredDuration = Duration.ZERO;
            if (StringUtils.isNotBlank(annotationDuration)) {
                try {
                    configuredDuration = Duration.parse(annotationDuration);
                } catch (DateTimeParseException e) {
                    log.warn("[Herodotus] |- AccessLimited duration value is incorrect, on api [{}].", request.getRequestURI());
                }
            }

            String key = SecureUtil.md5(handlerMethod.toString());
            String expireKey = key + "_expire";
            Long times = accessLimitedStampManager.get(key);

            if (ObjectUtils.isEmpty(times) || times == 0L) {
                if (!configuredDuration.isZero()) {
                    // 如果注解上配置了Duration且没有配置错可以正常解析，那么使用注解上的配置值
                    accessLimitedStampManager.create(key, configuredDuration);
                    accessLimitedStampManager.put(expireKey, System.currentTimeMillis(), configuredDuration);
                } else {
                    // 如果注解上没有配置Duration或者配置错无法正常解析，那么使用StampProperties的配置值
                    accessLimitedStampManager.create(key);
                    accessLimitedStampManager.put(expireKey, System.currentTimeMillis());
                }
                return true;
            } else {
                log.debug("[Herodotus] |- AccessLimitedInterceptor request [{}] times.", times);

                if (times <= annotationMaxTimes) {
                    Duration newDuration = calculateRemainingTime(configuredDuration, expireKey);
                    // 不管是注解上配置Duration值还是StampProperties中配置的Duration值，是不会变的
                    // 所以第一次存入expireKey对应的System.currentTimeMillis()时间后，这个值也不应该变化。
                    // 因此，这里只更新访问次数的标记值
                    accessLimitedStampManager.put(key, times + 1L, newDuration);
                    return true;
                } else {
                    throw new FrequentRequestsException("Requests are too frequent. Please try again later!");
                }
            }
        }

        return true;
    }

    /**
     * 计算剩余过期时间
     *
     * 每次create或者put，缓存的过期时间都会被覆盖。（注意：Jetcache put 方法的参数名：expireAfterWrite）。
     * 因为Jetcache没有Redis的incr之类的方法，那么每次放入Times值，都会更新过期时间，实际操作下来是变相的延长了过期时间。
     *
     * @param configuredDuration 注解上配置的、且可以正常解析的Duration值
     * @param expireKey 时间标记存储Key值。
     * @return 还剩余的过期时间 {@link Duration}
     */
    private Duration calculateRemainingTime(Duration configuredDuration, String expireKey) {
        Long begin = accessLimitedStampManager.get(expireKey);
        Long current = System.currentTimeMillis();
        long interval = current - begin;

        log.debug("[Herodotus] |- AccessLimitedInterceptor operation interval [{}] millis.", interval);

        Duration duration;
        if (!configuredDuration.isZero()) {
            duration = configuredDuration.minusMillis(interval);
        } else {
            duration = accessLimitedStampManager.getExpire().minusMillis(interval);
        }

        return duration;
    }
}
