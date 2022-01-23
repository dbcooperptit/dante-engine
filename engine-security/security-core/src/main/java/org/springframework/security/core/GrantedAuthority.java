/*
 * Copyright (c) 2019-2021 Gengwei Zheng (herodotus@aliyun.com)
 *
 * Project Name: herodotus-cloud
 * Module Name: herodotus-engine-security
 * File Name: GrantedAuthority.java
 * Author: gengwei.zheng
 * Date: 2021/09/03 22:11:03
 */

package org.springframework.security.core;

import cn.herodotus.engine.security.core.definition.domain.HerodotusGrantedAuthority;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * <p>Description: 重新定义GrantedAuthority支持序列化 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/3 22:11
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@clazz")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HerodotusGrantedAuthority.class, name = "HerodotusGrantedAuthority")
})
public interface GrantedAuthority extends Serializable {

    /**
     * f the GrantedAuthority can be represented as a String and that String is sufficient in precision to be relied upon for an access control decision by an AccessDecisionManager (or delegate), this method should return such a String.
     * If the GrantedAuthority cannot be expressed with sufficient precision as a String, null should be returned. Returning null will require an AccessDecisionManager (or delegate) to specifically support the GrantedAuthority implementation, so returning null should be avoided unless actually required.
     *
     * @return a representation of the granted authority (or null if the granted authority cannot be expressed as a String with sufficient precision).
     */
    String getAuthority();
}
