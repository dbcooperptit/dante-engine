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

package cn.herodotus.engine.assistant.core.constants;

/**
 * <p>Description: 统一的 Sandbox 管理 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/7 20:36
 */
public class Sandbox {

    /**
     * 支付宝网关地址
     */
    private static final String ALIPAY_PRODUCTION_SERVER_URL = "https://openapi.alipay.com/gateway.do";
    private static final String ALIPAY_SANDBOX_SERVER_URL = "https://openapi.alipaydev.com/gateway.do";

    public static String getAliPayServerUrl(boolean sandbox) {
        return sandbox ? ALIPAY_SANDBOX_SERVER_URL : ALIPAY_PRODUCTION_SERVER_URL;
    }
}
