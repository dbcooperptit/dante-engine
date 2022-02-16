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

package cn.herodotus.engine.security.extend.enhance;

import cn.herodotus.engine.security.core.definition.domain.HerodotusUserDetails;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义用户转换器
 * 用于token解析
 *
 * @author gengwei.zheng
 */
public class HerodotusUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(UserAuthenticationConverter.USERNAME, authentication.getPrincipal());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(UserAuthenticationConverter.AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * 转换为自定义信息
     *
     * @param map
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object converterUserDetails(Map<String, ?> map) {
        Map<String, Object> params = new HashMap<>(8);
        for (String key : map.keySet()) {
            if (UserAuthenticationConverter.USERNAME.equals(key)) {
                if (map.get(key) instanceof Map) {
                    params.putAll((Map) map.get(key));
                } else if (map.get(key) instanceof HerodotusUserDetails) {
                    return map.get(key);
                } else {
                    params.put(key, map.get(key));
                }
            } else {
                params.put(key, map.get(key));
            }
        }
        HerodotusUserDetails herodotusUserDetails = BeanUtil.mapToBean(params, HerodotusUserDetails.class, true);
        if (params.get(UserAuthenticationConverter.USERNAME) != null) {
            herodotusUserDetails.setUsername(params.get(UserAuthenticationConverter.USERNAME).toString());
        }

        herodotusUserDetails.setAuthorities(getAuthorities(map));
        return herodotusUserDetails;
    }

    /**
     * 读取认证信息
     *
     * @param map 认证信息存储对象
     * @return OAuth2 认证对象
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(UserAuthenticationConverter.USERNAME)) {
            Object principal = converterUserDetails(map);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            if (principal != null) {
                HerodotusUserDetails user = (HerodotusUserDetails) principal;
                authorities = user.getAuthorities();
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    /**
     * 获取权限
     *
     * @param map
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(UserAuthenticationConverter.AUTHORITIES)) {
            return AuthorityUtils.NO_AUTHORITIES;
        }
        Object authorities = map.get(UserAuthenticationConverter.AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
