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

package cn.herodotus.engine.security.core.definition.service;

import cn.herodotus.engine.assistant.core.domain.AccessPrincipal;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>Description: 自定义UserDetailsService接口，方便以后扩展 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/1/17 12:49
 */
public interface HerodotusUserDetailsService extends UserDetailsService {

    /**
     * 通过社交集成的唯一id，获取用户信息
     * <p>
     * 如果是短信验证码，openId就是手机号码
     *
     * @param accessPrincipal 社交登录提供的相关信息
     * @param source          社交集成提供商类型
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户不存在
     */
    UserDetails loadUserBySocial(String source, AccessPrincipal accessPrincipal) throws AuthenticationException;
}
