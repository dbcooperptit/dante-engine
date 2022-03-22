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

package cn.herodotus.engine.oauth2.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: OAuth2 Signature </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/3/2 16:15
 */
@Schema(name = "签名算法")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Signature {

    /**
     * enum
     */
    RS256(0,"RS256"),
    RS384(1,"RS384"),
    RS512(2,"RS512"),
    ES256(3,"ES256"),
    ES384(4,"ES384"),
    ES512(5,"ES512"),
    PS256(6,"PS256"),
    PS384(7,"PS384"),
    PS512(8,"PS512");

    @Schema(name = "索引")
    private final Integer index;
    @Schema(name = "文字")
    private final String algorithm;

    private static final Map<Integer, Signature> indexMap = new HashMap<>();
    private static final List<Map<String, Object>> toJsonStruct = new ArrayList<>();

    static {
        for (Signature signature : Signature.values()) {
            indexMap.put(signature.ordinal(), signature);
            toJsonStruct.add(signature.ordinal(),
                    ImmutableMap.<String, Object>builder()
                            .put("value", signature.ordinal())
                            .put("key", signature.name())
                            .put("text", signature.getAlgorithm())
                            .build());
        }
    }

    Signature(Integer index, String algorithm) {
        this.index = index;
        this.algorithm = algorithm;
    }

    /**
     * 不加@JsonValue，转换的时候转换出完整的对象。
     * 加了@JsonValue，只会显示相应的属性的值
     * <p>
     * 不使用@JsonValue @JsonDeserializer类里面要做相应的处理
     *
     * @return Enum索引
     */
    @JsonValue
    public Integer getIndex() {
        return index;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public static Signature getSignatureAlgorithm(Integer type) {
        return indexMap.get(type);
    }

    public static List<Map<String, Object>> getToJsonStruct() {
        return toJsonStruct;
    }
}
