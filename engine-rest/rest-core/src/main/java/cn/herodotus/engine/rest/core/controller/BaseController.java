/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 Zhenggengwei<码匠君>, herodotus@aliyun.com
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

package cn.herodotus.engine.rest.core.controller;

import cn.herodotus.engine.assistant.core.domain.Result;
import cn.herodotus.engine.assistant.core.domain.entity.AbstractEntity;
import cn.herodotus.engine.data.core.service.ReadableService;

import java.io.Serializable;

/**
 * <p> Description : 通用Controller </p>
 * <p>
 * 单独提取出一些公共方法，是为了解决某些支持feign的controller，requestMapping 不方便统一编写的问题。
 *
 * @author : gengwei.zheng
 * @date : 2020/4/30 11:00
 */
public abstract class BaseController<E extends AbstractEntity, ID extends Serializable> implements WriteableController<E, ID> {

    /**
     * 获取Service
     *
     * @return Service
     */
    @Override
    public ReadableService<E, ID> getReadableService() {
        return this.getWriteableService();
    }

    @Override
    public Result<E> saveOrUpdate(E domain) {
        E savedDomain = getWriteableService().saveOrUpdate(domain);
        return result(savedDomain);
    }

    @Override
    public Result<String> delete(ID id) {
        Result<String> result = result(String.valueOf(id));
        getWriteableService().deleteById(id);
        return result;
    }
}
