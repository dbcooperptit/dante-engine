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

package cn.herodotus.engine.data.jpa.hibernate.cache.spi;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * <p>Description: 自定义Hibernate二级缓存DomainDataStorageAccess </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/12 22:06
 */
public class HerodotusDomainDataStorageAccess implements DomainDataStorageAccess {

    private static final Logger log = LoggerFactory.getLogger(HerodotusDomainDataStorageAccess.class);

    private final Cache cache;

    public HerodotusDomainDataStorageAccess(Cache cache) {
        this.cache = cache;
    }

    private Object get(Object key) {
        Cache.ValueWrapper value = cache.get(key);

        if (ObjectUtils.isNotEmpty(value)) {
            return value.get();
        }
        return null;
    }

    @Override
    public boolean contains(Object key) {
        Object value = this.get(key);
        log.trace("[Herodotus] |- CACHE - SPI check is key : [{}] exist.", key);
        return ObjectUtils.isNotEmpty(value);
    }

    @Override
    public Object getFromCache(Object key, SharedSessionContractImplementor session) {
        Object value = this.get(key);
        log.trace("[Herodotus] |- CACHE - SPI get from cache key is : [{}], value is : [{}]", key, value);
        return value;
    }

    @Override
    public void putIntoCache(Object key, Object value, SharedSessionContractImplementor session) {
        log.trace("[Herodotus] |- CACHE - SPI put into cache key is : [{}], value is : [{}]", key, value);
        cache.put(key, value);
    }

    @Override
    public void removeFromCache(Object key, SharedSessionContractImplementor session) {
        log.trace("[Herodotus] |- CACHE - SPI remove from cache key is : [{}]", key);
        cache.evict(key);
    }

    @Override
    public void evictData(Object key) {
        log.trace("[Herodotus] |- CACHE - SPI evict key : [{}] from cache.", key);
        cache.evict(key);
    }

    @Override
    public void clearCache(SharedSessionContractImplementor session) {
        this.evictData();
    }

    @Override
    public void evictData() {
        log.trace("[Herodotus] |- CACHE - SPI clear all cache data.");
        cache.clear();
    }

    @Override
    public void release() {
        log.trace("[Herodotus] |- CACHE - SPI cache release.");
        cache.invalidate();
    }
}
