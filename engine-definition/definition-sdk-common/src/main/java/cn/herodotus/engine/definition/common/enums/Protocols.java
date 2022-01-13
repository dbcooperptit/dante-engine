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

package cn.herodotus.engine.definition.common.enums;

/**
 * <p>Description: Protocol枚举 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/6/12 14:48
 */
public enum Protocols {
    /**
     * 协议类型
     */
    HTTP("http://", "http"),
    HTTPS("https://", "https");

    private final String format;
    private final String prefix;

    Protocols(String format, String prefix) {
        this.format = format;
        this.prefix = prefix;
    }

    public String getFormat() {
        return format;
    }

    public String getPrefix() {
        return prefix;
    }
}
