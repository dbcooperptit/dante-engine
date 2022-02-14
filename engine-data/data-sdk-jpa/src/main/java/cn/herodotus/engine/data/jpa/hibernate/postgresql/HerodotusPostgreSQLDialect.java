/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.data.jpa.hibernate.postgresql;

import org.hibernate.dialect.PostgreSQL10Dialect;
import org.hibernate.type.descriptor.sql.LongVarbinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.LongVarcharTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

/**
 * <p>Description: 自定义PostgreSQLDialect，增加JSONB等类型支持 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/15 10:31
 */
public class HerodotusPostgreSQLDialect extends PostgreSQL10Dialect {

    public HerodotusPostgreSQLDialect() {
        super();
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
        this.registerColumnType(Types.ARRAY, "text[]");
    }

//    /**
//     * 重载getAddForeignKeyConstraintString方法，阻止ddl-auto添加外键关联
//     *
//     * @param constraintName
//     * @param foreignKey
//     * @param referencedTable
//     * @param primaryKey
//     * @param referencesPrimaryKey
//     * @return String
//     */
//    @Override
//    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable, String[] primaryKey, boolean referencesPrimaryKey) {
//        //  设置foreignkey对应的列值可以为空
//        return " alter " + foreignKey[0] + " set default null ";
//    }


    /**
     * 在 JPA 环境下，映射 PostgreSQL TEXT 专有类型的三种处理方式中的一种。
     * <p>
     * 这个方法将 @Lob 对应的 CLOB 和 BLOB 合并进行了处理
     *
     * @param sqlTypeDescriptor SQL 类型描述器
     * @return 处理后的 SQL 类型描述器
     * @see <a href="https://www.baeldung.com/jpa-annotation-postgresql-text-type">参考资料1</a>
     * @see <a href="https://blog.csdn.net/weixin_30539835/article/details/98190592">参考资料2</a>
     * @see <a href="https://stackoverflow.com/questions/13090089/org-hibernate-type-texttype-and-oracle">参考资料3</a>
     * @see <a href="https://www.programminghunter.com/article/2482653094/">参考资料4</a>
     */
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        switch (sqlTypeDescriptor.getSqlType()) {
            case Types.CLOB:
                return LongVarcharTypeDescriptor.INSTANCE;
            case Types.BLOB:
                return LongVarbinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}
