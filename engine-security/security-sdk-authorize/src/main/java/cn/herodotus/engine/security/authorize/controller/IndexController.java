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

package cn.herodotus.engine.security.authorize.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.security.core.definition.service.HerodotusClientDetailsService;
import cn.herodotus.engine.security.core.exception.SecurityGlobalExceptionHandler;
import cn.herodotus.engine.security.core.properties.SecurityProperties;
import cn.herodotus.engine.security.core.utils.SymmetricUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author gengwei.zheng
 */
@Controller
@SessionAttributes("authorizationRequest")
public class IndexController {

    private final Logger log = LoggerFactory.getLogger(IndexController.class);

    private static final String ERROR_MESSAGE_KEY = "SPRING_SECURITY_LAST_EXCEPTION_CUSTOM_MESSAGE";

    @Autowired
    private HerodotusClientDetailsService herodotusClientDetailsService;
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 欢迎页
     *
     * @return 登录地址
     */
    @RequestMapping("/")
    public String welcome() {
        return "/login";
    }

    /**
     * 登录页
     *
     * @return 视图模型 {@link ModelAndView}
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Map<String, Object> model, HttpServletRequest request) throws DecoderException {

        ModelAndView modelAndView = new ModelAndView("/login");

        HttpSession session = request.getSession();
        Object error = session.getAttribute(ERROR_MESSAGE_KEY);
        if (ObjectUtils.isNotEmpty(error)) {
            modelAndView.addObject("message", error);
        }
        session.removeAttribute(ERROR_MESSAGE_KEY);

        // AES加密key
        modelAndView.addObject("soup_spoon", SymmetricUtils.getEncryptedSymmetricKey());
        // 登录可配置用户名参数
        modelAndView.addObject("vulgar_tycoon", securityProperties.getLogin().getUsernameParameter());
        // 登录可配置密码参数
        modelAndView.addObject("beast", securityProperties.getLogin().getPasswordParameter());
        // 登录可配置验证码参数
        modelAndView.addObject("graphic", securityProperties.getCaptcha().getCaptchaParameter());
        // 登录可配置是否启用验证码参数
        modelAndView.addObject("hide_verification_code", securityProperties.getCaptcha().isClosed());
        // 验证码类别
        modelAndView.addObject("verification_category", securityProperties.getCaptcha().getCategory());

        return modelAndView;
    }

    /**
     * 确认授权页
     *
     * @param request 请求对象
     * @param model   模型对象
     * @return 视图模型 {@link ModelAndView}
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView confirmAccess(Map<String, Object> model, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("/confirm_access");

        if (request.getAttribute("_csrf") != null) {
            modelAndView.addObject("_csrf", request.getAttribute("_csrf"));
        }

        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        modelAndView.addObject("scopes", authorizationRequest.getScope());

        ClientDetails clientDetails = herodotusClientDetailsService.getOauthClientDetails(authorizationRequest.getClientId());
        modelAndView.addObject("app", clientDetails.getAdditionalInformation());

        return modelAndView;
    }

    /**
     * 自定义oauth2错误页
     *
     * @param request 请求对象
     * @return 页面地址
     */
    @RequestMapping("/oauth/error")
    public String handleError(Map<String, Object> model, HttpServletRequest request) {
        Object error = request.getAttribute("error");
        if (error instanceof Exception) {
            Exception exception = (Exception) error;
            Result<String> result = SecurityGlobalExceptionHandler.resolveOauthException(exception, request.getRequestURI());
            model.putAll(result.toModel());
        }
        return "/error";
    }
}
