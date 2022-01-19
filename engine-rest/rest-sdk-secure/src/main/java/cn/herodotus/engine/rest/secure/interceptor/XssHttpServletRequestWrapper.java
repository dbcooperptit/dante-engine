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

import cn.herodotus.engine.assistant.secure.utils.XssUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Description: Xss 请求参数包装器  </p>
 * <p>
 * Content-Type	传参方式	接收方式
 * application/x-www-form-urlencoded	表单key-value	HttpServletRequest Parameters 获取
 * multipart/form-data	表单key-value	HttpServletRequest Parameters 获取
 * application/json	json格式文本	HttpServletRequest IO流获取
 * <p>
 * 本过滤器主要针对表单提交的参数过滤
 *
 * @author : gengwei.zheng
 * @date : 2021/8/29 21:30
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger log = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 使用AntiSamy清洗数据
     *
     * @param value 需要清洗的数据
     * @return 清洗后的数据
     */
    private String cleaning(String value) {
        return XssUtils.cleaning(value);
    }

    private String[] cleaning(String[] parameters) {
        List<String> cleanParameters = Arrays.stream(parameters).map(XssUtils::cleaning).collect(Collectors.toList());
        String[] results = new String[cleanParameters.size()];
        return cleanParameters.toArray(results);
    }

    /**
     * 过滤请求头
     *
     * @param name 参数名
     * @return 参数值
     */
    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        // 如果Header为空，则直接返回，否则进行清洗
        return StringUtils.isBlank(header) ? header : cleaning(header);
    }

    @Override
    public String getParameter(String name) {
        String header = super.getHeader(name);
        // 如果Header为空，则直接返回，否则进行清洗
        return StringUtils.isBlank(header) ? header : cleaning(header);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (ArrayUtils.isNotEmpty(parameterValues)) {
            return cleaning(parameterValues);
        }
        return super.getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        return parameterMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> cleaning(entry.getValue())));
    }
}
