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

package cn.herodotus.engine.web.rest.autoconfigure;

import cn.herodotus.engine.web.rest.annotation.ConditionalOnFeignUseHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Description: HttpClient 自动配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/5/29 18:46
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnFeignUseHttpClient
public class HttpClientAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OkHttpAutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Web HttpClient] Auto Configure.");
    }

    private final Timer connectionManagerTimer = new Timer(
            "FeignApacheHttpClientConfiguration.connectionManagerTimer", true);

    @Autowired(required = false)
    private RegistryBuilder registryBuilder;

    private CloseableHttpClient httpClient;

    @Bean
    @ConditionalOnMissingBean(HttpClientConnectionManager.class)
    public HttpClientConnectionManager connectionManager(ApacheHttpClientConnectionManagerFactory connectionManagerFactory, FeignHttpClientProperties feignHttpClientProperties) {

        final HttpClientConnectionManager connectionManager = connectionManagerFactory.newConnectionManager(
                feignHttpClientProperties.isDisableSslValidation(), feignHttpClientProperties.getMaxConnections(),
                feignHttpClientProperties.getMaxConnectionsPerRoute(), feignHttpClientProperties.getTimeToLive(),
                feignHttpClientProperties.getTimeToLiveUnit(), this.registryBuilder);
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectionManager.closeExpiredConnections();
            }
        }, 30000, feignHttpClientProperties.getConnectionTimerRepeat());
        return connectionManager;
    }

    @Bean
    public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory, HttpClientConnectionManager httpClientConnectionManager, FeignHttpClientProperties feignHttpClientProperties) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(feignHttpClientProperties.getConnectionTimeout())
                .setRedirectsEnabled(feignHttpClientProperties.isFollowRedirects()).build();
        this.httpClient = httpClientFactory.createBuilder().setConnectionManager(httpClientConnectionManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();
        return this.httpClient;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        log.trace("[Herodotus] |- Bean [Client Http Request Factory for HttpClient] Auto Configure.");
        return httpComponentsClientHttpRequestFactory;
    }

    @PreDestroy
    public void destroy() {
        this.connectionManagerTimer.cancel();
        if (this.httpClient != null) {
            try {
                this.httpClient.close();
            } catch (IOException e) {
                log.trace("[Herodotus] |- Could not correctly close httpClient.");
            }
        }
    }
}
