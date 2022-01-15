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

package cn.herodotus.engine.assistant.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * <p> Description : 自定义的Bean工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/11 13:36
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 方法说明：map转化为对象
     *
     * @param map 待转换的Map对象
     * @param t   要转换成的对象实体
     * @param <T> 要转换成的对象类型
     * @return 最终转换的对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> t) {
        try {
            T instance = t.newInstance();
            populate(instance, map);
            return instance;
        } catch (Exception e) {
            log.error("[Herodotus] |- BeanUtils mapToBean error！", e);
            return null;
        }
    }
}
