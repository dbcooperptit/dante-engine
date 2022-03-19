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
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.oauth2.core.enums;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import com.google.common.collect.ImmutableMap;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 客户端身份验证模式 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/17 14:49
 */
public enum AuthenticationMethod {

    /**
     * enum
     */
    CLIENT_SECRET_BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue(), "基于Client Secret的Basic验证模式"),
    CLIENT_SECRET_POST(ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue(), "基于Client Secret的Post验证模式"),
    CLIENT_SECRET_JWT(ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue(), "基于Client Secret的JWT验证模式"),
    PRIVATE_KEY_JWT(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue(), "基于私钥的JWT验证模式"),
    NONE(ClientAuthenticationMethod.NONE.getValue(), "不设置任何模式");

    private final String method;
    private final String description;

    private static final Map<Integer, AuthenticationMethod> indexMap = new HashMap<>();
    private static final List<Map<String, Object>> toJsonStruct = new ArrayList<>();

    static {
        for (AuthenticationMethod grantType : AuthenticationMethod.values()) {
            indexMap.put(grantType.ordinal(), grantType);
            toJsonStruct.add(grantType.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", grantType.getMethod())
                            .put("key", grantType.name())
                            .put("text", grantType.getDescription())
                            .put("index", grantType.ordinal())
                            .build());
        }
    }

    AuthenticationMethod(String method, String description) {
        this.method = method;
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }

    public static AuthenticationMethod getAuthenticationMethod(Integer grant) {
        return indexMap.get(grant);
    }

    public static List<Map<String, Object>> getToJsonStruct() {
        return toJsonStruct;
    }
}
