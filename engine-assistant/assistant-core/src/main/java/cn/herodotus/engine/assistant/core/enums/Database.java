/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020-2030 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.assistant.core.enums;

import cn.herodotus.engine.assistant.core.definition.enums.BaseUiEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 数据库类型 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/21 17:02
 */
@Schema(title = "数据库类别")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Database implements BaseUiEnum<Integer> {
    /**
     * 数据库类型
     */
    ORACLE(0, "Oracle"),
    POSTGRESQL(1, "PostgreSQL"),
    MYSQL(2, "Mysql"),
    MARIADB(3, "MariaDB"),
    SQLSERVER(4, "SQLServer"),
    SYBASE(5, "SyBase"),
    SAPDB(6, "SAPDB"),
    DB2(7, "DB2"),
    H2(8, "H2"),
    REDIS(9, "Redis");

    @Schema(title = "枚举值")
    private final Integer value;
    @Schema(name = "文字")
    private final String description;

    private static final Map<Integer, Database> INDEX_MAP = new HashMap<>();
    private static final List<Map<String, Object>> JSON_STRUCTURE = new ArrayList<>();

    static {
        for (Database database : Database.values()) {
            INDEX_MAP.put(database.getValue(), database);
            JSON_STRUCTURE.add(database.getValue(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", database.getValue())
                            .put("key", database.name())
                            .put("text", database.getDescription())
                            .put("index", database.getValue())
                            .build());
        }
    }

    Database(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 不加@JsonValue，转换的时候转换出完整的对象。
     * 加了@JsonValue，只会显示相应的属性的值
     * <p>
     * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
     *
     * @return Enum枚举值
     */
    @JsonValue
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static Database get(Integer index) {
        return INDEX_MAP.get(index);
    }

    public static List<Map<String, Object>> getPreprocessedJsonStructure() {
        return JSON_STRUCTURE;
    }
}
