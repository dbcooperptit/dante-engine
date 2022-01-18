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

package cn.herodotus.engine.temporal.influxdb.configuration;

import cn.herodotus.engine.temporal.influxdb.annotation.ConditionalOnInfluxdbEnabled;
import cn.herodotus.engine.temporal.influxdb.properties.InfluxdbProperties;
import cn.herodotus.engine.temporal.influxdb.support.InfluxdbTemplate;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: Influxdb 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/17 17:39
 */
@Configuration
@ConditionalOnInfluxdbEnabled
@EnableConfigurationProperties({InfluxdbProperties.class})
public class InfluxdbConfiguration {

    private static final Logger log = LoggerFactory.getLogger(InfluxdbConfiguration.class);

    @PostConstruct
    public void init() {
        log.info("[Herodotus] |- Plugin [Herodotus Influxdb] Auto Configure.");
    }

    @Bean
    public InfluxDB influxdb(InfluxdbProperties influxdbProperties) {

        InfluxDB influxdb;
        if (StringUtils.isNotBlank(influxdbProperties.getUsername()) && StringUtils.isNotBlank(influxdbProperties.getPassword())) {
            influxdb = InfluxDBFactory.connect(influxdbProperties.getUrl(), influxdbProperties.getUsername(), influxdbProperties.getPassword());
        } else {
            influxdb = InfluxDBFactory.connect(influxdbProperties.getUrl());
        }

        try {
            /**
             * 异步插入：
             * enableBatch这里第一个是point的个数，第二个是时间，单位毫秒
             * point的个数和时间是联合使用的，如果满100条或者2000毫秒
             * 满足任何一个条件就会发送一次写的请求。
             */
            influxdb.setDatabase(influxdbProperties.getDatabase()).enableBatch(100, 2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("[Herodotus] |- Influxdb set database catch error.", e);
        } finally {
            influxdb.setRetentionPolicy("autogen");
        }
        influxdb.setLogLevel(InfluxDB.LogLevel.BASIC);
        return influxdb;
    }

    @Bean
    @ConditionalOnBean(InfluxDB.class)
    public InfluxdbTemplate influxdbTemplate(InfluxDB influxdb) {
        InfluxdbTemplate influxdbTemplate = new InfluxdbTemplate(influxdb);
        log.trace("[Herodotus] |- Bean [Influxdb Template Auto Configure.");
        return influxdbTemplate;
    }
}
