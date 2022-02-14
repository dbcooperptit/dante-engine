<p align="center"><img src="./documents/readme/images/logo.png" height="150" width="150" alt="logo"/></p>
<h2 align="center">简洁优雅 · 稳定高效 | 宁静致远 · 精益求精 </h2>
<p align="center">Herodotus Engine 基于 Spring Boot 2.X， 是 Eurynome Cloud 微服务架构内核核心组件库，可用于任何 Spring Boot 工程</p>

---

<p align="center">
    <a href="https://www.oracle.com/java/technologies/javase-downloads.html" target="_blank"><img src="https://shields.io/badge/JDK-1.8%2B-green" alt="JDK 1.8+"></a>
    <a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://shields.io/badge/Spring%20Boot-2.6.2-blue" alt="Spring Boot 2.6.2"></a>
    <a href="https://spring.io/projects/spring-cloud" target="_blank"><img src="https://shields.io/badge/Spring%20Cloud-2021.0.0-blue" alt="Spring Cloud 2021.0.0"></a>
    <a href="https://github.com/alibaba/spring-cloud-alibaba" target="_blank"><img src="https://shields.io/badge/Spring%20Cloud%20Alibaba-2021.1-blue" alt="Spring Cloud Alibaba 2021.1"></a>
    <a href="https://nacos.io/zh-cn/index.html" target="_blank"><img src="https://shields.io/badge/Nacos-2.0.3-brightgreen" alt="Nacos 2.0.3"></a>
    <a href="./LICENSE"><img src="https://shields.io/badge/License-Apache--2.0-blue" alt="License Apache 2.0"></a>
    <a href="https://blog.csdn.net/Pointer_v" target="_blank"><img src="https://shields.io/badge/Author-%E7%A0%81%E5%8C%A0%E5%90%9B-orange" alt="码匠君"></a>
    <a href="#" target="_blank"><img src="https://shields.io/badge/Version-2.7.0.Beta2-red" alt="Version 2.7.0.Beta2"></a>
</p>

<p align="center">
    <a href="https://gitee.com/herodotus/eurynome-cloud">Eurynome Cloud</a> &nbsp; | &nbsp;
    <a href="https://www.herodotus.cn">文档</a>
</p>

## 背景

> 2021年11月8日 Spring 官方已经强烈建议使用 `Spring Authorization Server` 替换已经过时的 `Spring Security OAuth2.0`。距离 `Spring Security OAuth2.0` 结束生命周期还有小半年的时间，所以准备用 `Spring Authorization Server` 对已有的 `Eurynome Cloud` 微服务架构进行升级。

`Eurynome Cloud` 微服务架构，一直遵循“高内聚、低耦合”的原则，在开发和维护的过程中不断优化已有代码，尽一切可能降低代码的耦合性。但是，毕竟所有的代码都堆积在同一个工程中，代码间的过度依赖和互相耦合还是较为严重。这为 `Spring Authorization Server` 替换 `Spring Security OAuth2.0` 带来了较大的阻力和难度。如果完全推翻现有代码，基于 `Spring Authorization Server` 重新构建系统，投入成本太大而且是一种极大的浪费；在现有工程中直接改造，由于代码间的耦合，改造过程也是困难重重。

最终，采取了一个折中的方案：对现有的 `Eurynome Cloud` 微服务架构，来一次深度的“庖丁解牛”，将一个完整的微服务架构，根据涉及组件的职责以及用途，拆解为多个细化的、各自独立组件模块，在最大程度上降低代码间的耦合。那么在使用 `Spring Authorization Server` 进行改造时影响和涉及的代码量将会极大地降低。因此，就有了本项目。

## 特点

1. 严格遵照“单一职责”原则，进行各个模块的划分和代码拆解。
2. 严格遵循 Spring Boot 编码规则和命名规则。
3. 大多数模块均支持 @EnableXXX注解 和 starter，让 Spring Bean 的注入顺序更加可控。
4. 模块化设计思想，通过 Bean 注入、以及丰富的自定义 @ConditionalXXX 注解，让模块的添加和删除更加灵活。
5. 各模块既可以综合在一起使用，也可以在其它 Spring Boot 工程中独立使用。

