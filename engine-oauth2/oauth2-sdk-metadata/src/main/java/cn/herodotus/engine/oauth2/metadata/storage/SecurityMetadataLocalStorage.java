/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oauth2.metadata.storage;

import cn.herodotus.engine.oauth2.metadata.constants.SecurityMetadataConstants;
import cn.herodotus.engine.oauth2.metadata.matcher.HerodotusRequestMatcher;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>Description: SecurityMetadata 本地存储 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/30 15:05
 */
public class SecurityMetadataLocalStorage {

    private static final Logger log = LoggerFactory.getLogger(SecurityMetadataLocalStorage.class);

    /**
     * 全部 ConfigAttributes 缓存。独立一个缓存，方便获取，减少重复读取。
     * 主要由 {@link FilterInvocationSecurityMetadataSource#getAllConfigAttributes()} 使用
     */
    @CreateCache(name = SecurityMetadataConstants.CACHE_NAME_SECURITY_METADATA_ATTRIBUTES, cacheType = CacheType.LOCAL)
    private Cache<String, Collection<ConfigAttribute>> allConfigAttributes;

    /**
     * attribute 索引辅助缓存，用于帮助 allConfigAttributes去重
     */
    @CreateCache(name = SecurityMetadataConstants.CACHE_NAME_SECURITY_METADATA_INDEXES, cacheType = CacheType.LOCAL)
    private Cache<String, Set<String>> indexes;

    /**
     * 模式匹配权限缓存。主要存储 包含 "*"、"?" 和 "{"、"}" 等特殊字符的路径权限。
     * 该种权限，需要通过遍历，利用 AntPathRequestMatcher 机制进行匹配
     */
    @CreateCache(name = SecurityMetadataConstants.CACHE_NAME_SECURITY_METADATA_COMPATIBLE, cacheType = CacheType.LOCAL)
    private Cache<String, LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>>> compatible;

    /**
     * 直接索引权限缓存，主要存储全路径权限
     * 该种权限，直接通过 Map Key 进行获取
     */
    @CreateCache(name = SecurityMetadataConstants.CACHE_NAME_SECURITY_METADATA_INDEXABLE, cacheType = CacheType.LOCAL)
    private Cache<HerodotusRequestMatcher, Collection<ConfigAttribute>> indexable;

    private static final String KEY_ALL_CONFIG_ATTRIBUTES = "ALL_CONFIG_ATTRIBUTES";
    private static final String KEY_COMPATIBLE = "COMPATIBLE";
    private static final String KEY_INDEXES = "indexes";

    /**
     * 从 allConfigAttributes 缓存中读取数据
     *
     * @return @return 返回全部的权限
     */
    private Collection<ConfigAttribute> readFromAllConfigAttributes() {
        Collection<ConfigAttribute> configAttributes = this.allConfigAttributes.get(KEY_ALL_CONFIG_ATTRIBUTES);
        if (CollectionUtils.isNotEmpty(configAttributes)) {
            return configAttributes;
        }
        return new LinkedHashSet<>();
    }

    /**
     * 写入 allConfigAttributes 缓存
     *
     * @param configAttributes 权限配置属性对象集合
     */
    private void writeToAllConfigAttributes(Collection<ConfigAttribute> configAttributes) {
        this.allConfigAttributes.put(KEY_ALL_CONFIG_ATTRIBUTES, configAttributes);
    }

    /**
     * 从 indexes 缓存中读取数据
     *
     * @return @return 返回全部的权限
     */
    private Set<String> readFromIndexes() {
        Set<String> indexes = this.indexes.get(KEY_INDEXES);
        if (CollectionUtils.isNotEmpty(indexes)) {
            return indexes;
        }
        return new LinkedHashSet<>();
    }

    /**
     * 写入 indexes 缓存
     *
     * @param indexes 权限配置属性对象集合
     */
    private void writeToIndexes(Set<String> indexes) {
        this.indexes.put(KEY_INDEXES, indexes);
    }

    /**
     * 从 compatible 缓存中读取数据。
     *
     * @return 需要进行模式匹配的权限数据
     */
    private LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> readFromCompatible() {
        LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> compatible = this.compatible.get(KEY_COMPATIBLE);
        if (MapUtils.isNotEmpty(compatible)) {
            return compatible;
        }
        return new LinkedHashMap<>();

    }

    /**
     * 写入 compatible 缓存
     *
     * @param compatible 请求路径和权限配置属性映射Map
     */
    private void writeToCompatible(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> compatible) {
        this.compatible.put(KEY_COMPATIBLE, compatible);
    }

    /**
     * 从 indexable 缓存中读取数据
     *
     * @param herodotusRequestMatcher 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequestMatcher}
     * @return 权限配置属性对象集合
     */
    private Collection<ConfigAttribute> readFromIndexable(HerodotusRequestMatcher herodotusRequestMatcher) {
        Collection<ConfigAttribute> indexable = this.indexable.get(herodotusRequestMatcher);
        if (CollectionUtils.isNotEmpty(indexable)) {
            return indexable;
        }
        return new LinkedHashSet<>();
    }

    /**
     * 写入 indexable 缓存
     *
     * @param herodotusRequestMatcher 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequestMatcher}
     * @param configAttributes        权限配置属性对象集合
     */
    private void writeToIndexable(HerodotusRequestMatcher herodotusRequestMatcher, Collection<ConfigAttribute> configAttributes) {
        this.indexable.put(herodotusRequestMatcher, configAttributes);
    }

