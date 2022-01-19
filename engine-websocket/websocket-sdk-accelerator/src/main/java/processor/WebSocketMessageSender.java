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

package processor;

import domain.WebSocketChannel;
import domain.WebSocketMessage;
import exception.IllegalChannelException;
import exception.PrincipalNotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import properties.WebSocketProperties;

/**
 * <p>Description: Web Socket 服务端消息发送 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:47
 */
public class WebSocketMessageSender {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageSender.class);

    private SimpMessagingTemplate simpMessagingTemplate;
    private SimpUserRegistry simpUserRegistry;
    private WebSocketProperties webSocketProperties;

    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void setSimpUserRegistry(SimpUserRegistry simpUserRegistry) {
        this.simpUserRegistry = simpUserRegistry;
    }

    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    /**
     * 发送给指定用户信息。
     *
     * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
     * @param <T>              指定 payload 类型
     * @throws IllegalChannelException    Web Socket 通道设置错误
     * @throws PrincipalNotFoundException 该服务中无法找到与 identity 对应的用户 Principal
     */
    public <T> void toUser(WebSocketMessage<T> webSocketMessage) throws IllegalChannelException, PrincipalNotFoundException {
        WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannel(webSocketMessage.getChannel());
        if (ObjectUtils.isEmpty(webSocketChannel)) {
            throw new IllegalChannelException("Web socket channel is incorrect!");
        }

        SimpUser simpUser = simpUserRegistry.getUser(webSocketMessage.getTo());
        if (ObjectUtils.isEmpty(simpUser)) {
            throw new PrincipalNotFoundException("Web socket user principal is not found!");
        }

        log.debug("[Herodotus] |- Web socket send message to user [{}].", webSocketMessage.getTo());
        simpMessagingTemplate.convertAndSendToUser(webSocketMessage.getTo(), webSocketChannel.getDestination(), webSocketMessage.getPayload());
    }

    /**
     * 广播 WebSocket 信息
     *
     * @param payload 发送的内容
     * @param <T>     payload 类型
     */
    public <T> void toAll(T payload) {
        simpMessagingTemplate.convertAndSend(webSocketProperties.getBroadcast(), payload);
    }
}
