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

package cn.herodotus.engine.oss.minio.condition;

import cn.herodotus.engine.assistant.core.support.PropertyResolver;
import cn.herodotus.engine.oss.core.constants.OssConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: Minio是否开启条件 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 11:16
 */
public class MinioEnabledCondition implements Condition {

    private static final Logger log = LoggerFactory.getLogger(MinioEnabledCondition.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        String endpoint = PropertyResolver.getProperty(conditionContext, OssConstants.ITEM_MINIO_ENDPOINT);
        String accessKey = PropertyResolver.getProperty(conditionContext, OssConstants.ITEM_MINIO_ACCESSKEY);
        String secretkey = PropertyResolver.getProperty(conditionContext, OssConstants.ITEM_MINIO_SECRETKEY);
        boolean result = StringUtils.isNotBlank(endpoint) && StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(secretkey);
        log.debug("[Herodotus] |- Condition [Minio Enabled] value is [{}]", result);
        return result;
    }
}
