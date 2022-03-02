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

package cn.herodotus.engine.oauth2.manager.entity;

import cn.herodotus.engine.data.core.entity.BaseSysEntity;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * <p> Description : Oauth Scope </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/3/19 14:15
 */
@Entity
@Table(name = "oauth2_scope", uniqueConstraints = {@UniqueConstraint(columnNames = {"scope_code"})}, indexes = {
        @Index(name = "oauth2_scope_id_idx", columnList = "scope_id"),
        @Index(name = "oauth2_scope_code_idx", columnList = "scope_code")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = OAuth2Constants.REGION_OAUTH2_SCOPE)
public class OAuth2Scope extends BaseSysEntity implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "scope_id", length = 64)
    private String scopeId;

    @Column(name = "scope_code", length = 128, unique = true)
    private String scopeCode;

    @Column(name = "scope_name", length = 128)
    private String scopeName;

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(String scopeCode) {
        this.scopeCode = scopeCode;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String getAuthority() {
        return this.getScopeCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OAuth2Scope that = (OAuth2Scope) o;

        return new EqualsBuilder()
                .append(getScopeId(), that.getScopeId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getScopeId())
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("scopeId", scopeId)
                .add("scopeCode", scopeCode)
                .add("scopeName", scopeName)
                .toString();
    }
}
