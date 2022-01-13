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

package cn.herodotus.engine.definition.common.domain;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>Description: 错误详情 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/18 15:48
 */
@Schema(title = "响应错误详情", description = "为兼容Validation而增加的Validation错误信息实体")
public class Error implements Serializable {

    @Schema(title = "Exception完整信息", type = "string")
    private String detail;

    @Schema(title = "额外的错误信息，目前主要是Validation的Message")
    private String message;

    @Schema(title = "额外的错误代码，目前主要是Validation的Code")
    private String code;

    @Schema(title = "额外的错误字段，目前主要是Validation的Field")
    private String field;

    @Schema(title = "错误堆栈信息")
    private StackTraceElement[] stackTrace;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("detail", detail)
                .add("message", message)
                .add("code", code)
                .add("field", field)
                .toString();
    }
}
