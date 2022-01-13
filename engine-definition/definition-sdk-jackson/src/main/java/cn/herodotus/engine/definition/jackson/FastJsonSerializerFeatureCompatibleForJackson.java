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

package cn.herodotus.engine.definition.jackson;

import cn.herodotus.engine.definition.jackson.serializer.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Date;
import java.util.List;

/**
 * @author gengwei.zheng
 */
public class FastJsonSerializerFeatureCompatibleForJackson extends BeanSerializerModifier {

    final private JsonSerializer<Object> nullBooleanJsonSerializer;
    final private JsonSerializer<Object> nullNumberJsonSerializer;
    final private JsonSerializer<Object> nullListJsonSerializer;
    final private JsonSerializer<Object> nullStringJsonSerializer;
    final private JsonSerializer<Object> nullMapJsonSerializer;

    public FastJsonSerializerFeatureCompatibleForJackson(SerializerFeature... features) {
        int config = 0;
        for (SerializerFeature feature : features) {
            config |= feature.mask;
        }
        nullBooleanJsonSerializer = (config & SerializerFeature.WriteNullBooleanAsFalse.mask) != 0 ? new NullBooleanSerializer() : null;
        nullNumberJsonSerializer = (config & SerializerFeature.WriteNullNumberAsZero.mask) != 0 ? new NullNumberSerializer() : null;
        nullListJsonSerializer = (config & SerializerFeature.WriteNullListAsEmpty.mask) != 0 ? new NullListJsonSerializer() : null;
        nullStringJsonSerializer = (config & SerializerFeature.WriteNullStringAsEmpty.mask) != 0 ? new NullStringSerializer() : null;
        nullMapJsonSerializer = (config & SerializerFeature.WriteNullMapAsEmpty.mask) != 0 ? new NullMapSerializer() : null;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            final JavaType javaType = writer.getType();
            final Class<?> rawClass = javaType.getRawClass();
            if (javaType.isArrayType() || javaType.isCollectionLikeType()) {
                writer.assignNullSerializer(nullListJsonSerializer);
            } else if (Number.class.isAssignableFrom(rawClass) && rawClass.getName().startsWith("java.lang")) {
                writer.assignNullSerializer(nullNumberJsonSerializer);
            } else if (Boolean.class.equals(rawClass)) {
                writer.assignNullSerializer(nullBooleanJsonSerializer);
            } else if (String.class.equals(rawClass) || Date.class.equals(rawClass)) {
                writer.assignNullSerializer(nullStringJsonSerializer);
            } else {
                writer.assignNullSerializer(nullMapJsonSerializer);
            }
        }
        return beanProperties;
    }
}
