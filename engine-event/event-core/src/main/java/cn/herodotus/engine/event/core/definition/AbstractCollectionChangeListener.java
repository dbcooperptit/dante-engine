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

package cn.herodotus.engine.event.core.definition;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 实体集合属性变更监听器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/11 18:12
 */
public abstract class AbstractCollectionChangeListener extends AbstractEntityListener {

    private List<String> before;
    private List<String> after;

    public void setBefore(List<String> before) {
        this.before = before;
    }

    public void setAfter(List<String> after) {
        this.after = after;
    }

    protected List<String> getChangedItems() {
        if (CollectionUtils.isNotEmpty(this.before) && CollectionUtils.isNotEmpty(this.after)) {
            return new ArrayList<>(CollectionUtils.disjunction(this.before, this.after));
        }

        if (CollectionUtils.isNotEmpty(this.before) && CollectionUtils.isEmpty(this.after)) {
            return this.before;
        }

        if (CollectionUtils.isEmpty(this.before) && CollectionUtils.isNotEmpty(this.after)) {
            return this.after;
        }

        return new ArrayList<>();
    }
}
