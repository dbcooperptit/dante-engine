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

package cn.herodotus.engine.rest.crypto.configuration;

import cn.herodotus.engine.rest.crypto.enhance.*;
import cn.herodotus.engine.rest.crypto.stamp.SecretKeyStampManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>Description: TODO </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/14 21:11
 */
public class CryptoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CryptoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public SecretKeyStampManager secretKeyStampManager() {
        SecretKeyStampManager secretKeyStampManager = new SecretKeyStampManager();
        log.trace("[Herodotus] |- Bean [Interface Security Stamp Manager] Auto Configure.");
        return secretKeyStampManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SecretKeyStampManager.class)
    public InterfaceCryptoProcessor interfaceCryptoProcessor(SecretKeyStampManager secretKeyStampManager) {
        InterfaceCryptoProcessor interfaceCryptoProcessor = new InterfaceCryptoProcessor();
        interfaceCryptoProcessor.setSecretKeyStampManager(secretKeyStampManager);
        log.trace("[Herodotus] |- Bean [Interface Crypto Processor] Auto Configure.");
        return interfaceCryptoProcessor;
    }

    @Bean
    @ConditionalOnClass(InterfaceCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestBodyAdvice decryptRequestBodyAdvice(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        DecryptRequestBodyAdvice decryptRequestBodyAdvice = new DecryptRequestBodyAdvice();
        decryptRequestBodyAdvice.setInterfaceCryptoProcessor(interfaceCryptoProcessor);
        log.trace("[Herodotus] |- Bean [Decrypt Request Body Advice] Auto Configure.");
        return decryptRequestBodyAdvice;
    }

    @Bean
    @ConditionalOnClass(InterfaceCryptoProcessor.class)
    @ConditionalOnMissingBean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        EncryptResponseBodyAdvice encryptResponseBodyAdvice = new EncryptResponseBodyAdvice();
        encryptResponseBodyAdvice.setInterfaceCryptoProcessor(interfaceCryptoProcessor);
        log.trace("[Herodotus] |- Bean [Encrypt Response Body Advice] Auto Configure.");
        return encryptResponseBodyAdvice;
    }

    @Bean
    @ConditionalOnClass(InterfaceCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestParamMapResolver decryptRequestParamStringResolver(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        DecryptRequestParamMapResolver decryptRequestParamMapResolver = new DecryptRequestParamMapResolver();
        decryptRequestParamMapResolver.setInterfaceCryptoProcessor(interfaceCryptoProcessor);
        log.trace("[Herodotus] |- Bean [Decrypt Request Param Map Resolver] Auto Configure.");
        return decryptRequestParamMapResolver;
    }

    @Bean
    @ConditionalOnClass(InterfaceCryptoProcessor.class)
    @ConditionalOnMissingBean
    public DecryptRequestParamResolver decryptRequestParamResolver(InterfaceCryptoProcessor interfaceCryptoProcessor) {
        DecryptRequestParamResolver decryptRequestParamResolver = new DecryptRequestParamResolver();
        decryptRequestParamResolver.setInterfaceCryptoProcessor(interfaceCryptoProcessor);
        log.trace("[Herodotus] |- Bean [Decrypt Request Param Resolver] Auto Configure.");
        return decryptRequestParamResolver;
    }
}
