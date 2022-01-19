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

package properties;

import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.herodotus.engine.websocket.core.constants.WebSocketConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: Web Socket 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:38
 */
@ConfigurationProperties(prefix = WebSocketConstants.PROPERTY_HERODOTUS_WEBSOCKET)
public class WebSocketProperties {

    /**
     * 客户端尝试连接端点
     */
    private String endpoint = "stomp/websocketjs";
    /**
     * WebSocket 广播消息代理地址
     */
    private String broadcast = "topic";

    /**
     * WebSocket 点对点消息代理地址
     */
    private String peerToPeer = "private";

    /**
     * 全局使用的消息前缀
     */
    private List<String> applicationDestinationPrefixes;

    /**
     * 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
     */
    private String userDestinationPrefix;

    /**
     * 集群模式下，信息同步消息队列Topic
     */
    private String topic = "ws";

    /**
     * 请求中传递的用户身份标识属性名
     */
    private String principalAttribute = "openid";

    private String format(String endpoint) {
        if (StringUtils.isNotBlank(endpoint) && !StringUtils.startsWith(endpoint, SymbolConstants.FORWARD_SLASH)) {
            return SymbolConstants.FORWARD_SLASH + endpoint;
        } else {
            return endpoint;
        }
    }

    public String getEndpoint() {
        return format(endpoint);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBroadcast() {
        return format(broadcast);
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getPeerToPeer() {
        return format(peerToPeer);
    }

    public void setPeerToPeer(String peerToPeer) {
        this.peerToPeer = peerToPeer;
    }

    public List<String> getApplicationDestinationPrefixes() {
        return applicationDestinationPrefixes;
    }

    public void setApplicationDestinationPrefixes(List<String> applicationDestinationPrefixes) {
        this.applicationDestinationPrefixes = applicationDestinationPrefixes;
    }

    public String[] getApplicationPrefixes() {
        List<String> prefixes = this.getApplicationDestinationPrefixes();
        if (CollectionUtils.isNotEmpty(prefixes)) {
            List<String> wellFormed = prefixes.stream().map(this::format).collect(Collectors.toList());
            String[] result = new String[wellFormed.size()];
            return wellFormed.toArray(result);
        } else {
            return new String[]{};
        }
    }

    public String getUserDestinationPrefix() {
        return format(userDestinationPrefix);
    }

    public void setUserDestinationPrefix(String userDestinationPrefix) {
        this.userDestinationPrefix = userDestinationPrefix;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPrincipalAttribute() {
        return principalAttribute;
    }

    public void setPrincipalAttribute(String principalAttribute) {
        this.principalAttribute = principalAttribute;
    }
}
