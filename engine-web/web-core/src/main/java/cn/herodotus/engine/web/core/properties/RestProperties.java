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

package cn.herodotus.engine.web.core.properties;

import cn.herodotus.engine.web.core.constants.WebPropertyConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: Rest相关配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/5/31 16:39
 */
@ConfigurationProperties(prefix = WebPropertyConstants.PROPERTY_PLATFORM_REST)
public class RestProperties {

    private RequestMapping requestMapping = new RequestMapping();
    private RestTemplate restTemplate = new RestTemplate();

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class RequestMapping implements Serializable {
        /**
         * 指定扫描的命名空间。未指定的命名空间中，即使包含RequestMapping，也不会被添加进来。
         */
        private List<String> scanGroupIds;
        /**
         * Spring 中会包含 Controller和 RestController，
         * 如果该配置设置为True，那么就只扫描RestController
         * 如果该配置设置为False，那么Controller和 RestController斗扫描。
         */
        private boolean justScanRestController = false;

        public void setScanGroupIds(List<String> scanGroupIds) {
            this.scanGroupIds = scanGroupIds;
        }

        public List<String> getScanGroupIds() {
            List<String> defaultGroupIds = Stream.of("cn.herodotus.engine", "cn.herodotus.cloud").collect(Collectors.toList());

            if (CollectionUtils.isEmpty(this.scanGroupIds)) {
                this.scanGroupIds = new ArrayList<>();
            }

            this.scanGroupIds.addAll(defaultGroupIds);
            return scanGroupIds;
        }

        public boolean isJustScanRestController() {
            return justScanRestController;
        }

        public void setJustScanRestController(boolean justScanRestController) {
            this.justScanRestController = justScanRestController;
        }
    }

    public static class RestTemplate implements Serializable {
        /**
         * RestTemplate 读取超时5秒,默认无限限制,单位：毫秒
         */
        private int readTimeout = 15000;
        /**
         * 连接超时15秒，默认无限制，单位：毫秒
         */
        private int connectTimeout = 15000;

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }
    }
}
