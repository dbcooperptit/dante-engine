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
import cn.herodotus.engine.rest.core.annotation.Crypto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: @RequestParam 解密处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/10 12:17
 */
public class DecryptRequestParamResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(DecryptRequestParamResolver.class);

    private InterfaceCryptoProcessor interfaceCryptoProcessor;
    private RequestParamMethodArgumentResolver requestParamMethodArgumentResolver;

    public void setRequestParamMethodArgumentResolver(RequestParamMethodArgumentResolver requestParamMethodArgumentResolver) {
        this.requestParamMethodArgumentResolver = requestParamMethodArgumentResolver;
    }

    public void setInterfaceCryptoProcessor(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        this.interfaceCryptoProcessor = interfaceCryptoProcessor;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        String methodName = methodParameter.getMethod().getName();
        boolean isSupports = isConfigCrypto(methodParameter) && requestParamMethodArgumentResolver.supportsParameter(methodParameter);

        log.trace("[Herodotus] |- Is DecryptRequestParamResolver supports method [{}] ? Status is [{}].", methodName, isSupports);
        return isSupports;
    }

    /**
     * 判断该接口方法是否用@Crypto注解标记，同时requestDecrypt的值是true
     *
     * @param methodParameter {@link MethodParameter}
     * @return 是否开启了自定义@Crypto
     */
    private boolean isConfigCrypto(MethodParameter methodParameter) {
        Crypto crypto = methodParameter.getMethodAnnotation(Crypto.class);
        return ObjectUtils.isNotEmpty(crypto) && crypto.requestDecrypt();
    }

    /**
     * 是否是常规的请求
     *
     * @param webRequest {@link NativeWebRequest}
     * @return boolean
     */
    private boolean isRegularRequest(NativeWebRequest webRequest) {
        MultipartRequest multipartRequest = webRequest.getNativeRequest(MultipartRequest.class);
        return ObjectUtils.isEmpty(multipartRequest);
    }

    private String[] decrypt(String sessionId, String[] paramValues) {
        List<String> values = Arrays.stream(paramValues).map(paramValue -> interfaceCryptoProcessor.decryptToString(sessionId, paramValue)).collect(Collectors.toList());
        String[] result = new String[values.size()];
        return values.toArray(result);
    }

    @Override
    @Nullable
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        if (isRegularRequest(webRequest)) {

            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            String sessionId = request.getHeader(HttpHeaders.X_HERODOTUS_SESSION);

            if (StringUtils.isNotBlank(sessionId)) {
                    String[] paramValues = request.getParameterValues(methodParameter.getParameterName());
                if (ArrayUtils.isNotEmpty(paramValues)) {
                    String[] values = decrypt(sessionId, paramValues);
                    return (values.length == 1 ? values[0] : values);
                }
            } else {
                log.warn("[Herodotus] |- Cannot find Herodotus Cloud custom session header. Use interface crypto founction need add X_HERODOTUS_SESSION to request header.");
            }
        }

        log.debug("[Herodotus] |- The decryption conditions are not met DecryptRequestParamResolver, skip! to next!");
        return requestParamMethodArgumentResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }
}
