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

package cn.herodotus.engine.captcha.behavior.renderer;

import cn.herodotus.engine.assistant.core.constants.SymbolConstants;
import cn.herodotus.engine.captcha.core.definition.domain.Coordinate;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Description: 文字点选信息混淆器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/17 12:19
 */
public class WordClickObfuscator {
    /**
     * 文字点选验证码文字坐标信息列表
     */
    private final List<Coordinate> coordinates;
    /**
     * 文字点选验证码校验文字
     */
    private final List<String> words;

    private String wordString;

    public WordClickObfuscator(List<String> originalWords, List<Coordinate> originalCoordinates) {
        this.coordinates = new ArrayList<>();
        this.words = new ArrayList<>();
        this.execute(originalWords, originalCoordinates);
    }

    private void execute(List<String> originalWords, List<Coordinate> originalCoordinates) {

        int[] indexes = RandomUtil.randomInts(originalWords.size());

        Arrays.stream(indexes).forEach(value -> {
            this.words.add(this.words.size(), originalWords.get(value));
            this.coordinates.add(this.coordinates.size(), originalCoordinates.get(value));
        });

        this.wordString = StringUtils.join(getWords(), SymbolConstants.COMMA);
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public List<String> getWords() {
        return words;
    }

    public String getWordString() {
        return this.wordString;
    }
}
