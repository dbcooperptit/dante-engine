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

package cn.herodotus.engine.oss.minio.domain;

import com.google.common.base.MoreObjects;
import io.minio.StatObjectResponse;

import java.util.Date;

/**
 * <p>Description: TODO </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 15:58
 */
public class MinioObject {

    private String bucketName;
    private String name;
    private Date lastModified;
    private long length;
    private String etag;
    private String contentType;

    public MinioObject(String bucketName, String name, Date lastModified, long length, String etag, String contentType) {
        this.bucketName = bucketName;
        this.name = name;
        this.lastModified = lastModified;
        this.length = length;
        this.etag = etag;
        this.contentType = contentType;
    }

    public MinioObject(StatObjectResponse os) {
        this.bucketName = os.bucket();
        this.name = os.object();
        this.lastModified = Date.from(os.lastModified().toInstant());
        this.length = os.size();
        this.etag = os.etag();
        this.contentType = os.contentType();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bucketName", bucketName)
                .add("name", name)
                .add("lastModified", lastModified)
                .add("length", length)
                .add("etag", etag)
                .add("contentType", contentType)
                .toString();
    }
}
