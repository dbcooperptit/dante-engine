/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.caffeine.enhance;

import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <p>Description: Caffeine 缓存永不过期时间配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/30 15:07
 */
public class CaffeineNeverExpire<K, V> implements Expiry<K, V> {

    @Override
    public long expireAfterCreate(@NonNull K key, @NonNull V value, long currentTime) {
        return Long.MAX_VALUE;
    }

    @Override
    public long expireAfterUpdate(@NonNull K key, @NonNull V value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }

    @Override
    public long expireAfterRead(@NonNull K key, @NonNull V value, long currentTime, @NonNegative long currentDuration) {
        return currentDuration;
    }
}
