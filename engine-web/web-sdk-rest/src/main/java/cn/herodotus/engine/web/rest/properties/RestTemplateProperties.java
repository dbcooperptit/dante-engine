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

package cn.herodotus.engine.web.rest.properties;

import cn.herodotus.engine.web.core.constants.WebConstants;
import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * <p>Description: Rest Template 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/16 18:57
 */
@ConfigurationProperties(prefix = WebConstants.PROPERTY_REST_TEMPLATE)
public class RestTemplateProperties {

    /**
     * 连接池最大连接数，默认：1000
     */
    private Integer maxTotal = 1000;
    /**
     * 连接池每条线路最大值，默认：500
     */
    private Integer maxPerRoute = 500;
    /**
     * 连接上服务器的超时时间，默认值 10000 毫秒；
     */
    private Integer connectTimeout = 10000;
    /**
     * 从连接池中获取连接的超时时间, 默认值 1000 毫秒
     */
    private Integer connectionRequestTimeout = 1000;
    /**
     * 返回数据超时时间，默认值 20000 毫秒
     */
    private Integer socketTimeout = 20000;
    /**
     * 开启内容压缩
     */
    private Boolean compressionEnabled = false;

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(Integer maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(Boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("maxTotal", maxTotal)
                .add("maxPerRoute", maxPerRoute)
                .add("connectTimeout", connectTimeout)
                .add("connectionRequestTimeout", connectionRequestTimeout)
                .add("socketTimeout", socketTimeout)
                .add("compressionEnabled", compressionEnabled)
                .toString();
    }
}
