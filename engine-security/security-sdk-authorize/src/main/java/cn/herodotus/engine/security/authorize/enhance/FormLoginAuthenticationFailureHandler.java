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

package cn.herodotus.engine.security.authorize.enhance;

import cn.herodotus.engine.security.authorize.exception.OauthCaptchaArgumentIllegalException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaHasExpiredException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaIsEmptyException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaMismatchException;
import cn.herodotus.engine.security.core.properties.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> Description : 表单登录失败处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/1/26 18:08
 */
public class FormLoginAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(FormLoginAuthenticationFailureHandler.class);

    public static final String ERROR_MESSAGE_KEY = "SPRING_SECURITY_LAST_EXCEPTION_CUSTOM_MESSAGE";

    private Map<String, String> exceptionDictionary;

    public FormLoginAuthenticationFailureHandler(SecurityProperties securityProperties) {
        this.initExceptionMappings(securityProperties.getLogin().getFailureUrl());
        this.initExceptionDictionary();
    }

    private void initExceptionMappings(String failureUrl) {
        Map<String, String> exceptionMappings = new HashMap<>(8);
        exceptionMappings.put(UsernameNotFoundException.class.getName(), failureUrl);
        exceptionMappings.put(DisabledException.class.getName(), failureUrl);
        exceptionMappings.put(AccountExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(CredentialsExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(BadCredentialsException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaArgumentIllegalException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaHasExpiredException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaMismatchException.class.getName(), failureUrl);
        exceptionMappings.put(OauthCaptchaIsEmptyException.class.getName(), failureUrl);
        this.setExceptionMappings(exceptionMappings);
    }

    private void initExceptionDictionary() {
        this.exceptionDictionary = new HashMap<>(8);
        exceptionDictionary.put("UsernameNotFoundException", "用户名/密码无效");
        exceptionDictionary.put("DisabledException", "用户已被禁用");
        exceptionDictionary.put("AccountExpiredException", "账号已过期");
        exceptionDictionary.put("CredentialsExpiredException", "凭证已过期");
        exceptionDictionary.put("BadCredentialsException", "用户名/密码无效");
        exceptionDictionary.put("OauthCaptchaArgumentIllegalException", "请输入验证码！");
        exceptionDictionary.put("OauthCaptchaHasExpiredException", "验证码已过期！请刷新重试");
        exceptionDictionary.put("OauthCaptchaMismatchException", "验证码输入错误！");
        exceptionDictionary.put("OauthCaptchaIsEmptyException", "请输入验证码！");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        String errorMessage = "请刷新重试！";

        String exceptionName = e.getClass().getSimpleName();
        if (StringUtils.isNotEmpty(exceptionName)) {
            if (exceptionDictionary.containsKey(exceptionName)) {
                errorMessage = exceptionDictionary.get(exceptionName);
            } else {
                log.warn("[Herodotus] |- Form Login Authentication Failur eHandler,  Can not find the exception name [{}] in dictionary, please do optimize ", exceptionName);
            }
        }

        if (this.isUseForward()) {
            httpServletRequest.setAttribute(ERROR_MESSAGE_KEY, errorMessage);
        } else {
            HttpSession session = httpServletRequest.getSession(false);
            if (session != null || this.isAllowSessionCreation()) {
                httpServletRequest.getSession().setAttribute(ERROR_MESSAGE_KEY, errorMessage);
            }
        }

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }
}
