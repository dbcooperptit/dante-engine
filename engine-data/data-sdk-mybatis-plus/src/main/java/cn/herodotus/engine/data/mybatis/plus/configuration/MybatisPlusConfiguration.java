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

package cn.herodotus.engine.data.mybatis.plus.configuration;

import cn.herodotus.engine.data.core.constants.DataConstants;
import cn.herodotus.engine.data.mybatis.plus.enhance.HerodotusIdentifierGenerator;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: Mybatis Plus 配置</p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/28 11:48
 */
@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlusConfiguration.class);

    @Value(DataConstants.ANNOTATION_SQL_INIT_PLATFORM)
    private String platform;

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Data Mybatis Plus] Auto Configure.");
    }

    private DbType parseDbType() {
        if (StringUtils.isNotBlank(platform)) {
            DbType type = DbType.getDbType(platform);
            if (ObjectUtils.isNotEmpty(type)) {
                return type;
            }
        }

        return DbType.POSTGRE_SQL;
    }

    /**
     * 防止 修改与删除时对全表进行操作
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(parseDbType()));
        log.trace("[Herodotus] |- Bean [Mybatis Plus Interceptor] Auto Configure.");
        return mybatisPlusInterceptor;
    }

    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        log.trace("[Herodotus] |- Bean [Block Attack Inner Interceptor] Auto Configure.");
        return blockAttackInnerInterceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        HerodotusIdentifierGenerator herodotusIdentifierGenerator = new HerodotusIdentifierGenerator();
        log.trace("[Herodotus] |- Bean [Herodotus Identifier Generator] Auto Configure.");
        return herodotusIdentifierGenerator;
    }
}
