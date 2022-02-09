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

package cn.herodotus.engine.security.log.listener;

import cn.herodotus.engine.security.core.definition.domain.HerodotusUserDetails;
import cn.herodotus.engine.security.log.domain.Signin;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Description: 认证监听基础类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/19 16:36
 */
public abstract class AbstractAuthenticationListener {

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> getDetail(Authentication authentication) {
        Object details = authentication.getDetails();

        if (details instanceof LinkedHashMap) {
            return (LinkedHashMap<String, String>) details;
        }
        return null;
    }

    protected HerodotusUserDetails  getUserDetails(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof HerodotusUserDetails) {
            return  (HerodotusUserDetails) principal;
        }
        return null;
    }

    protected UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));
    }

    protected String getIp(HttpServletRequest request) {
        return ServletUtil.getClientIP(request, "");
    }

    protected Signin getSignin(Authentication authentication) {

        Signin signin = new Signin();

        HttpServletRequest request = getRequest();

        if (ObjectUtils.isNotEmpty(request)) {
            UserAgent userAgent = getUserAgent(request);
            if (ObjectUtils.isNotEmpty(userAgent)) {
                //获取ip
                signin.setIp(getIp(request));
                // 获取客户端操作系统
                signin.setOs(userAgent.getOs().getName());
                // 获取客户端浏览器
                signin.setBrowser( userAgent.getBrowser().getName());
                signin.setEngine(userAgent.getEngine().getName());
                signin.setPlatform(userAgent.getPlatform().getName());
                signin.setMobile(userAgent.isMobile());
            }
        }

        HerodotusUserDetails herodotusUserDetails = getUserDetails(authentication);
        if (ObjectUtils.isNotEmpty(herodotusUserDetails)) {
            signin.setUserId(herodotusUserDetails.getUserId());
            signin.setUserName(herodotusUserDetails.getUsername());
        }

        return signin;
    }
}
