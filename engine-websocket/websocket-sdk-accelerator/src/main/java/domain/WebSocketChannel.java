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

package domain;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: WebSocket通道 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/24 18:41
 */
public enum WebSocketChannel {
    /**
     * 个人通知
     */
    NOTICE("/notice", "个人通知");

    @Schema(title = "消息端点")
    private final String destination;
    @Schema(title = "说明")
    private final String description;

    private static final Map<String, WebSocketChannel> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCT = new ArrayList<>();

    static {
        for (WebSocketChannel webSocketChannel : WebSocketChannel.values()) {
            INDEX_MAP.put(webSocketChannel.name(), webSocketChannel);
            JSON_STRUCT.add(webSocketChannel.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", webSocketChannel.ordinal())
                            .put("key", webSocketChannel.name())
                            .put("text", webSocketChannel.getDescription())
                            .build());
        }
    }

    WebSocketChannel(String destination, String description) {
        this.destination = destination;
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public static WebSocketChannel getWebSocketChannel(String code) {
        return INDEX_MAP.get(code);
    }

    public static List<Map<String, Object>> getToJsonStruct() {
        return JSON_STRUCT;
    }
}