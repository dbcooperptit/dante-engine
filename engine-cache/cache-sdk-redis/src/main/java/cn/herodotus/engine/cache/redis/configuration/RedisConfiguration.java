/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.redis.configuration;

import cn.herodotus.engine.cache.core.properties.CacheProperties;
import cn.herodotus.engine.cache.redis.enhance.HerodotusRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Redis缓存配置
 *
 * @author gengwei.zheng
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisConfiguration {

    private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    @Resource
    private LettuceConnectionFactory lettuceConnectionFactory;
    @Resource
    private CacheProperties cacheProperties;

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new Jackson2JsonRedisSerializer<>(Object.class);
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Cache Redis] Auto Configure.");
    }

    /**
     * 重新配置一个RedisTemplate
     *
     * @return {@link RedisTemplate}
     */
    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());
        redisTemplate.setDefaultSerializer(valueSerializer());
        redisTemplate.afterPropertiesSet();

        log.trace("[Herodotus] |- Bean [Redis Template] Auto Configure.");

        return redisTemplate;
    }

    @Bean(name = "stringRedisTemplate")
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        stringRedisTemplate.afterPropertiesSet();

        log.trace("[Herodotus] |- Bean [String Redis Template] Auto Configure.");

        return stringRedisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(RedisCacheManager.class)
    public RedisCacheManager redisCacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory);

        // 注意：这里 RedisCacheConfiguration 每一个方法调用之后，都会返回一个新的 RedisCacheConfiguration 对象，所以要注意对象的引用关系。
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(cacheProperties.getTtl());

        boolean allowNullValues = cacheProperties.getAllowNullValues();
        if (!allowNullValues) {
            // 注意：这里 RedisCacheConfiguration 每一个方法调用之后，都会返回一个新的 RedisCacheConfiguration 对象，所以要注意对象的引用关系。
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }

        HerodotusRedisCacheManager herodotusRedisCacheManager = new HerodotusRedisCacheManager(redisCacheWriter, redisCacheConfiguration, cacheProperties);
        herodotusRedisCacheManager.setTransactionAware(false);
        herodotusRedisCacheManager.afterPropertiesSet();
        log.trace("[Herodotus] |- Bean [Redis Cache Manager] Auto Configure.");
        return herodotusRedisCacheManager;
    }
}
