## Web 应用基础支撑模块组件

Web 应用 Rest Client 基础支撑性模块组件。

**包含以下内容：**

1. Okhttp 配置。
2. HttpClient 配置
3. Feign 扩展配置
4. Feign 融合使用 OkHttp 或 HttpClient 配置
5. RestTemplate 配置
6. RestTemplate 融合使用 OkHttp 或 HttpClient 配置
7. OpenApi 配置
8. 自定义 ServiceContext 初始化


### 说明

1. 该模块包含 `RestTemplate` 配置。`RestTemplate` 需要负载均衡处理。`spring-cloud-starter-openfeign` 包中包含了 `spring-cloud-starter-loadbalancer` 引用，所以该包直接引用的 `spring-cloud-starter-openfeign`。
2. OkHttp:
    - 默认让 Feign 使用 OkHttp 或 HttpClient 作为其请求Client。所以直接沿用 Feign 的配置参数来对 OkHttp 或 HttpClient 进行配置。
    - 如果存在 `feign.okhttp.enabled` 配置， 同时其值为 `true`，就会自动配置 OkHttp。
    - 如果存在 `feign.httpclient.enabled` 配置， 同时其值为 `true`，就会自动配置 HttpClient。
    - OkHttp 与 HttpClient 互斥，使用 OkHttp 就不能使用 HttpClient。换句话说： `feign.okhttp.enabled` 设置为 `true` 那么 `feign.httpclient.enabled` 必须设置为 `false` 或者不设置，反之亦然 
    - 在此处配置 OkHttp 和 HttpClient， 也是为了共用 OkHttp 和 HttpClient 的配置，让其可以同时支持 RestTemplate