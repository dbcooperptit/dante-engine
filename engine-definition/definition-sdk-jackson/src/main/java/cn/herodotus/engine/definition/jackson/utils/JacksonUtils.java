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

package cn.herodotus.engine.definition.jackson.utils;

import cn.herodotus.engine.definition.jackson.FastJsonSerializerFeatureCompatibleForJackson;
import cn.herodotus.engine.definition.jackson.SerializerFeature;
import cn.herodotus.engine.definition.jackson.deserializer.XssStringJsonDeserializer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author gengwei.zheng
 */
public class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);

    private static volatile JacksonUtils INSTANCE;
    private final ObjectMapper objectMapper;

    private JacksonUtils() {
        objectMapper = new ObjectMapper();
        // 设置为中国上海时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 空值不序列化
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时，日期的统一格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 排序key
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // 忽略空bean转json错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略在json字符串中存在，在java类中不存在字段，防止错误。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 单引号处理
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

        /**
         * 序列换成json时,将所有的long变成string
         * js中long过长精度丢失
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addDeserializer(String.class, new XssStringJsonDeserializer());

        objectMapper.registerModule(simpleModule);
        // 兼容fastJson 的一些空值处理
        SerializerFeature[] features = new SerializerFeature[]{
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullMapAsEmpty
        };
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new FastJsonSerializerFeatureCompatibleForJackson(features)));
    }

    private static JacksonUtils getInstance() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (JacksonUtils.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new JacksonUtils();
                }
            }
        }

        return INSTANCE;
    }

    private ObjectMapper objectMapper() {
        return this.objectMapper;
    }

    public static ObjectMapper getObjectMapper() {
        return JacksonUtils.getInstance().objectMapper();
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- JacksonUtils toObject parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- JacksonUtils toObject mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- JacksonUtils toObject read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> String toJson(T entity) {
        try {
            return getObjectMapper().writeValueAsString(entity);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return getObjectMapper().readValue(json, javaType);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- JacksonUtils toCollection read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> T toCollection(String json, TypeReference<T> typeReference) {
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonParseException e) {
            logger.error("-| [Herodotus]: JacksonUtils toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("-| [Herodotus]: JacksonUtils toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("-| [Herodotus]: JacksonUtils toCollection read content error! {}", e.getMessage());
        }

        return null;
    }
}
