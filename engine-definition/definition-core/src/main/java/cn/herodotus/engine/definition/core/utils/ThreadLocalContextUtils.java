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

package cn.herodotus.engine.definition.core.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> Description : ThreadLocal工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/5 17:22
 */
public class ThreadLocalContextUtils {

    private static final String TENANT_ID = "tenantId";

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        set(TENANT_ID, tenantId);
    }

    public static String getTenantId() {
        return getString(TENANT_ID);
    }

    public static String getString(String attribute) {
        Object object = get(attribute);
        if (ObjectUtils.isNotEmpty(object) && object instanceof String) {
            return (String) object;
        }

        return null;
    }

    /**
     * 获得线程中保存的属性.
     *
     * @param attribute 属性名称
     * @return 属性值
     */
    public static Object get(String attribute) {
        Map<String, Object> map = threadLocal.get();
        if (MapUtils.isEmpty(map)) {
            return null;
        }

        return map.get(attribute);
    }

    public static void set(String attribute, Object value) {
        Map<String, Object> map = threadLocal.get();

        if (MapUtils.isEmpty(map)) {
            map = new ConcurrentHashMap<>(8);
        }
        map.put(attribute, value);
        threadLocal.set(map);
    }

    /**
     * 清除线程中保存的数据
     */
    public static void clear() {
        threadLocal.remove();
    }

}
