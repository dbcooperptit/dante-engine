## Redis 相关代码组件模块

**包含以下内容：**

1. Redis 配置。
2. 扩展的 Redis Cache Manager 配置
3. 基于 Redis 的 Spring Session 共享配置。通过设置 `spring.session.store-type=redis` 开启。

### 说明

spring session 共享，还支持 mongodb，jdbc，所以暂时将Redis session 共享放置在此包，后续根据实际使用再行调整。