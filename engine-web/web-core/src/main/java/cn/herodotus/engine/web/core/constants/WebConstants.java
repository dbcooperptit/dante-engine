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

package cn.herodotus.engine.web.core.constants;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;

/**
 * <p>Description: Web包属性常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/14 17:34
 */
public interface WebConstants extends BaseConstants {

    String PROPERTY_PLATFORM_REST = PROPERTY_HERODOTUS_PLATFORM + ".rest";
    String PROPERTY_PLATFORM_SWAGGER = PROPERTY_HERODOTUS_PLATFORM + ".swagger";

    /* ---------- Herodotus 详细配置属性路径 ---------- */

    String ITEM_PLATFORM_DATA_ACCESS_STRATEGY = PROPERTY_HERODOTUS_PLATFORM + ".data-access-strategy";
    String ITEM_PLATFORM_ARCHITECTURE = PROPERTY_HERODOTUS_PLATFORM + ".architecture";

    /* ---------- Spring 详细配置属性路径 ---------- */

    String ITEM_SPRING_APPLICATION_NAME = "spring.application.name";
    String ITEM_SERVER_PORT = "server.port";


    String ITEM_SWAGGER_ENABLED = PROPERTY_PLATFORM_SWAGGER + PROPERTY_ENABLED;

    /* ---------- 注解属性通用值 ---------- */

    String ANNOTATION_APPLICATION_NAME = ANNOTATION_PREFIX + ITEM_SPRING_APPLICATION_NAME + ANNOTATION_SUFFIX;
    String ANNOTATION_SERVER_PORT = ANNOTATION_PREFIX + ITEM_SERVER_PORT + ANNOTATION_SUFFIX;
}
