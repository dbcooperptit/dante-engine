/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.herodotus.engine.sms.netease.configuration;

import cn.herodotus.engine.sms.core.constants.SmsConstants;
import cn.herodotus.engine.sms.netease.annotation.ConditionalOnNeteaseSmsEnabled;
import cn.herodotus.engine.sms.netease.processor.NeteaseSmsSendHandler;
import cn.herodotus.engine.sms.netease.properties.NeteaseSmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 网易短信发送配置类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/25 15:24
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnNeteaseSmsEnabled
@EnableConfigurationProperties(NeteaseSmsProperties.class)
public class NeteaseSmsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(NeteaseSmsConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Sms Netease] Auto Configure.");
    }

    /**
     * 构造网易云信发送处理
     *
     * @param neteaseSmsProperties 配置对象
     * @return 网易云信发送处理
     */
    @Bean(name = SmsConstants.CHANNEL_NETEASE_CLOUD)
    public NeteaseSmsSendHandler neteaseCloudSmsSendHandler(NeteaseSmsProperties neteaseSmsProperties) {
        NeteaseSmsSendHandler neteaseSmsSendHandler = new NeteaseSmsSendHandler(neteaseSmsProperties);
        log.debug("[Herodotus] |- Bean [Netease Sms Send Handler] Auto Configure.");
        return neteaseSmsSendHandler;
    }
}
