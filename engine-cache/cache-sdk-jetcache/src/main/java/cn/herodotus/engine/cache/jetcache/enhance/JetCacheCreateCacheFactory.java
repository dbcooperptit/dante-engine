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
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.cache.jetcache.enhance;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.support.CachedAnnoConfig;
import com.alicp.jetcache.anno.support.ConfigProvider;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: JetCache 手动创建Cache 工厂 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/23 10:49
 */
public class JetCacheCreateCacheFactory {

    private static final String UNDEFINED = "$$undefined$$";
    private final ConfigProvider configProvider;

    public JetCacheCreateCacheFactory(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    public <K, V> Cache<K, V> create(String cacheName, int expire, TimeUnit timeUnit) {
        return create(cacheName, CacheType.BOTH, expire, timeUnit);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(BaseConstants.LOWERCASE_DEFAULT, cacheName, cacheType, expire, timeUnit);
    }

    public <K, V> Cache<K, V> create(String cacheName) {
        return create(cacheName, CacheType.BOTH);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType) {
        return create(cacheName, cacheType, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String cacheName, CacheType cacheType, int expire) {
        return create(BaseConstants.LOWERCASE_DEFAULT, cacheName, cacheType, expire);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire) {
        return create(area, cacheName, cacheType, expire, TimeUnit.SECONDS);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit) {
        return create(area, cacheName, cacheType, expire, timeUnit, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire) {
        return create(area, cacheName, cacheType, expire, timeUnit, localExpire, Integer.MIN_VALUE);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire, int localLimit) {
        return create(area, cacheName, cacheType, expire, timeUnit, localExpire, localLimit, UNDEFINED, UNDEFINED);
    }

    public <K, V> Cache<K, V> create(String area, String cacheName, CacheType cacheType, int expire, TimeUnit timeUnit, int localExpire, int localLimit, String serialPolicy, String keyConvertor) {
        CachedAnnoConfig cac = new CachedAnnoConfig();
        cac.setArea(area);
        cac.setName(cacheName);
        cac.setCacheType(cacheType);
        cac.setExpire(expire);
        cac.setTimeUnit(timeUnit);
        cac.setLocalExpire(localExpire);
        cac.setLocalLimit(localLimit);
        cac.setSerialPolicy(serialPolicy);
        cac.setKeyConvertor(keyConvertor);
        return create(cac);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> create(CachedAnnoConfig cac) {
        return configProvider.getCacheContext().__createOrGetCache(cac, cac.getArea(), cac.getName());
    }
}
