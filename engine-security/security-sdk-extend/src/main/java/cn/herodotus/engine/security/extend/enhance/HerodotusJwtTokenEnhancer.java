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

package cn.herodotus.engine.security.extend.enhance;

import cn.herodotus.engine.assistant.core.constants.SecurityConstants;
import cn.herodotus.engine.security.core.definition.domain.HerodotusUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */

/**
 * <p>Description: 自定义JwtAccessToken增强 </p>
 *
 * jwt生成token 我们可以自己定义jwt里面的内容
 *
 * @author : gengwei.zheng
 * @date : 2020/3/4 16:07
 */
public class HerodotusJwtTokenEnhancer implements TokenEnhancer {

    /**
     * 生成token,添加额外信息。
     * @param accessToken accessToken
     * @param authentication authentication
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOauth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof HerodotusUserDetails) {
            // 设置额外用户信息
            // 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
            HerodotusUserDetails herodotusUserDetails = (HerodotusUserDetails) authentication.getPrincipal();
            final Map<String, Object> additionalInfo = new HashMap<>(8);
            additionalInfo.put(SecurityConstants.OPEN_ID, herodotusUserDetails.getUserId());
            additionalInfo.put(SecurityConstants.LICENSE, "herodotus-cloud");
            defaultOauth2AccessToken.setAdditionalInformation(additionalInfo);
        }

        return defaultOauth2AccessToken;
    }
}
