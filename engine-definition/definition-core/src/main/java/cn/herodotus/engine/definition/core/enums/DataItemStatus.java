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

package cn.herodotus.engine.definition.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gengwei.zheng
 */
@Schema(title = "数据状态")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataItemStatus {

    /**
     * 数据条目已启用
     */
    ENABLE(0, "启用"),
    /**
     * 数据条目被启用
     */
    FORBIDDEN(1, "禁用"),
    /**
     * 数据条目被锁定
     */
    LOCKING(2, "锁定"),
    /**
     * 数据条目已过期
     */
    EXPIRED(3, "过期");

    @Schema(title = "索引")
    private final Integer index;
    @Schema(title = "文字")
    private final String text;

    private static final Map<Integer, DataItemStatus> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> TO_JSON_STRUCT = new ArrayList<>();

    static {
        for (DataItemStatus dataItemStatus : DataItemStatus.values()) {
            INDEX_MAP.put(dataItemStatus.getIndex(), dataItemStatus);
            TO_JSON_STRUCT.add(dataItemStatus.getIndex(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", dataItemStatus.getIndex())
                            .put("key", dataItemStatus.name())
                            .put("text", dataItemStatus.getText())
                            .build());
        }
    }

    DataItemStatus(Integer index, String text) {
        this.index = index;
        this.text = text;
    }

    /**
     * 不加@JsonValue，转换的时候转换出完整的对象。
     * 加了@JsonValue，只会显示相应的属性的值
     * <p>
     * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
     *
     * @return Enum索引
     */
    @JsonValue
    public Integer getIndex() {
        return index;
    }

    public String getText() {
        return this.text;
    }

    public static DataItemStatus getStatus(Integer status) {
        return INDEX_MAP.get(status);
    }

    public static List<Map<String, Object>> getToJsonStruct() {
        return TO_JSON_STRUCT;
    }
}
