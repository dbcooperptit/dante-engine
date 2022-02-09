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

package cn.herodotus.engine.facility.sentinel.configuration;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.facility.sentinel.enhance.HerodotusFeignSentinel;
import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.DefaultBlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import com.alibaba.fastjson.JSONObject;
import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * <p>Description: 基础设施 Sentinel 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/5 17:57
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class FacilitySentinelConfiguration {

    private static final Logger log = LoggerFactory.getLogger(FacilitySentinelConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Facility Sentinel] Auto Configure.");
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return HerodotusFeignSentinel.builder();
    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebmvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                Result result = Result.failure("Too many request, please retry later.");
                response.getWriter().print(JSONObject.toJSONString(result));
            };
        }

    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(ServerResponse.class)
    public static class WebfluxHandler {
        @Bean
        public BlockRequestHandler webfluxBlockExceptionHandler() {
            return (exchange, t) ->
                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(Result.failure(t.getMessage())));
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
    @EnableConfigurationProperties(SentinelProperties.class)
    @Import({FeignConfiguration.class})
    public static class SentinelWebConfiguration {

        @Autowired
        private SentinelProperties properties;

        @Autowired
        private Optional<UrlCleaner> urlCleanerOptional;

        @Autowired
        private Optional<BlockExceptionHandler> blockExceptionHandlerOptional;

        @Autowired
        private Optional<RequestOriginParser> requestOriginParserOptional;

        @Bean
        @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled",
                matchIfMissing = true)
        public SentinelWebInterceptor sentinelWebInterceptor(
                SentinelWebMvcConfig sentinelWebMvcConfig) {
            return new SentinelWebInterceptor(sentinelWebMvcConfig);
        }

        @Bean
        @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled",
                matchIfMissing = true)
        public SentinelWebMvcConfig sentinelWebMvcConfig() {
            SentinelWebMvcConfig sentinelWebMvcConfig = new SentinelWebMvcConfig();
            sentinelWebMvcConfig.setHttpMethodSpecify(properties.getHttpMethodSpecify());
            sentinelWebMvcConfig.setWebContextUnify(properties.getWebContextUnify());

            if (blockExceptionHandlerOptional.isPresent()) {
                blockExceptionHandlerOptional
                        .ifPresent(sentinelWebMvcConfig::setBlockExceptionHandler);
            } else {
                if (StringUtils.hasText(properties.getBlockPage())) {
                    sentinelWebMvcConfig.setBlockExceptionHandler(((request, response,
                                                                    e) -> response.sendRedirect(properties.getBlockPage())));
                } else {
                    sentinelWebMvcConfig
                            .setBlockExceptionHandler(new DefaultBlockExceptionHandler());
                }
            }

            urlCleanerOptional.ifPresent(sentinelWebMvcConfig::setUrlCleaner);
            requestOriginParserOptional.ifPresent(sentinelWebMvcConfig::setOriginParser);
            return sentinelWebMvcConfig;
        }
    }
}
