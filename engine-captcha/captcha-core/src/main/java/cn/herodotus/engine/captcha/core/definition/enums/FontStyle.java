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

package cn.herodotus.engine.captcha.core.definition.enums;

import java.awt.*;

/**
 * <p>Description: 字体风格 </p>
 *
 * 定义此类的目的：
 * 1. 设置字体风格和字体大小，最初都是使用int类型参数，很容混淆出错，增加个枚举类型以示区别
 * 2. 枚举类型让配置参数配置更便捷。
 *
 * @author : gengwei.zheng
 * @date : 2021/12/23 10:33
 */
public enum FontStyle {

    /**
     * 字体风格
     */
    PLAIN(Font.PLAIN),
    BOLD(Font.BOLD),
    ITALIC(Font.ITALIC);

    private final int mapping;

    FontStyle(int mapping) {
        this.mapping = mapping;
    }

    public int getMapping() {
        return mapping;
    }
}
