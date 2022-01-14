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

import cn.herodotus.engine.definition.core.domain.Entity;

/**
 * <p>Description: 自定义缓存基础实体 </p>
 * <p>
 * 把早期自定义多级缓存使用到的基础类提取出来，一方面减少对其它代码的影响，另一方面做代码保存备用。
 *
 * @author : gengwei.zheng
 * @date : 2021/12/14 16:11
 */
public abstract class AbstractCacheEntity implements Entity {

    /**
     * 实体主键，作为默认Cache Key
     *
     * @return 实体主键
     */
    public abstract String getId();

    /**
     * 数据表中除主键以外的唯一数据属性
     * <p>
     * 有些实体，只使用一个属性作为Cache Key  不能满足需求。
     * 例如：SysUser，使用UserId和UserName查询都比较频繁，用UserId作为缓存的Key，那么用UserName就无法从缓存读取。
     * 除非用UserId作为缓存Key，缓存一遍实体，用UserName再作为缓存Key，缓存一遍实体。这样增加缓存管理复杂度。
     * <p>
     * 通过getLinkedProperty()，维护一个Map。
     * 通过这个Map，实现指定属性与CacheKey的映射。
     *
     * @return LinkedProperty
     */
    public abstract String getLinkedProperty();
}
