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
import cn.herodotus.engine.rest.core.annotation.Idempotent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

/**
 * <p> Description : BaseRestController </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/2/29 15:28
 */
public abstract class BaseWriteableRestController<E extends AbstractEntity, ID extends Serializable> extends BaseReadableRestController<E, ID> implements WriteableController<E, ID> {

    @Override
    public ReadableService<E, ID> getReadableService() {
        return this.getWriteableService();
    }

    @Idempotent
    @Operation(summary = "保存或更新数据", description = "接收JSON数据，转换为实体，进行保存或更新",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "已保存数据", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "domain", required = true, description = "可转换为实体的json数据")
    })
    @PostMapping
    @Override
    public Result<E> saveOrUpdate(@RequestBody E domain) {
        return WriteableController.super.saveOrUpdate(domain);
    }

    @Idempotent
    @Operation(summary = "删除数据", description = "根据实体ID删除数据，以及相关联的关联数据",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "操作消息", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "id", required = true, in = ParameterIn.PATH, description = "实体ID，@Id注解对应的实体属性")
    })
    @DeleteMapping("/{id}")
    @Override
    public Result<String> delete(@PathVariable ID id) {
        return WriteableController.super.delete(id);
    }
}
