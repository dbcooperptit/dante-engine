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

package cn.herodotus.engine.cache.core.constants;

/**
 * <p>Description: 缓存相关常量 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/13 21:04
 */
public interface CacheConstants {

    String CACHE_PREFIX = "cache:";

    String CACHE_SIMPLE_BASE_PREFIX = CACHE_PREFIX + "simple:";
    String CACHE_TOKEN_BASE_PREFIX = CACHE_PREFIX + "token:";

    String CACHE_SECURITY_PREFIX = CACHE_PREFIX + "security:";
    String CACHE_SECURITY_METADATA_PREFIX = CACHE_SECURITY_PREFIX + "metadata:";

    String CACHE_NAME_TOKEN_CAPTCHA = CACHE_TOKEN_BASE_PREFIX + "captcha:";

    String CACHE_NAME_TOKEN_VERIFICATION_CODE = CACHE_TOKEN_BASE_PREFIX + "verification:";
    String CACHE_NAME_TOKEN_EASEMOB = CACHE_TOKEN_BASE_PREFIX + "easemob:";
    String CACHE_NAME_TOKEN_JUSTAUTH_STATE = CACHE_TOKEN_BASE_PREFIX + "justauth_state:";
    String CACHE_NAME_TOKEN_PAY = CACHE_TOKEN_BASE_PREFIX + "pay:";

    String CACHE_NAME_CAPTCHA_JIGSAW = CACHE_NAME_TOKEN_CAPTCHA + "jigsaw:";
    String CACHE_NAME_CAPTCHA_WORD_CLICK = CACHE_NAME_TOKEN_CAPTCHA + "word_click:";
    String CACHE_NAME_CAPTCHA_GRAPHIC = CACHE_NAME_TOKEN_CAPTCHA + "graphic:";

    String CACHE_NAME_SECURITY_METADATA_ATTRIBUTES = CACHE_SECURITY_METADATA_PREFIX + "attributes:";
    String CACHE_NAME_SECURITY_METADATA_INDEXABLE = CACHE_SECURITY_METADATA_PREFIX + "indexable:";
    String CACHE_NAME_SECURITY_METADATA_COMPATIBLE = CACHE_SECURITY_METADATA_PREFIX + "compatible:";

    String CACHE_NAME_PAY_ALIPAY = CACHE_NAME_TOKEN_PAY + "alipay:";

    int DEFAULT_UPMS_CACHE_EXPIRE = 86400;
    int DEFAULT_UPMS_LOCAL_LIMIT = 1000;

    String INDEX_CACHE_NAME = "index:";
}
