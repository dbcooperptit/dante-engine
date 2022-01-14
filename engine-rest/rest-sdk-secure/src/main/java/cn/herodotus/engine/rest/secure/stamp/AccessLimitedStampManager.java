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

package cn.herodotus.engine.rest.secure.stamp;

import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.herodotus.engine.rest.core.constants.RestCacheConstants;
import cn.herodotus.engine.rest.secure.properties.StampProperties;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;

/**
 * <p>Description: 防刷签章管理器 </p>
 * <p>
 * 这里使用Long类型作为值的存储类型，是为了解决该Cache 同时可以存储Duration相关的数据
 *
 * @author : gengwei.zheng
 * @date : 2021/8/25 21:43
 */
public class AccessLimitedStampManager extends AbstractStampManager<String, Long> {

    private StampProperties stampProperties;

    public void setStampProperties(StampProperties stampProperties) {
        this.stampProperties = stampProperties;
    }

    @CreateCache(name = RestCacheConstants.CACHE_NAME_TOKEN_ACCESS_LIMITED, cacheType = CacheType.BOTH)
    protected Cache<String, Long> cache;

    @Override
    protected Cache<String, Long> getCache() {
        return this.cache;
    }

    @Override
    public Long nextStamp(String key) {
        return 1L;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(stampProperties.getAccessLimited().getExpire());
    }
}
