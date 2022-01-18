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

package cn.herodotus.engine.temporal.influxdb.condition;

import cn.herodotus.engine.assistant.core.support.PropertyResolver;
import cn.herodotus.engine.temporal.core.constants.TemporalConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: Influxdb 注入开启条件 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/17 18:06
 */
public class InfluxdbEnabledCondition implements Condition {

    private static final Logger log = LoggerFactory.getLogger(InfluxdbEnabledCondition.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        String url = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_URL);
        String database = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_DATABASE);
        String username = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_USERNAME);
        String password = PropertyResolver.getProperty(conditionContext, TemporalConstants.ITEM_INFLUXDB_PASSWORD);
        boolean result = StringUtils.isNotBlank(url) && StringUtils.isNotBlank(database) && StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
        log.debug("[Herodotus] |- Condition [Influxdb Enabled] value is [{}]", result);
        return result;
    }
}
