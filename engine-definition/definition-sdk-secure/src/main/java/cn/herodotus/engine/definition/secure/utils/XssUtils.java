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

package cn.herodotus.engine.definition.secure.utils;

import cn.herodotus.engine.definition.common.utils.ResourceUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * <p>Description: Antisamy 单例 工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/29 16:15
 */
public class XssUtils {

    private static final Logger log = LoggerFactory.getLogger(XssUtils.class);

    private static volatile XssUtils INSTANCE;
    private final AntiSamy antiSamy;
    private final String nbsp;
    private final String quot;

    private XssUtils() {
        Policy policy = createPolicy();
        this.antiSamy = ObjectUtils.isNotEmpty(policy) ? new AntiSamy(policy) : new AntiSamy();
        this.nbsp = cleanHtml("&nbsp;");
        this.quot = cleanHtml("\"");
    }

    private static XssUtils getInstance() {
        if (ObjectUtils.isEmpty(INSTANCE)) {
            synchronized (XssUtils.class) {
                if (ObjectUtils.isEmpty(INSTANCE)) {
                    INSTANCE = new XssUtils();
                }
            }
        }

        return INSTANCE;
    }

    private Policy createPolicy() {
        try {
            URL url = ResourceUtils.getURL("classpath:antisamy/antisamy-anythinggoes.xml");
            return Policy.getInstance(url);
        } catch (IOException | PolicyException e) {
            log.warn("[Herodotus] |- Antisamy create policy error! {}", e.getMessage());
            return null;
        }
    }

    private CleanResults scan(String taintedHtml) throws ScanException, PolicyException {
        return antiSamy.scan(taintedHtml);
    }

    private String cleanHtml(String taintedHtml) {
        try {
            log.trace("[Herodotus] |- Before Antisamy Scan, value is: [{}]", taintedHtml);

            // 使用AntiSamy清洗数据
            final CleanResults cleanResults = scan(taintedHtml);
            String result = cleanResults.getCleanHTML();
            log.trace("[Herodotus] |- After  Antisamy Scan, value is: [{}]", result);
            return result;
        } catch (ScanException | PolicyException e) {
            log.error("[Herodotus] |- Antisamy scan catch error! {}", e.getMessage());
            return taintedHtml;
        }
    }

    public static String cleaning(String taintedHTML) {
        // 对转义的HTML特殊字符（<、>、"等）进行反转义，因为AntiSamy调用scan方法时会将特殊字符转义
        String cleanHtml = StringEscapeUtils.unescapeHtml4(getInstance().cleanHtml(taintedHTML));
        //AntiSamy会把“&nbsp;”转换成乱码，把双引号转换成"&quot;" 先将&nbsp;的乱码替换为空，双引号的乱码替换为双引号
        String temp = cleanHtml.replaceAll(getInstance().nbsp, "");
        String result = temp.replaceAll(getInstance().quot, "\"");
        log.trace("[Herodotus] |- After  Antisamy Well Formed, value is: [{}]", result);
        return result;
    }
}
