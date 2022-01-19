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

package cn.herodotus.engine.oss.minio.properties;

import cn.herodotus.engine.oss.core.constants.OssConstants;
import com.google.common.base.MoreObjects;
import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * <p>Description: Minio 配置参数 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 10:31
 */
@ConfigurationProperties(prefix = OssConstants.PROPERTY_OSS_MINIO)
public class MinioProperties {

    /**
     * Minio Server URL
     */
    private String endpoint;

    /**
     * Minio Server accessKey
     */
    private String accessKey;

    /**
     * Minio Server secretKey
     */
    private String secretKey;

    private String bucketNamePrefix;

    /**
     * 文件名中时间标识内容格式。
     */
    private String timestampFormat = "yyyy-MM-dd";

    private Pool pool = new Pool();

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketNamePrefix() {
        return bucketNamePrefix;
    }

    public void setBucketNamePrefix(String bucketNamePrefix) {
        this.bucketNamePrefix = bucketNamePrefix;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class Pool {

        /**
         * 最大总数
         */
        private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;

        /**
         * 最大空闲数
         */
        private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

        /**
         * 最小空闲数
         */
        private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

        /**
         * 最大等待时间，支持 Duration 表达式
         */
        private Duration maxWait = Duration.ofMillis(BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS);
        private boolean blockWhenExhausted;

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public Duration getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public boolean isBlockWhenExhausted() {
            return blockWhenExhausted;
        }

        public void setBlockWhenExhausted(boolean blockWhenExhausted) {
            this.blockWhenExhausted = blockWhenExhausted;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("maxTotal", maxTotal)
                    .add("maxIdle", maxIdle)
                    .add("minIdle", minIdle)
                    .add("maxWaitMillis", maxWait)
                    .add("blockWhenExhausted", blockWhenExhausted)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("endpoint", endpoint)
                .add("accessKey", accessKey)
                .add("secretKey", secretKey)
                .add("bucketNamePrefix", bucketNamePrefix)
                .add("timestampFormat", timestampFormat)
                .add("pool", pool)
                .toString();
    }
}
