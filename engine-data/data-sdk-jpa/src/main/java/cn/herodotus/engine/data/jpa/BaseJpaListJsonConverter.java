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

package cn.herodotus.engine.data.jpa;

import cn.herodotus.engine.assistant.core.domain.entity.AbstractEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description : Jpa JSON字符串字段与List自动转换 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/31 11:11
 */
public abstract class BaseJpaListJsonConverter<T extends AbstractEntity> implements AttributeConverter<List<T>, String> {

    @Override
    public String convertToDatabaseColumn(List<T> ts) {
        return JSON.toJSONString(ts);
    }

    @Override
    public List<T> convertToEntityAttribute(String s) {
        List<T> result = JSON.parseObject(s, new TypeReference<List<T>>() {
        });
        if (CollectionUtils.isEmpty(result)) {
            return new ArrayList<>();
        } else {
            return result;
        }
    }
}
