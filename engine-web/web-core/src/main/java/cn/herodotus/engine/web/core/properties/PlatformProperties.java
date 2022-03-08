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

package cn.herodotus.engine.web.core.properties;

import cn.herodotus.engine.assistant.core.constants.SecurityConstants;
import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.herodotus.engine.assistant.core.enums.Protocol;
import cn.herodotus.engine.web.core.constants.WebConstants;
import cn.herodotus.engine.web.core.enums.Architecture;
import cn.herodotus.engine.web.core.enums.DataAccessStrategy;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 平台服务相关配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/17 15:22
 */
@ConfigurationProperties(prefix = WebConstants.PROPERTY_HERODOTUS_PLATFORM)
public class PlatformProperties {

    /**
     * 平台架构类型，默认：DISTRIBUTED（分布式架构）
     */
    private Architecture architecture = Architecture.DISTRIBUTED;
    /**
     * 数据访问策略，默认：
     */
    private DataAccessStrategy dataAccessStrategy = DataAccessStrategy.REMOTE;

    private Endpoint endpoint = new Endpoint();

    private Swagger swagger = new Swagger();

    private Debezium debezium = new Debezium();

    public Architecture getArchitecture() {
        return architecture;
    }

    public void setArchitecture(Architecture architecture) {
        this.architecture = architecture;
    }

    public DataAccessStrategy getDataAccessStrategy() {
        return dataAccessStrategy;
    }

    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        this.dataAccessStrategy = dataAccessStrategy;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }

    public Debezium getDebezium() {
        return debezium;
    }

    public void setDebezium(Debezium debezium) {
        this.debezium = debezium;
    }

    public static class Endpoint {

        private Protocol protocol = Protocol.HTTP;
        /**
         * 网关服务地址。可以是IP+端口，可以是域名
         */
        private String gatewayAddress;
        /**
         * 统一认证中心服务地址
         */
        private String uaaServiceUri;
        /**
         * 统一权限管理服务地址
         */
        private String upmsServiceUri;
        /**
         * OAuth2 /oauth/token端点地址，可修改为自定义地址
         */
        private String accessTokenUri;
        /**
         * OAuth2 /oauth/authorize端点地址，可修改为自定义地址
         */
        private String userAuthorizationUri;
        /**
         * OAuth2 /oauth/check_token端点地址，可修改为自定义地址
         */
        private String tokenInfoUri;
        /**
         * 用户信息获取地址
         */
        private String userInfoUri;
        /**
         * 认证中心服务名称
         */
        private String uaaServiceName = "herodotus-cloud-uaa";
        /**
         * 用户中心服务名称
         */
        private String upmsServiceName = "herodotus-cloud-upms-ability";
        ;

        private String convertAddressToUri(String address) {
            String content = address;
            if (StringUtils.endsWith(address, SymbolConstants.FORWARD_SLASH)) {
                content = StringUtils.removeEnd(address, SymbolConstants.FORWARD_SLASH);
            }

            if (StringUtils.startsWith(content, Protocol.HTTP.getPrefix())) {
                return content;
            } else {
                return this.getProtocol().getFormat() + content;
            }
        }

        public Protocol getProtocol() {
            return protocol;
        }

        public void setProtocol(Protocol protocol) {
            this.protocol = protocol;
        }

        public String getGatewayAddress() {
            if (StringUtils.isNotBlank(this.gatewayAddress)) {
                return convertAddressToUri(this.gatewayAddress);
            }
            return gatewayAddress;
        }

        public void setGatewayAddress(String gatewayAddress) {
            this.gatewayAddress = gatewayAddress;
        }

        public String getUaaServiceUri() {
            if (StringUtils.isBlank(this.uaaServiceUri)) {
                return this.getGatewayAddress() + SymbolConstants.FORWARD_SLASH + getUaaServiceName();
            }
            return uaaServiceUri;
        }

        public void setUaaServiceUri(String uaaServiceUri) {
            this.uaaServiceUri = uaaServiceUri;
        }

        public String getUpmsServiceUri() {
            if (StringUtils.isBlank(this.upmsServiceUri)) {
                return this.getGatewayAddress() + SymbolConstants.FORWARD_SLASH + getUpmsServiceName();
            }
            return upmsServiceUri;
        }

        public void setUpmsServiceUri(String upmsServiceUri) {
            this.upmsServiceUri = upmsServiceUri;
        }

        public String getAccessTokenUri() {
            if (StringUtils.isBlank(this.accessTokenUri)) {
                return this.getUaaServiceUri() + SecurityConstants.ENDPOINT_OAUTH_TOKEN;
            }
            return accessTokenUri;
        }

        public void setAccessTokenUri(String accessTokenUri) {
            this.accessTokenUri = accessTokenUri;
        }

        public String getUserAuthorizationUri() {
            if (StringUtils.isBlank(this.userAuthorizationUri)) {
                return this.getUserInfoUri() + SecurityConstants.ENDPOINT_OAUTH_AUTHORIZE;
            }
            return userAuthorizationUri;
        }

        public void setUserAuthorizationUri(String userAuthorizationUri) {
            this.userAuthorizationUri = userAuthorizationUri;
        }

        public String getTokenInfoUri() {
            if (StringUtils.isBlank(this.tokenInfoUri)) {
                return this.getUaaServiceUri() + SecurityConstants.ENDPOINT_OAUTH_CHECK_TOKEN;
            }
            return tokenInfoUri;
        }

        public void setTokenInfoUri(String tokenInfoUri) {
            this.tokenInfoUri = tokenInfoUri;
        }

        public String getUserInfoUri() {
            if (StringUtils.isBlank(this.userInfoUri)) {
                return this.getUaaServiceUri() + SecurityConstants.ENDPOINT_OAUTH_IDENTITY_PROFILE;
            }
            return userInfoUri;
        }

        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }

        public String getUaaServiceName() {
            return uaaServiceName;
        }

        public void setUaaServiceName(String uaaServiceName) {
            this.uaaServiceName = uaaServiceName;
        }

        public String getUpmsServiceName() {
            return upmsServiceName;
        }

        public void setUpmsServiceName(String upmsServiceName) {
            this.upmsServiceName = upmsServiceName;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("protocol", protocol)
                    .add("gatewayAddress", gatewayAddress)
                    .add("uaaServiceUri", uaaServiceUri)
                    .add("upmsServiceUri", upmsServiceUri)
                    .add("accessTokenUri", accessTokenUri)
                    .add("userAuthorizationUri", userAuthorizationUri)
                    .add("tokenInfoUri", tokenInfoUri)
                    .add("userInfoUri", userInfoUri)
                    .toString();
        }
    }

    public static class Swagger {

        /**
         * 是否开启Swagger
         */
        private Boolean enabled;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("enabled", enabled)
                    .toString();
        }
    }

    public static class Debezium {

        /**
         * 是否开启 Debezium
         */
        private Boolean enabled = false;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }
}
