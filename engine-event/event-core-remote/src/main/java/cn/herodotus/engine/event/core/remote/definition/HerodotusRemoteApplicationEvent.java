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

package cn.herodotus.engine.event.core.remote.definition;

import cn.herodotus.engine.event.core.remote.processor.DestinationResolver;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * <p>Description: 自定义 Remote Application Event 基础类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/4 15:20
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonIgnoreProperties({"data"})
public class HerodotusRemoteApplicationEvent extends RemoteApplicationEvent {

    /**
     * 时间传递数据
     * <p>
     * 远程事件传递数据，如果传递实体，会经常出现JSON序列化问题。
     * 统一使用JSON 字符串进行传递，减少JSON序列化错误。同时，降低实体引用带来的耦合性。
     */
    private String data;
    /**
     * 发送事件服务名
     */
    private String originService;
    /**
     * 接收事件的服务表达式 {@link Destination}
     */
    private Destination destination;

    public HerodotusRemoteApplicationEvent() {
    }

    public HerodotusRemoteApplicationEvent(String data, String originService, String destinationService) {
        this(data, originService, DestinationResolver.create(destinationService));
    }

    public HerodotusRemoteApplicationEvent(String data, String originService, Destination destination) {
        super(data, originService, destination);
        this.data = data;
        this.originService = originService;
        this.destination = destination;
    }

    public String getData() {
        return data;
    }

    @Override
    public String getOriginService() {
        return originService;
    }

    public Destination getDestination() {
        return destination;
    }
}
