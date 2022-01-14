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

package cn.herodotus.engine.definition.core.support;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;

/**
 * <p>Description: 配置信息读取工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/7 13:39
 */
public class PropertyResolver {

    /**
     * 从环境信息中获取配置信息
     *
     * @param environment Spring Boot Environment {@link Environment}
     * @param property    配置名称
     * @return 配置属性值
     */
    public static String getProperty(Environment environment, String property) {
        return environment.getProperty(property);
    }

    /**
     * 从环境信息中获取配置信息
     *
     * @param environment  Spring Boot Environment {@link Environment}
     * @param property     配置名称
     * @param defaultValue 默认值
     * @return 配置属性值
     */
    public static String getProperty(Environment environment, String property, String defaultValue) {
        return environment.getProperty(property, defaultValue);
    }

    /**
     * 从条件上下文中获取配置信息
     *
     * @param conditionContext Spring Boot ConditionContext {@link ConditionContext}
     * @param property         配置名称
     * @return 配置属性值
     */
    public static String getProperty(ConditionContext conditionContext, String property) {
        return getProperty(conditionContext.getEnvironment(), property);
    }

    /**
     * 从条件上下文中获取配置信息
     *
     * @param conditionContext Spring Boot ConditionContext {@link ConditionContext}
     * @param property         配置名称
     * @param defaultValue     默认值
     * @return 配置属性值
     */
    public static String getProperty(ConditionContext conditionContext, String property, String defaultValue) {
        return getProperty(conditionContext.getEnvironment(), property, defaultValue);
    }
}
