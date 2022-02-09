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

package cn.herodotus.engine.facility.sentinel.enhance;

import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.hutool.extra.servlet.ServletUtil;
import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>Description: 自定义FeignRequestInterceptor </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/6/8 12:01
 */
public class HerodotusFeignRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(HerodotusFeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();

        if (httpServletRequest != null) {
            Map<String, String> headers = ServletUtil.getHeaderMap(httpServletRequest);
            // 传递所有请求头,防止部分丢失
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                // 跳过content-length值的复制。因为服务之间调用需要携带一些用户信息之类的 所以实现了Feign的RequestInterceptor拦截器复制请求头，复制的时候是所有头都复制的,可能导致Content-length长度跟body不一致
                // @see https://blog.csdn.net/qq_39986681/article/details/107138740
                if (StringUtils.equalsIgnoreCase(key, HttpHeaders.CONTENT_LENGTH)) {
                    continue;
                }

                // 解决 UserAgent 信息被修改后，AppleWebKit/537.36 (KHTML,like Gecko)部分存在非法字符的问题
                if (StringUtils.equalsIgnoreCase(key, HttpHeaders.USER_AGENT)) {
                    value = StringUtils.replace(value, SymbolConstants.NEW_LINE, SymbolConstants.BLANK);
                    entry.setValue(value);
                }

                requestTemplate.header(key, value);
            }

            log.debug("[Herodotus] |- FeignRequestInterceptor copy all need transfer header!");

            // 微服务之间传递的唯一标识,区分大小写所以通过httpServletRequest查询
            if (headers.containsKey(HttpHeaders.X_REQUEST_ID)) {
                String traceId = headers.get(HttpHeaders.X_REQUEST_ID);
                MDC.put("traceId", traceId);
                log.info("[Herodotus] |- Feign Request Interceptor Trace: {}", traceId);
            }
        }

        log.trace("[Herodotus] |- Feign Request Interceptor [{}]", requestTemplate.toString());
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            log.error("[Herodotus] |- Feign Request Interceptor can not get Request.");
            return null;
        }
    }
}
