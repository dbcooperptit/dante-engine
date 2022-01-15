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

package cn.herodotus.engine.assistant.core.constants;

import cn.herodotus.engine.assistant.core.constants.SymbolConstants;

/**
 * <p>Description: 认证授权等安全相关常量值 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/18 8:37
 */
public interface SecurityConstants {

    String ENDPOINT_OAUTH_TOKEN = "/oauth/token";

    String ENDPOINT_OAUTH_AUTHORIZE = "/oauth/authorize";

    String ENDPOINT_OAUTH_CHECK_TOKEN = "/oauth/check_token";

    String ENDPOINT_OAUTH_IDENTITY_PROFILE = "/identity/profile";

    /**
     * Oauth2 四种模式类型
     */
    String AUTHORIZATION_CODE = "authorization_code";
    String IMPLICIT = "implicit";
    String PASSWORD = "password";
    String CLIENT_CREDENTIALS = "client_credentials";
    String REFRESH_TOKEN = "refresh_token";
    String SOCIAL_AUTHENTICATION = "social_authentication";

    String OPEN_API_SECURITY_SCHEME_BEARER_NAME = "HERODOTUS_AUTH";

    /**
     * 访问 /oauth/authorize地址的两种 response_type类型值
     */

    String TOKEN = "token";

    String BEARER_TYPE = "Bearer";
    String BEARER_TOKEN = BEARER_TYPE + SymbolConstants.SPACE;

    String BASIC_TYPE = "Basic";
    String BASIC_TOKEN = BASIC_TYPE + SymbolConstants.SPACE;

    String ROLE_PREFIX = "ROLE_";
    String AUTHORITY_PREFIX = "OP_";
    String AUTHORITY_PREFIX_API = "API_";

    String OPEN_ID = "openid";
    String LICENSE = "license";

    String CODE = "code";
}
