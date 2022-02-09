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

import cn.herodotus.engine.security.log.domain.Signin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;

/**
 * <p>Description: 登出成功监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/18 17:58
 */
public class AuthenticationFailureListener extends AbstractAuthenticationListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String message;
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            //提供的凭据是错误的，用户名或者密码错误
            message = "提供的凭据是错误的，用户名或者密码错误";
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            message = "验证通过，但是密码过期";
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            message = "验证过了但是账户被禁用";
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            message = "验证通过了，但是账号已经过期";
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            message = "账户被锁定";
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            message = "配置错误";
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            // 代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            message = "代理不受信任";
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            // 其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            message = "内部发生的异常";
        } else {
            message = "其他未知错误";
        }

        Authentication authentication = event.getAuthentication();
        Signin signin = this.getSignin(authentication);
        signin.setStatus(0);
        signin.setRemark(message);
        signin.setTimestamp(event.getTimestamp());

        log.debug("[Herodotus] |- Current signin user is [{}]", signin);
    }
}
