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
package cn.herodotus.engine.sms.chinamobile.processor;

import cn.herodotus.engine.sms.chinamobile.domain.ChinaMobileSmsRequest;
import cn.herodotus.engine.sms.chinamobile.properties.ChinaMobileSmsProperties;
import cn.herodotus.engine.sms.core.definition.AbstractSmsSendHandler;
import cn.herodotus.engine.sms.core.domain.Template;
import cn.herodotus.engine.sms.core.enums.SmsSupplier;
import cn.herodotus.engine.sms.core.exception.ParameterOrdersInvalidException;
import cn.herodotus.engine.sms.core.exception.TemplateIdInvalidException;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Description: 移动云发送处理 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/5/25 14:57
 */
public class ChinaMobileSmsSendHandler extends AbstractSmsSendHandler {

    private static final Logger log = LoggerFactory.getLogger(ChinaMobileSmsSendHandler.class);

    private final ChinaMobileSmsProperties properties;

    public ChinaMobileSmsSendHandler(ChinaMobileSmsProperties properties) {
        super(properties);
        this.properties = properties;
    }

    @Override
    protected String getChannel() {
        return SmsSupplier.CHINA_MOBILE.name();
    }

    @Override
    protected boolean execute(Template template, List<String> phones) throws TemplateIdInvalidException, ParameterOrdersInvalidException {
        String templateId = this.getTemplateId(template);
        String templateParams = this.getOrderedParamsString(template);

        ChinaMobileSmsRequest request = new ChinaMobileSmsRequest(
                this.properties.getEcName(),
                this.properties.getApId(),
                this.properties.getSecretKey(),
                templateId,
                join(phones),
                templateParams,
                this.properties.getSign());

        HttpResult result = this.http().sync(this.properties.getUri())
                .bodyType(OkHttps.FORM)
                .setBodyPara(request)
                .nothrow()
                .post();

        return result.isSuccessful();
    }
}