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

package cn.herodotus.engine.data.jpa.hibernate.postgresql;

import cn.herodotus.engine.data.jpa.hibernate.AbstractArrayType;
import org.hibernate.HibernateException;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * <p>Description: 自定义文本数组类型 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/15 10:34
 */
public class TextArrayType extends AbstractArrayType<String> implements Serializable {

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    protected Object safeReturnedObject() {
        return new String[]{};
    }

    @Override
    protected String dbRealTypeName() {
        return "TEXT";
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        if (x == null) {
            return 0;
        }

        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

}
