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

package cn.herodotus.engine.web.rest.configuration;

import cn.herodotus.engine.assistant.core.json.jackson2.utils.JacksonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.annotation.PostConstruct;

/**
 * <p>Description: Jackson配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/8 17:15
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JacksonConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Web Rest Jackson] Auto Configure.");
    }

    @Bean(name = "jacksonObjectMapper")
    @ConditionalOnMissingBean(ObjectMapper.class)
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        log.trace("[Herodotus] |- Bean [Jackson Object Mapper] Auto Configure.");
        return JacksonUtils.getObjectMapper();
    }

    /**
     * 转换器全局配置
     *
     * @return MappingJackson2HttpMessageConverter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        log.trace("[Herodotus] |- Bean [Jackson Http Message Converter] Auto Configure.");
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}
