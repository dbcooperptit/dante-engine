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

package cn.herodotus.engine.assistant.core.definition.event;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * <p>Description: 策略 Event 定义 </p>
 * <p>
 * 为了同时支持 分布式模式 和 单体式模式。所以很多事件均需要同时支持本地发送和远程发送两种模式。
 * 抽象本定义用于统一处理两种模式的事件支持。
 *
 * @author : gengwei.zheng
 * @date : 2022/2/5 15:32
 */
public interface StrategyEvent<T> {

    /**
     * 获取 Spring 应用上下文
     *
     * @return {@link ApplicationContext}
     */
    ApplicationContext getApplicationContext();

    /**
     * 创建本地事件
     *
     * @param data 事件携带数据
     * @return 事件对象 {@link ApplicationEvent}
     */
    ApplicationEvent createLocalEvent(T data);

    /**
     * 创建远程事件
     *
     * @param data               事件携带数据。JSON 格式的数据。
     * @param originService      发送远程事件原始服务
     * @param destinationService 接收远程事件目的地
     * @return 事件对象 {@link ApplicationEvent}
     */
    ApplicationEvent createRemoteEvent(String data, String originService, String destinationService);

    /**
     * 发送远程事件还是本地事件条件。
     *
     * @return true 发送远程事件，false 发送本地事件
     */
    boolean isRemote();

    /**
     * 发送事件
     *
     * @param data               事件携带数据
     * @param originService      发送远程事件原始服务
     * @param destinationService 接收远程事件目的地
     */
    default void postProcess(T data, String originService, String destinationService) {
        if (!isRemote()) {
            getApplicationContext().publishEvent(createLocalEvent(data));
        } else {
            getApplicationContext().publishEvent(createRemoteEvent(JSON.toJSONString(data), originService, destinationService));
        }
    }
}
