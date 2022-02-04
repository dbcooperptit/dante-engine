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

import cn.herodotus.engine.captcha.core.dto.Verification;
import cn.herodotus.engine.captcha.core.exception.CaptchaHasExpiredException;
import cn.herodotus.engine.captcha.core.exception.CaptchaMismatchException;
import cn.herodotus.engine.captcha.core.exception.CaptchaParameterIllegalException;
import cn.herodotus.engine.captcha.core.processor.CaptchaRendererFactory;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaArgumentIllegalException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaHasExpiredException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaIsEmptyException;
import cn.herodotus.engine.security.authorize.exception.OauthCaptchaMismatchException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p> Description : Security Form登录提供者。 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/16 18:01
 */
public class FormLoginAuthenticationProvider extends DaoAuthenticationProvider {

    private CaptchaRendererFactory captchaRendererFactory;

    public void setCaptchaRendererFactory(CaptchaRendererFactory captchaRendererFactory) {
        this.captchaRendererFactory = captchaRendererFactory;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        Object details = authentication.getDetails();

        if (ObjectUtils.isNotEmpty(details) && details instanceof FormLoginWebAuthenticationDetails) {
            FormLoginWebAuthenticationDetails formLoginWebAuthenticationDetails = (FormLoginWebAuthenticationDetails) authentication.getDetails();

            if (!formLoginWebAuthenticationDetails.isClose()) {

                String code = formLoginWebAuthenticationDetails.getCode();
                String category = formLoginWebAuthenticationDetails.getCategory();
                String identity = formLoginWebAuthenticationDetails.getIdentity();

                if (StringUtils.isBlank(code)) {
                    throw new OauthCaptchaIsEmptyException("Captcha is empty.");
                }

                try {
                    Verification verification = new Verification();
                    verification.setCharacters(code);
                    verification.setCategory(category);
                    verification.setIdentity(identity);
                    captchaRendererFactory.verify(verification);
                } catch (CaptchaParameterIllegalException e) {
                    throw new OauthCaptchaArgumentIllegalException("Captcha argument is illegal");
                } catch (CaptchaHasExpiredException e) {
                    throw new OauthCaptchaHasExpiredException("Captcha is expired!");
                } catch (CaptchaMismatchException e) {
                    throw new OauthCaptchaMismatchException("Captcha is mismatch!");
                }
            }
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
