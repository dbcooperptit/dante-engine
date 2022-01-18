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

package cn.herodotus.engine.temporal.influxdb.support;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>Description: Influxdb 操作模版 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/17 18:26
 */
@Component
public class InfluxdbTemplate {

    private static final Logger log = LoggerFactory.getLogger(InfluxdbTemplate.class);

    private final InfluxDB influxdb;

    public InfluxdbTemplate(InfluxDB influxdb) {
        this.influxdb = influxdb;
    }

    /**
     * 测试连接是否正常
     *
     * @return true 正常
     */
    public boolean ping() {
        boolean isConnected = false;
        Pong pong;
        try {
            pong = influxdb.ping();
            if (pong != null) {
                isConnected = true;
            }
        } catch (Exception e) {
            log.error("[Herodotus] |- Influxdb ping the connection error.", e);
        }
        return isConnected;
    }
}
