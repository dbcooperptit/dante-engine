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

import cn.herodotus.engine.captcha.core.provider.RandomProvider;

/**
 * <p>Description: 验证码字符类型 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/21 16:26
 */
public enum CaptchaCharacter {

    /**
     * 验证码字母显示类别
     */
    NUM_AND_CHAR(RandomProvider.NUM_MIN_INDEX, RandomProvider.CHAR_MAX_INDEX, "数字和字母混合"),
    ONLY_NUM(RandomProvider.NUM_MIN_INDEX, RandomProvider.NUM_MAX_INDEX, "纯数字"),
    ONLY_CHAR(RandomProvider.CHAR_MIN_INDEX, RandomProvider.CHAR_MAX_INDEX,"纯字母"),
    ONLY_UPPER_CHAR(RandomProvider.UPPER_MIN_INDEX, RandomProvider.UPPER_MAX_INDEX, "纯大写字母"),
    ONLY_LOWER_CHAR(RandomProvider.LOWER_MIN_INDEX, RandomProvider.LOWER_MAX_INDEX,"纯小写字母"),
    NUM_AND_UPPER_CHAR(RandomProvider.NUM_MIN_INDEX, RandomProvider.UPPER_MAX_INDEX,"数字和大写字母");

    /**
     * 字符索引开始位置
     */
    private final int start;
    /**
     * 字符索引结束位置
     */
    private final int end;
    /**
     * 类型说明
     */
    private final String description;

    CaptchaCharacter(int start, int end, String description) {
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }
}
