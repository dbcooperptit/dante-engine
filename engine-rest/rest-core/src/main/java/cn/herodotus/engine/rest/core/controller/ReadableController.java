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
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 只读Controller </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/4/30 11:00
 */
public interface ReadableController<E extends AbstractEntity, ID extends Serializable> extends Controller {

    /**
     * 获取Service
     *
     * @return Service
     */
    ReadableService<E, ID> getReadableService();

    /**
     * 查询分页数据
     * @param pageNumber 当前页码，起始页码 0
     * @param pageSize 每页显示数据条数
     * @return {@link Result}
     */
    default Result<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize) {
        Page<E> pages = getReadableService().findByPage(pageNumber, pageSize);
        return result(pages);
    }

    default Result<List<E>> findAll() {
        List<E> domains = getReadableService().findAll();
        return result(domains);
    }

    default Result<E> findById(ID id) {
        E domain = getReadableService().findById(id);
        return result(domain);
    }
}
