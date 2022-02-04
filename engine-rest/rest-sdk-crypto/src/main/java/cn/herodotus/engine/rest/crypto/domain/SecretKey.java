/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.rest.crypto.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>Description: 秘钥缓存存储实体 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:17
 */
public class SecretKey implements Serializable {

    /**
     * 数据存储身份标识
     */
    private String sessionId;
    /**
     * AES秘钥
     */
    private String aesKey;

    /**
     * 服务器端RSA公钥
     */
    private String publicKeyBase64;

    /**
     * 服务器端RSA私钥
     */
    private String privateKeyBase64;

    /**
     * 创建时间戳
     */
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPrivateKeyBase64() {
        return privateKeyBase64;
    }

    public void setPrivateKeyBase64(String privateKeyBase64) {
        this.privateKeyBase64 = privateKeyBase64;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecretKey secretKey = (SecretKey) o;
        return Objects.equal(sessionId, secretKey.sessionId) && Objects.equal(timestamp, secretKey.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sessionId, timestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sessionId", sessionId)
                .add("aesKey", aesKey)
                .add("publicKeyBase64", publicKeyBase64)
                .add("privateKeyBase64", privateKeyBase64)
                .add("timestamp", timestamp)
                .toString();
    }
}
