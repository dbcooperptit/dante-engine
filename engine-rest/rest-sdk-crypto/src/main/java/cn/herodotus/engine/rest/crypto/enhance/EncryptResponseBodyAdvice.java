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

package cn.herodotus.engine.rest.crypto.enhance;

import cn.herodotus.engine.definition.core.constants.HttpHeaders;
import cn.herodotus.engine.definition.jackson.utils.JacksonUtils;
import cn.herodotus.engine.rest.core.annotation.Crypto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p>Description: 响应体加密Advice </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/4 14:30
 */
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

    private InterfaceCryptoProcessor interfaceCryptoProcessor;

    public void setInterfaceCryptoProcessor(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        this.interfaceCryptoProcessor = interfaceCryptoProcessor;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {

        String methodName = methodParameter.getMethod().getName();
        Crypto crypto = methodParameter.getMethodAnnotation(Crypto.class);

        boolean isSupports = ObjectUtils.isNotEmpty(crypto) && crypto.responseEncrypt();

        log.trace("[Herodotus] |- Is EncryptResponseBodyAdvice supports method [{}] ? Status is [{}].", methodName, isSupports);
        return isSupports;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        String sessionKey = request.getHeaders().get(HttpHeaders.X_HERODOTUS_SESSION).get(0);

        if (StringUtils.isBlank(sessionKey)) {
            log.warn("[Herodotus] |- Cannot find Herodotus Cloud custom session header. Use interface crypto founction need add X_HERODOTUS_SESSION to request header.");
            return body;
        }

        log.info("[Herodotus] |- EncryptResponseBodyAdvice begin encrypt data.");

        String methodName = methodParameter.getMethod().getName();
        String className = methodParameter.getDeclaringClass().getName();

        try {
            String bodyString = JacksonUtils.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body);
            String result = interfaceCryptoProcessor.encryptToString(sessionKey, bodyString);
            if (StringUtils.isNotBlank(result)) {
                log.debug("[Herodotus] |- Encrypt response body for rest method [{}] in [{}] finished.", methodName, className);
                return result;
            } else {
                return body;
            }
        } catch (JsonProcessingException e) {
            log.debug("[Herodotus] |- Encrypt response body for rest method [{}] in [{}] catch error, skip encrypt operation.", methodName, className, e);
            return body;
        }
    }
}
