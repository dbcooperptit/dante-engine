/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.cache.jetcache.enhance;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.support.CacheContext;
import com.alicp.jetcache.anno.support.CachedAnnoConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 自定义 Jetcache 非注解创建 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/26 15:23
 */
public class JetCacheBuilder {

    private static final Logger log = LoggerFactory.getLogger(JetCacheBuilder.class);

    private static final String UNDEFINED = "$$undefined$$";
    private static final String DEFAULT_AREA = "default";
    private static final int DEFAULT_EXPIRE = -2147483648;

    private final SpringConfigProvider springConfigProvider;

    public JetCacheBuilder(SpringConfigProvider springConfigProvider) {
        this.springConfigProvider = springConfigProvider;
    }

    public CacheContext getCacheContext() {
        return springConfigProvider.getCacheContext();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <K, V> Cache<K, V> create(String name, CachedAnnoConfig cachedAnnoConfig) {
        Cache cache = this.getCacheContext().__createOrGetCache(cachedAnnoConfig, cachedAnnoConfig.getArea(), name);
        log.debug("[Herodotus] |- JetCacheBuilder create cache [{}].", cachedAnnoConfig.getName());
        return cache;
    }

    public <K, V> Cache<K, V> create(String name, String area, CacheType cacheType, int expire, int localexpire, TimeUnit timeUnit, int localLimit) {
        CachedAnnoConfig cachedAnnoConfig = new CachedAnnoConfig();
        cachedAnnoConfig.setArea(area);
        cachedAnnoConfig.setName(name);
        cachedAnnoConfig.setTimeUnit(timeUnit);
        cachedAnnoConfig.setExpire(expire);
        cachedAnnoConfig.setLocalExpire(localexpire);
        cachedAnnoConfig.setCacheType(cacheType);
        cachedAnnoConfig.setLocalLimit(localLimit);
        cachedAnnoConfig.setSerialPolicy(UNDEFINED);
        cachedAnnoConfig.setKeyConvertor(UNDEFINED);
        return create(name, cachedAnnoConfig);
    }

    public <K, V> Cache<K, V> create(String name, String area, CacheType cacheType, int expire, int localexpire, TimeUnit timeUnit) {
        return create(name, area, cacheType, expire, localexpire, timeUnit, DEFAULT_EXPIRE);
    }

    public <K, V> Cache<K, V> create(String name, String area, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(name, area, cacheType, expire, DEFAULT_EXPIRE, timeUnit);
    }

    public <K, V> Cache<K, V> create(String name, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(name, DEFAULT_AREA, cacheType, expire, timeUnit);
    }

    public <K, V> Cache<K, V> create(String name, CacheType cacheType, int expire) {
        return create(name, cacheType, expire, TimeUnit.SECONDS);
    }

    public <K, V> Cache<K, V> create(String name, int expire) {
        return create(name, CacheType.BOTH, expire);
    }

    public <K, V> Cache<K, V> create(String name) {
        return create(name, DEFAULT_EXPIRE);
    }
}
