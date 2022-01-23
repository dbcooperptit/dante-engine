/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2022 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.security.core.definition.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.security.core.GrantedAuthority;

/**
 * <p>Description: 自定义 GrantedAuthority</p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/3 22:13
 */
@JsonTypeName(value = "HerodotusGrantedAuthority")
public class HerodotusGrantedAuthority implements GrantedAuthority {

    public HerodotusGrantedAuthority() {
    }

    public HerodotusGrantedAuthority(String authority) {
        this.authority = authority;
    }

    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HerodotusGrantedAuthority that = (HerodotusGrantedAuthority) o;
        return Objects.equal(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authority);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("authority", authority)
                .toString();
    }
}