## 优点

很多朋友不理解这样做的好处，明明很多代码都可以放在一起，为什么要拆分出这么多包、拆这么细？

这样做主要有以下优势：

1. 虽然模块看似很多，但是每个模块职责单一、代码清晰，更有利于聚焦和定位问题。
2. 通过对微服务架构的“庖丁解牛”，初学者不再需要在代码的海洋里“遨游”，通过针对性地了解各个模块，以点带面快速掌握微服务架构整体结构。
3. 模块间的依赖极大的降低，想要替换为 `Spring Authorization Server`，影响到的代码和范围将会很小。该工程也是使用 `Spring Authorization Server` 的前序工作
4. 每个模块均是最小化依赖第三包，规避依赖包过度依赖，特别是 starter 过多依赖，导致不可预知、难以调试、不好修改等问题。
5. 降低微服务系统代码量，独立组件可提前编译并上传至Maven仓库，降低工程代码编译耗时，改进 CICD 效率。

## 工程结构

```
herodotus-engine
├── dependencies -- 工程Maven顶级依赖，统一控制版本和依赖
├── documents -- 需要放置的文档位置
├    └── readme -- README 相关素材放置目录
├── engine-assistant -- 基础通用代码包
├    ├── assistant-core -- 全局共性通用代码
├    ├── assistant-sdk-jackson -- Jackson 组件
├    ├── assistant-sdk-secure -- Xss 和 SQL 注入等安全组件
├    └── assistant-spring-boot-starter -- Assistant  模块统一 Starter
├── engine-cache -- 缓存模块
├    ├── cache-core -- 缓存通用代码
├    ├── cache-layer-spring-boot-starter -- 自研多级缓存 Starter
├    ├── cache-sdk-jetcache -- JetCache 组件相关代码模块
├    ├── cache-sdk-layer -- 自研多级缓存组件相关代码模块
├    ├── cache-sdk-redisson -- Redisson 组件相关代码模块
├    └── cache-spring-boot-starter -- Cache  模块统一 Starter
├── engine-captcha -- 验证码模块
├    ├── captcha-core -- 验证码共性通用代码
├    ├── captcha-sdk-behavior -- 行为验证码组件（包括拼图滑块、文字点选）
├    ├── captcha-sdk-graphic -- 传统图形验证码组件（包括算数类型、中文类型、字母类型、GIF类型）
├    ├── captcha-sdk-hutool -- Hutool验证码组件（包括圆圈干扰、扭曲干扰、线段干扰）
├    └── captcha-spring-boot-starter -- Captcha  模块统一 Starter
├── engine-data -- 数据访问模块
├    ├── data-core -- 数据访问共性通用代码
├    ├── data-sdk-jpa -- JPA 及Hibernate 组件相关代码模块
├    ├── data-sdk-mybatis-plus -- MybatisPlus 组件相关代码模块
├    ├── data-sdk-p6spy -- P6spy 组件相关代码模块
├    └── data-spring-boot-starter -- Data 模块统一 Starter
├── engine-event -- Spring 事件模块
├    ├── event-core-local -- Spring 标准事件组件相关代码模块
├    ├── event-core-remote -- 基于 Spring Cloud Bus 的远程事件组件相关代码模块
├    ├── event-pay-spring-boot-starter -- 支付事件统一 Starter
├    ├── event-sdk-pay -- 支付事件组件相关代码模块
├    ├── event-sdk-security -- 安全事件组件相关代码模块
├    └── event-security-spring-boot-starter --安全事件统一 Starter
├── engine-facility -- 微服务基础设施模块
├    ├── facility-core -- 基础设施共性通用代码
├    ├── facility-sdk-log -- 微服务日志中心组件相关代码模块
├    ├── facility-sdk-sentinel -- Sentinel 组件相关代码模块
├    └── facility-spring-boot-starter -- Facility 模块统一 Starter
├── engine-message -- 消息模块
├    ├── message-core -- 消息共性通用代码
├    └── message-spring-boot-starter -- Message  模块统一 Starter
├── engine-oss -- 对象存储模块
├    ├── oss-core -- 对象存储共性通用代码
├    ├── oss-sdk-minio -- Minio 组件相关代码模块
├    └── oss-spring-boot-starter -- Oss 模块统一 Starter
├── engine-pay -- 支付模块
├    ├── pay-core -- 支付共性通用代码
├    ├── pay-sdk-alipay -- 支付宝支付组件相关代码模块
├    ├── pay-sdk-all -- 支付方式整合组件相关代码模块
├    ├── pay-sdk-wxpay -- 微信支付组件相关代码模块
├    └── pay-spring-boot-starter -- Pay 模块统一 Starter
├── engine-rest -- 服务Rest接口模块
├    ├── rest-core -- 服务Rest接口共性通用代码
├    ├── rest-sdk-crypto -- 前后端数据加密组件相关代码模块
├    ├── rest-sdk-secure -- 接口幂等、防刷、Xss和SQL注入防护组件相关代码模块
├    └── rest-spring-boot-starter -- Rest 模块统一 Starter(包括通用CRUD代码)
├── engine-security -- Security & OAuth2 安全模块
├    ├── security-core -- 安全模块共性通用代码
├    ├── security-sdk-authorize -- OAuth2 授权码模式扩展组件相关代码模块
├    ├── security-sdk-extend -- Security 扩展组件相关代码模块
├    └──security-sdk-log -- OAuth2 认证后操作信息记录组件相关代码模块
├── engine-temporal -- 时序数据存储处理模块
├    ├── temporal-core -- 时序数据存储共性通用代码
├    ├── temporal-sdk-influxdb -- Influxdb 组件相关代码模块
├    └── temporal-spring-boot-starter -- Temporal 模块统一 Starter
├── engine-web -- Web处理模块
├    ├── web-core -- Web共性通用代码
├    ├── web-sdk-rest -- Web 通用配置组件相关代码模块
├    ├── web-sdk-scan -- 接口权限扫描组件相关代码模块
├    └── web-spring-boot-starter -- Web 模块统一 Starter
├── engine-websocket -- Websocket模块
├    ├── websocket-core -- Websocket模块共性通用代码
├    ├── websocket-sdk-accelerator -- Websocket基础逻辑组件相关代码模块
└──  └── websocket-spring-boot-starter -- Websocket 模块统一 Starter
```

## 阅读顺序

### 一、关联性阅读

部分组件存在关联和组合性，建议按照以下顺序阅读和了解代码：

1. engine-assistant
2. engine-cache
3. engine-data
4. engine-web
5. engine-rest
6. engine-security
7. engine-facility
8. engine-event
9. engine-message

### 二、独立性阅读

部分组件都是相对独立的，组件间的关联性非常弱。可分开独立阅读和了解代码：

* engine-captcha
* engine-oss
* engine-pay
* engine-temporal
* engine-websocket

## 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

## 交流反馈

- 欢迎提交[ISSUS](https://gitee.com/herodotus/eurynome-cloud/issues) ，请写清楚问题的具体原因，重现步骤和环境(上下文)

## 关联项目

- Eurynome Cloud 后端微服务工程：[https://gitee.com/herodotus/eurynome-cloud](https://gitee.com/herodotus/eurynome-cloud)
- Eurynome Cloud 单体版示例工程：[https://gitee.com/herodotus/eurynome-cloud-athena](https://gitee.com/herodotus/eurynome-cloud-athena)
- Eurynome Cloud 前端功能：[https://gitee.com/herodotus/eurynome-cloud-ui](https://gitee.com/herodotus/eurynome-cloud-ui)

