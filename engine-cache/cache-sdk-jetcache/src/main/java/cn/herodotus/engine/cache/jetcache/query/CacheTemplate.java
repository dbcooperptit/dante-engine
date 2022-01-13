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
