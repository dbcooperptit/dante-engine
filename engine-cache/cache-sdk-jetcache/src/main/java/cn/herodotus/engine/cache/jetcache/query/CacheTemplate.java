/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.cache.jetcache.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * <p>Description: 基于JetCache的，通用查询模型 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/6/3 16:09
 */
public class CacheTemplate<D extends AbstractCacheEntity> implements Serializable {

    private final Set<String> queryIndexes = new LinkedHashSet<>();
    private final Map<String, D> domains = new LinkedHashMap<>();

    private final Set<String> deleteIndexes = new LinkedHashSet<>();
    private final Map<String, Set<String>> propertyLinks = new LinkedHashMap<>();

    private boolean propertyLink = false;

    public boolean hasPropertyLink() {
        return propertyLink;
    }

    public Set<String> getQueryIndexes() {
        return queryIndexes;
    }

    public Map<String, D> getDomains() {
        return domains;
    }

    public Set<String> getDeleteIndexes() {
        return deleteIndexes;
    }

    public Map<String, Set<String>> getPropertyLinks() {
        return propertyLinks;
    }

    public void append(D object) {
        queryIndexes.add(object.getId());
        domains.put(object.getId(), object);

        if (StringUtils.isNotBlank(object.getLinkedProperty())) {
            propertyLink = true;
            deleteIndexes.add(object.getLinkedProperty());
            propertyLinks.put(object.getLinkedProperty(), convertToIndexCacheValue(object.getId()));
        }
    }

    public void append(Collection<D> objects) {
        objects.forEach(this::append);
    }

    /**
     * 将单独的值转换为Set工具方法，方便在Index Cache中存储
     *
     * @param value 需要在Index Cache中存储的值
     * @return Index Cache 值
     */
    private Set<String> convertToIndexCacheValue(String value) {
        Assert.notNull(value, "Value must not be null");
        Set<String> values = new LinkedHashSet<>();
        values.add(value);
        return values;
    }
}
