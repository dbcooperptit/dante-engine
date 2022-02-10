<p align="center"><img src="./documents/readme/images/logo.png" height="150" width="150" alt="logo"/></p>
<h2 align="center">简洁优雅 · 稳定高效 | 宁静致远 · 精益求精 </h2>
<h3 align="center">Herodotus Engine 基于 Spring Boot 2.X， 是 Eurynome Cloud 微服务架构基础内核模块公共组件库，可用于任何 Spring Boot 工程</h3>

---

<p align="center">
    <a href="https://www.oracle.com/java/technologies/javase-downloads.html" target="_blank"><img src="https://shields.io/badge/JDK-1.8%2B-green" alt="JDK 1.8+"></a>
    <a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://shields.io/badge/Spring%20Boot-2.6.3-blue" alt="Spring Boot 2.6.3"></a>
    <a href="https://spring.io/projects/spring-cloud" target="_blank"><img src="https://shields.io/badge/Spring%20Cloud-2021.0.0-blue" alt="Spring Cloud 2021.0.0"></a>
    <a href="https://github.com/alibaba/spring-cloud-alibaba" target="_blank"><img src="https://shields.io/badge/Spring%20Cloud%20Alibaba-2021.1-blue" alt="Spring Cloud Alibaba 2021.1"></a>
    <a href="https://nacos.io/zh-cn/index.html" target="_blank"><img src="https://shields.io/badge/Nacos-2.0.4-brightgreen" alt="Nacos 2.0.4"></a>
    <a href="./LICENSE"><img src="https://shields.io/badge/License-Apache--2.0-blue" alt="License Apache 2.0"></a>
    <a href="https://blog.csdn.net/Pointer_v" target="_blank"><img src="https://shields.io/badge/Author-%E7%A0%81%E5%8C%A0%E5%90%9B-orange" alt="码匠君"></a>
    <a href="#" target="_blank"><img src="https://shields.io/badge/Version-2.6.3.20-red" alt="Version 2.6.3.20"></a>
    <a href="https://gitee.com/herodotus/eurynome-cloud"><img src="https://gitee.com/herodotus/eurynome-cloud/badge/star.svg?theme=dark" alt="Gitee star"></a>
    <a href="https://gitee.com/herodotus/eurynome-cloud"><img src="https://gitee.com/herodotus/eurynome-cloud/badge/fork.svg?theme=dark" alt="Gitee fork"></a>
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




