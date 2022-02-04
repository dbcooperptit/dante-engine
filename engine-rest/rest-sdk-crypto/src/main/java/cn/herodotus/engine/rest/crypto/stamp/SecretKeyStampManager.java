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

package cn.herodotus.engine.rest.crypto.stamp;

import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.herodotus.engine.rest.core.constants.RestConstants;
import cn.herodotus.engine.rest.crypto.domain.SecretKey;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 数据加密秘钥缓存 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/9/30 18:14
 */
public class SecretKeyStampManager extends AbstractStampManager<String, SecretKey> {

    private static final Logger log = LoggerFactory.getLogger(SecretKeyStampManager.class);

    @CreateCache(name = RestConstants.CACHE_NAME_TOKEN_SECURE_KEY, cacheType = CacheType.BOTH)
    protected Cache<String, SecretKey> cache;

    @Override
    protected Cache<String, SecretKey> getCache() {
        return this.cache;
    }

    @Override
    public SecretKey nextStamp(String key) {
        // 生成 AES 秘钥
        String aesKey = RandomUtil.randomStringUpper(16);

        // 生成 RSA 公钥和私钥
        RSA rsa = SecureUtil.rsa();

        SecretKey secretKey = new SecretKey();
        secretKey.setSessionId(key);
        secretKey.setAesKey(aesKey);
        secretKey.setPrivateKeyBase64(rsa.getPrivateKeyBase64());
        secretKey.setPublicKeyBase64(rsa.getPublicKeyBase64());

        log.debug("[Herodotus] |- Generate secret key, value is : [{}]", secretKey);
        return secretKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
