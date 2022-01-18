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

package cn.herodotus.engine.captcha.graphic.renderer;

import cn.herodotus.engine.assistant.core.utils.RegexPool;
import cn.herodotus.engine.captcha.graphic.definition.AbstractBaseGraphicRenderer;
import cn.herodotus.engine.captcha.core.definition.domain.Metadata;
import cn.herodotus.engine.captcha.core.definition.enums.CaptchaCategory;
import cn.herodotus.engine.captcha.core.provider.RandomProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.image.BufferedImage;

/**
 * <p>Description: 算数类型一般验证码 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/20 22:55
 */
@Component
public class ArithmeticCaptchaRenderer extends AbstractBaseGraphicRenderer {

    private static final Logger log = LoggerFactory.getLogger(ArithmeticCaptchaRenderer.class);

    private int complexity = 2;
    /**
     * 计算结果
     */
    private String computedResult;

    @Override
    public String getCategory() {
        return CaptchaCategory.ARITHMETIC.getConstant();
    }

    @Override
    protected String getBase64ImagePrefix() {
        return BASE64_PNG_IMAGE_PREFIX;
    }

    @Override
    protected String[] getDrawCharacters() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < complexity; i++) {
            builder.append(RandomProvider.randomInt(10));
            if (i < complexity - 1) {
                int type = RandomProvider.randomInt(1, 4);
                if (type == 1) {
                    builder.append("+");
                } else if (type == 2) {
                    builder.append("-");
                } else if (type == 3) {
                    builder.append("x");
                }
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        try {
            computedResult = String.valueOf(engine.eval(builder.toString().replaceAll("x", "*")));
        } catch (ScriptException e) {
            log.error("[Herodotus] |- Arithmetic png captcha eval expression error！", e);
        }

        builder.append("=?");

        String result = builder.toString();
        return result.split(RegexPool.ALL_CHARACTERS);
    }

    @Override
    public Metadata draw() {
        String[] drawContent = getDrawCharacters();
        BufferedImage bufferedImage = createArithmeticBufferedImage(drawContent);

        Metadata metadata = new Metadata();
        metadata.setGraphicImageBase64(toBase64(bufferedImage));
        metadata.setCharacters(this.computedResult);
        return metadata;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.complexity = this.getCaptchaProperties().getGraphics().getComplexity();
    }
}
