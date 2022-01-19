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

import domain.WebSocketMessage;
import exception.IllegalChannelException;
import exception.PrincipalNotFoundException;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import properties.WebSocketProperties;

/**
 * <p>Description: WebSocket集群处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:46
 */
public class WebSocketClusterProcessor implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClusterProcessor.class);

    private RedissonClient redissonClient;
    private WebSocketProperties webSocketProperties;
    private WebSocketMessageSender webSocketMessageSender;

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void setWebSocketProperties(WebSocketProperties webSocketProperties) {
        this.webSocketProperties = webSocketProperties;
    }

    public void setWebSocketMessageSender(WebSocketMessageSender webSocketMessageSender) {
        this.webSocketMessageSender = webSocketMessageSender;
    }

    /**
     * 发送给集群中指定用户信息。
     *
     * @param webSocketMessage 发送内容参数实体 {@link WebSocketMessage}
     */
    public void toClusterUser(WebSocketMessage<String> webSocketMessage) {
        try {
            webSocketMessageSender.toUser(webSocketMessage);
        } catch (PrincipalNotFoundException e) {
            RTopic rTopic = redissonClient.getTopic(webSocketProperties.getTopic(), new JsonJacksonCodec());
            rTopic.publish(webSocketMessage);
            log.debug("[Herodotus] |- Current instance can not found user [{}], publish message.", webSocketMessage.getTo());
        } catch (IllegalChannelException e) {
            log.error("[Herodotus] |- Web socket channel is incorrect.");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RTopic topic = redissonClient.getTopic(webSocketProperties.getTopic());
        topic.addListener(WebSocketMessage.class, (MessageListener<WebSocketMessage<String>>) (charSequence, webSocketMessage) -> {
            log.debug("[Herodotus] |- Redisson received web socket sync message [{}]", webSocketMessage);
            webSocketMessageSender.toUser(webSocketMessage);
        });
    }
}
