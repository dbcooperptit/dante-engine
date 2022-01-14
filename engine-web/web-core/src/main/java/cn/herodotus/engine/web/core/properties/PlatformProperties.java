/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2021 Zhenggengwei<码匠君>, herodotus@aliyun.com
 *
 * This file is part of Herodotus Cloud.
 *
 * Herodotus Cloud is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Cloud is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with with Herodotus Cloud;
 * if no see <https://gitee.com/herodotus/herodotus-cloud>
 *
 * - Author: Zhenggengwei<码匠君>
 * - Contact: herodotus@aliyun.com
 * - License: GNU Lesser General Public License (LGPL)
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.web.core.properties;

import cn.herodotus.engine.definition.core.constants.SecurityConstants;
import cn.herodotus.engine.definition.core.constants.SymbolConstants;
import cn.herodotus.engine.definition.core.enums.ProtocolType;
import cn.herodotus.engine.web.core.constants.WebPropertyConstants;
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
@ConfigurationProperties(prefix = WebPropertyConstants.PROPERTY_HERODOTUS_PLATFORM)
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

        private ProtocolType protocol = ProtocolType.HTTP;
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
        private String upmsServiceName = "herodotus-cloud-upms-ability";;

        private String convertAddressToUri(String address) {
            String content = address;
            if (StringUtils.endsWith(address, SymbolConstants.FORWARD_SLASH)) {
                content = StringUtils.removeEnd(address, SymbolConstants.FORWARD_SLASH);
            }

            if (StringUtils.startsWith(content, ProtocolType.HTTP.getPrefix())) {
                return content;
            } else {
                return this.getProtocol().getFormat() + content;
            }
        }

        public ProtocolType getProtocol() {
            return protocol;
        }

        public void setProtocol(ProtocolType protocol) {
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
