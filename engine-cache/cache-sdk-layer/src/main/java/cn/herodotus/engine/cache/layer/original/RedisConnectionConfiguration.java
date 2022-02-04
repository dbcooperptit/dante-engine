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

package cn.herodotus.engine.cache.layer.original;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 迁移的Redis原始代码 </p>
 * <p>
 * 从 Spring Boot Redis 中迁移出的代码，解决以包的形式，自定义RedisTemplate无法配置问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/1/18 16:16
 */
public class RedisConnectionConfiguration {

    private static final boolean COMMONS_POOL2_AVAILABLE = ClassUtils.isPresent("org.apache.commons.pool2.ObjectPool",
            RedisConnectionConfiguration.class.getClassLoader());

    private final RedisProperties properties;

    private final RedisStandaloneConfiguration standaloneConfiguration;

    private final RedisSentinelConfiguration sentinelConfiguration;

    private final RedisClusterConfiguration clusterConfiguration;

    protected RedisConnectionConfiguration(RedisProperties properties,
                                           ObjectProvider<RedisStandaloneConfiguration> standaloneConfigurationProvider,
                                           ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                           ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider) {
        this.properties = properties;
        this.standaloneConfiguration = standaloneConfigurationProvider.getIfAvailable();
        this.sentinelConfiguration = sentinelConfigurationProvider.getIfAvailable();
        this.clusterConfiguration = clusterConfigurationProvider.getIfAvailable();
    }

    protected final RedisStandaloneConfiguration getStandaloneConfig() {
        if (this.standaloneConfiguration != null) {
            return this.standaloneConfiguration;
        }
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (StringUtils.hasText(this.properties.getUrl())) {
            RedisConnectionConfiguration.ConnectionInfo connectionInfo = parseUrl(this.properties.getUrl());
            config.setHostName(connectionInfo.getHostName());
            config.setPort(connectionInfo.getPort());
            config.setUsername(connectionInfo.getUsername());
            config.setPassword(RedisPassword.of(connectionInfo.getPassword()));
        } else {
            config.setHostName(this.properties.getHost());
            config.setPort(this.properties.getPort());
            config.setUsername(this.properties.getUsername());
            config.setPassword(RedisPassword.of(this.properties.getPassword()));
        }
        config.setDatabase(this.properties.getDatabase());
        return config;
    }

    protected final RedisSentinelConfiguration getSentinelConfig() {
        if (this.sentinelConfiguration != null) {
            return this.sentinelConfiguration;
        }
        RedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
        if (sentinelProperties != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(sentinelProperties.getMaster());
            config.setSentinels(createSentinels(sentinelProperties));
            config.setUsername(this.properties.getUsername());
            if (this.properties.getPassword() != null) {
                config.setPassword(RedisPassword.of(this.properties.getPassword()));
            }
            if (sentinelProperties.getPassword() != null) {
                config.setSentinelPassword(RedisPassword.of(sentinelProperties.getPassword()));
            }
            config.setDatabase(this.properties.getDatabase());
            return config;
        }
        return null;
    }

    /**
     * Create a {@link RedisClusterConfiguration} if necessary.
     *
     * @return {@literal null} if no cluster settings are set.
     */
    protected final RedisClusterConfiguration getClusterConfiguration() {
        if (this.clusterConfiguration != null) {
            return this.clusterConfiguration;
        }
        if (this.properties.getCluster() == null) {
            return null;
        }
        RedisProperties.Cluster clusterProperties = this.properties.getCluster();
        RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());
        if (clusterProperties.getMaxRedirects() != null) {
            config.setMaxRedirects(clusterProperties.getMaxRedirects());
        }
        config.setUsername(this.properties.getUsername());
        if (this.properties.getPassword() != null) {
            config.setPassword(RedisPassword.of(this.properties.getPassword()));
        }
        return config;
    }

    protected final RedisProperties getProperties() {
        return this.properties;
    }

    protected boolean isPoolEnabled(RedisProperties.Pool pool) {
        Boolean enabled = pool.getEnabled();
        return (enabled != null) ? enabled : COMMONS_POOL2_AVAILABLE;
    }

    private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<>();
        for (String node : sentinel.getNodes()) {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                nodes.add(new RedisNode(parts[0], Integer.parseInt(parts[1])));
            } catch (RuntimeException ex) {
                throw new IllegalStateException("Invalid redis sentinel property '" + node + "'", ex);
            }
        }
        return nodes;
    }

    protected RedisConnectionConfiguration.ConnectionInfo parseUrl(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"redis".equals(scheme) && !"rediss".equals(scheme)) {
                throw new RedisUrlSyntaxException(url);
            }
            boolean useSsl = ("rediss".equals(scheme));
            String username = null;
            String password = null;
            if (uri.getUserInfo() != null) {
                String candidate = uri.getUserInfo();
                int index = candidate.indexOf(':');
                if (index >= 0) {
                    username = candidate.substring(0, index);
                    password = candidate.substring(index + 1);
                } else {
                    password = candidate;
                }
            }
            return new RedisConnectionConfiguration.ConnectionInfo(uri, useSsl, username, password);
        } catch (URISyntaxException ex) {
            throw new RedisUrlSyntaxException(url, ex);
        }
    }

    static class ConnectionInfo {

        private final URI uri;

        private final boolean useSsl;

        private final String username;

        private final String password;

        ConnectionInfo(URI uri, boolean useSsl, String username, String password) {
            this.uri = uri;
            this.useSsl = useSsl;
            this.username = username;
            this.password = password;
        }

        boolean isUseSsl() {
            return this.useSsl;
        }

        String getHostName() {
            return this.uri.getHost();
        }

        int getPort() {
            return this.uri.getPort();
        }

        String getUsername() {
            return this.username;
        }

        String getPassword() {
            return this.password;
        }

    }
}
