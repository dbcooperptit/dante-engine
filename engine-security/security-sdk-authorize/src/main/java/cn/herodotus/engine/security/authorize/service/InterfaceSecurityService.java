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

package cn.herodotus.engine.security.authorize.service;

import cn.herodotus.engine.rest.crypto.domain.SecretKey;
import cn.herodotus.engine.rest.crypto.enhance.InterfaceCryptoProcessor;
import cn.herodotus.engine.rest.crypto.exception.SessionInvalidException;
import cn.herodotus.engine.security.core.definition.service.HerodotusClientDetailsService;
import cn.herodotus.engine.security.core.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

/**
 * <p>Description: 请求加密服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:08
 */
@Service
public class InterfaceSecurityService {

    private static final Logger log = LoggerFactory.getLogger(InterfaceSecurityService.class);

    private static final String PKCS1_BEGIN = "-----BEGIN RSA PUBLIC KEY-----";
    private static final String PKCS1_END = "-----END RSA PUBLIC KEY-----";
    private static final String PKCS8_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String PKCS8_END = "-----END PUBLIC KEY-----";


    @Autowired
    private InterfaceCryptoProcessor interfaceCryptoProcessor;
    @Autowired
    private HerodotusClientDetailsService herodotusClientDetailsService;

    /**
     * 检查终端是否是合法终端
     *
     * @param clientId     OAuth2 终端ID
     * @param clientSecret OAuth2 终端密码
     */
    private ClientDetails validateClient(String clientId, String clientSecret) {
        ClientDetails clientDetails = herodotusClientDetailsService.getOauthClientDetails(clientId);

        boolean isMatch = false;
        if (ObjectUtils.isNotEmpty(clientDetails)) {
            isMatch = SecurityUtils.matches(clientSecret, clientDetails.getClientSecret());
        }

        if (!isMatch) {
            throw new InvalidClientException("Illegal Client Info");
        }

        return clientDetails;
    }

    public SecretKey createSecretKey(String clientId, String clientSecret, String sessionId) {
        // 检测终端是否是有效终端
        ClientDetails clientDetails = this.validateClient(clientId, clientSecret);
        return interfaceCryptoProcessor.createSecretKey(sessionId, clientDetails.getAccessTokenValiditySeconds());
    }

    /**
     * 前端用后端PublicKey加密前端PublicKey后，将该值传递给后端，用于加密 AES KEY
     *
     * @param sessionId          Session 标识
     * @param confidentialBase64 前端用后端PublicKey加密前端PublicKey。前端使用node-rsa加密后的数据是base64编码
     * @return 前端RSA PublicKey 加密后的 AES Key
     */
    public String exchange(String sessionId, String confidentialBase64) {
        try {
            return interfaceCryptoProcessor.exchange(sessionId, confidentialBase64);
        } catch (SessionInvalidException e) {
            throw new InvalidTokenException("Token is expired!");
        }
    }

    public String appendPkcs8PublicKeyPadding(String key) {
        return interfaceCryptoProcessor.convertPublicKeyToPkcs8Padding(key);
    }
}