    /**
     * 从 allConfigAttributes 缓存中获取全部{@link ConfigAttribute}
     *
     * @return 缓存数据 {@link FilterInvocationSecurityMetadataSource#getAllConfigAttributes()}
     */
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return readFromAllConfigAttributes();
    }

    /**
     * 根据请求的 url 和 method 获取权限对象
     *
     * @param url    请求 URL
     * @param method 请求 method
     * @return 与请求url 和 method 匹配的权限数据，或者是空集合
     */
    public Collection<ConfigAttribute> getConfigAttribute(String url, String method) {
        HerodotusRequestMatcher herodotusRequestMatcher = new HerodotusRequestMatcher(url, method);
        return readFromIndexable(herodotusRequestMatcher);
    }

    /**
     * 将权限保存至 allConfigAttributes 缓存中
     *
     * @param securityMetadata {@link ConfigAttribute}
     */
    private void appendToAttributes(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata) {
        Collection<ConfigAttribute> allConfigAttributes = readFromAllConfigAttributes();
        Set<String> indexes = readFromIndexes();

        securityMetadata.forEach((key, value) -> value.forEach(attribute -> {
            String index = attribute.getAttribute();
            if (!indexes.contains(index)) {
                indexes.add(index);
                allConfigAttributes.add(attribute);
            }
        }));

        writeToIndexes(indexes);
        writeToAllConfigAttributes(allConfigAttributes);
    }

    /**
     * 从 compatible 缓存中获取全部不需要路径匹配的（包含*号的url）请求权限映射Map
     *
     * @return 如果缓存中存在，则返回请求权限映射Map集合，如果不存在则返回一个空的{@link LinkedHashMap}
     */
    public LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> getCompatible() {
        return readFromCompatible();
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param herodotusRequestMatcher 请求匹配对象 {@link HerodotusRequestMatcher}
     * @param configAttributes        权限配置 {@link ConfigAttribute}
     */
    private void appendToCompatible(HerodotusRequestMatcher herodotusRequestMatcher, Collection<ConfigAttribute> configAttributes) {
        LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> compatible = this.getCompatible();
//        compatible.merge(requestMatcher, configAttributes, (oldConfigAttributes, newConfigAttributes) -> {
//            newConfigAttributes.addAll(oldConfigAttributes);
//            return newConfigAttributes;
//        });

        // 使用merge会让整个功能的设计更加复杂，暂时改为直接覆盖已有数据，后续视情况再做变更。
        compatible.put(herodotusRequestMatcher, configAttributes);
        log.trace("[Herodotus] |- Append [{}] to Compatible cache, current size is [{}]", herodotusRequestMatcher, compatible.size());
        writeToCompatible(compatible);
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param securityMetadata 请求权限映射Map
     */
    private void appendToCompatible(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata) {
        securityMetadata.forEach(this::appendToCompatible);
    }

    /**
     * 向 indexable 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link HerodotusRequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link HerodotusRequestMatcher}为Key的数据，那么合并数据
     *
     * @param herodotusRequestMatcher 请求匹配对象 {@link HerodotusRequestMatcher}
     * @param configAttributes        权限配置 {@link ConfigAttribute}
     */
    private void appendToIndexable(HerodotusRequestMatcher herodotusRequestMatcher, Collection<ConfigAttribute> configAttributes) {
        log.debug("[Herodotus] |- Append [{}] to Indexable cache, current size is [{}]", herodotusRequestMatcher, configAttributes.size());
        writeToIndexable(herodotusRequestMatcher, configAttributes);
    }

    /**
     * 向 indexable 缓存中添加需要路径匹配的（不包含*号的url）请求权限映射Map。
     * <p>
     * 如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据
     * 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param securityMetadata 请求权限映射Map
     */
    private void appendToIndexable(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata) {
        securityMetadata.forEach(this::appendToIndexable);
    }

    /**
     * 将权限数据添加至本地存储
     *
     * @param securityMetadata 权限数据
     * @param isIndexable      true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata, boolean isIndexable) {
        if (MapUtils.isNotEmpty(securityMetadata)) {
            if (isIndexable) {
                appendToIndexable(securityMetadata);
                appendToAttributes(securityMetadata);
            } else {
                appendToCompatible(securityMetadata);
                appendToAttributes(securityMetadata);
            }
        }
    }


    /**
     * 将权限数据添加至本地存储，存储之前进行规则冲突校验
     *
     * @param matchers         校验资源
     * @param securityMetadata 权限数据
     * @param isIndexable      true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> matchers, LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata, boolean isIndexable) {
        LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> result = new LinkedHashMap<>();
        if (MapUtils.isNotEmpty(matchers) && MapUtils.isNotEmpty(securityMetadata)){
            result = checkConflict(matchers, securityMetadata);
        }

        addToStorage(result, isIndexable);
    }

    /**
     * 规则冲突校验
     * <p>
     * 如存在规则冲突，则保留可支持最大化范围规则，冲突的其它规则则不保存
     *
     * @param matchers         校验资源
     * @param securityMetadata 权限数据
     * @return 去除冲突的权限数据
     */
    private LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> checkConflict(LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> matchers, LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> securityMetadata) {

        LinkedHashMap<HerodotusRequestMatcher, Collection<ConfigAttribute>> result = new LinkedHashMap<>(securityMetadata);

        for (HerodotusRequestMatcher matcher : matchers.keySet()) {
            for (HerodotusRequestMatcher item : securityMetadata.keySet()) {
                if (matcher.matches(item)) {
                    result.remove(item);
                    log.debug("[Herodotus] |- Pattern [{}] is conflict with [{}], so remove it.", item.getPattern(), matcher.getPattern());
                }
            }
        }

        return result;
    }
}
