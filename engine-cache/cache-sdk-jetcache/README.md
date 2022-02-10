## JetCache 相关代码组件模块

**包含以下内容：**
1. JetCache 配置。
2. 基于 JetCache 的，Mybatis 二级缓存扩展。
3. 自研 JetCache 缓存手动创建工具类
4. 签章(Stamp)管理定义类

### 额外说明

> 包含了一些 JetCache 已有包的代码。与 JetCache 原有代码略有不同，进行了一定的修改，主要解决 JetCache 在 Spring Boot 2.6 环境下循环注入的问题。已向 JetCache 提出了 Issue， 需要等 JetCache 下个大版本发布，解决该问题以后，再进行删除。