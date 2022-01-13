/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.redisson.annotation;

import cn.herodotus.engine.cache.core.constants.CachePropertyConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * <p>Description: 是否开启 Redisson 条件注解 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/22 14:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnProperty(value = CachePropertyConstants.ITEM_REDISSON_ENABLED, havingValue = "true")
public @interface ConditionalOnRedissonEnabled {
}
