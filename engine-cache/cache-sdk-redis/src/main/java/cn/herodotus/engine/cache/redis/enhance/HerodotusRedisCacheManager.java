/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.redis.enhance;

import cn.herodotus.engine.cache.core.properties.CacheProperties;
import cn.herodotus.engine.cache.core.properties.Expire;
import cn.herodotus.engine.definition.core.constants.SymbolConstants;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * <p>Description: 扩展的RedisCacheManager </p>
 * <p>
 * 用于支持 Redis 缓存可以针对实体进行单独的过期时间设定
 *
 * @author : gengwei.zheng
 * @date : 2021/10/25 20:49
 */
public class HerodotusRedisCacheManager extends RedisCacheManager {

    private static final Logger log = LoggerFactory.getLogger(HerodotusRedisCacheManager.class);

    private CacheProperties cacheProperties;

    public HerodotusRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, CacheProperties cacheProperties) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheProperties = cacheProperties;
    }

    public HerodotusRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, CacheProperties cacheProperties, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheProperties = cacheProperties;
    }

    public HerodotusRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, CacheProperties cacheProperties, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheProperties = cacheProperties;
    }

    public HerodotusRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, CacheProperties cacheProperties) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheProperties = cacheProperties;
    }

    public HerodotusRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        Map<String, Expire> expires = cacheProperties.getExpires();
        if (MapUtils.isNotEmpty(expires)) {
            String key = StringUtils.replace(name, SymbolConstants.COLON, cacheProperties.getSeparator());
            if (expires.containsKey(key)) {
                Expire expire = expires.get(key);
                log.debug("[Herodotus] |- CACHE - Redis cache [{}] is setted to use CUSTEM exprie.", name);
                cacheConfig = cacheConfig.entryTtl(expire.getTtl());
            }
        }

        return super.createRedisCache(name, cacheConfig);
    }
}
