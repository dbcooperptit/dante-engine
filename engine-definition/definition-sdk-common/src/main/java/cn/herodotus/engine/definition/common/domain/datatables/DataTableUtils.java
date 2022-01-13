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

package cn.herodotus.engine.definition.common.domain.datatables;

import java.util.List;

/**
 * <p>Description: JQuery Datatable组件使用的工具类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2019/11/24 15:49
 */
public class DataTableUtils {

    public static final String ECHO = "sEcho";
    public static final String DISPLAY_START = "iDisplayStart";
    public static final String DISPLAY_LENGTH = "iDisplayLength";
    public static final String QUERY_JSON = "queryJson";
    public static final String DATA = "data";

    public static DataTableResult parseDataTableParameter(List<DataTableParameter> params) {

        String sEcho = null;
        String jsonString = null;
        int iDisplayStart = 0;
        int iDisplayLength = 0;
        for (DataTableParameter param : params) {
            if (param.getName().equals(ECHO)) {
                sEcho = param.getValue().toString();
            }
            if (param.getName().equals(DISPLAY_START)) {
                iDisplayStart = (int) param.getValue();
            }
            if (param.getName().equals(DISPLAY_LENGTH)) {
                iDisplayLength = (int) param.getValue();
            }
            if (param.getName().equals(QUERY_JSON)) {
                jsonString = param.getValue().toString();
            }
        }

        return new DataTableResult(sEcho, iDisplayStart, iDisplayLength, jsonString);
    }
}
