/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.redis;

import cn.herodotus.engine.cache.core.constants.CacheConstants;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: SimpleRedisCache的基础实现 </p>
 *
 * 增加这一层是为了方便扩展，比如说支持JustAuth
 *
 * @author : gengwei.zheng
 * @date : 2021/5/21 23:45
 */
public abstract class AbstractStringRedisCache implements StringRedisCache {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);
    private static final String DEFAULT_PREFIX = CacheConstants.CACHE_SIMPLE_BASE_PREFIX + "default:";

    private StringRedisTemplate stringRedisTemplate;
    private String prefix = DEFAULT_PREFIX;
    private long defaultTimeout = DEFAULT_TIMEOUT.toMillis();

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDefaultTimeout(long defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public void cache(String key, String value) {
        this.cache(key, value, this.defaultTimeout);
    }

    /**
     * 存入缓存
     *
     * @param key     缓存key
     * @param value   缓存内容
     * @param timeout 指定缓存过期时间（毫秒）
     */
    @Override
    public void cache(String key, String value, long timeout) {
        this.stringRedisTemplate.opsForValue().set(this.prefix + key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取缓存内容
     *
     * @param key 缓存key
     * @return 缓存内容
     */
    @Override
    public String get(String key) {
        return this.stringRedisTemplate.opsForValue().get(this.prefix + key);
    }

    /**
     * 是否存在key，如果对应key的value值已过期，也返回false
     *
     * @param key 缓存key
     * @return true：存在key，并且value没过期；false：key不存在或者已过期
     */
    @Override
    public boolean containsKey(String key) {
        Long expire = this.stringRedisTemplate.getExpire(this.prefix + key, TimeUnit.MILLISECONDS);
        if (expire == null) {
            expire = 0L;
        }
        return expire > 0;
    }

    @Override
    public boolean delete(String key) {
        if (containsKey(key)) {
            return this.stringRedisTemplate.delete(key);
        }
        return true;
    }
}
