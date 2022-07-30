/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
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
