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

package cn.herodotus.engine.data.jpa.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.sql.*;

/**
 * <p>Description: 抽象数组类型 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/15 10:34
 */
@SuppressWarnings("unchecked")
public abstract class AbstractArrayType<T extends Serializable> extends AbstractUserType {

    protected static final int[] SQL_TYPES = {Types.ARRAY};

    @Override
    public final int[] sqlTypes() {
        return SQL_TYPES;
    }

    protected abstract Object safeReturnedObject();

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        Array array = resultSet.getArray(strings[0]);
        T[] typeArray = (T[]) array.getArray();
        if (typeArray == null) {
            return safeReturnedObject();
        }

        return typeArray;
    }

    protected abstract String dbRealTypeName();

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        Connection connection = preparedStatement.getConnection();
        T[] typeArray = (T[]) value;
        Array array = connection.createArrayOf(dbRealTypeName(), typeArray);
        if (null != array) {
            preparedStatement.setArray(index, array);
        } else {
            preparedStatement.setNull(index, SQL_TYPES[0]);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value == null ? null : ((T[]) value).clone();
    }

}
