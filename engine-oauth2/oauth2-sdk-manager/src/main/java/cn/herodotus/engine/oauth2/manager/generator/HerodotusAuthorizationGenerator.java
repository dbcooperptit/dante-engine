/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020-2030 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.oauth2.manager.generator;

import cn.herodotus.engine.oauth2.manager.entity.HerodotusAuthorization;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

/**
 * <p>Description: OAuth2Authorization Id 生成器 </p>
 *
 * 指定ID生成器，解决实体ID无法手动设置问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/1/22 18:10
 */
public class HerodotusAuthorizationGenerator extends UUIDGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (ObjectUtils.isEmpty(object)) {
            throw new HibernateException(new NullPointerException());
        }

        HerodotusAuthorization HerodotusAuthorization = (HerodotusAuthorization) object;

        if (StringUtils.isEmpty(HerodotusAuthorization.getId())) {
            return super.generate(session, object);
        } else {
            return HerodotusAuthorization.getId();
        }
    }
}