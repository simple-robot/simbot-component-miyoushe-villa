# Bot配置文件

<tldr>
<p>在使用 <b>Spring Boot</b> 时自动注册 bot 所需的配置文件。</p>
</tldr>

## 示例

```json
{
    "component": "simbot.villa",
    "ticket": {
        "botId": "你的botId",
        "botSecret": "你的botSecret"
    }
}
```
{collapsible="true" default-state="expanded" collapsed-title="简单示例"}

```json
{
    "component": "simbot.villa",
    "ticket": {
        "botId": "你的botId",
        "botSecret": "你的botSecret"
    },
    "config": {
        "loginVillaId": "0",
        "loginMeta": null,
        "loginRegion": null,
        "timeout": null
    }
}
```
{collapsible="true" default-state="collapsed" collapsed-title="完整示例"}

## 属性描述

<deflist>
<def title="component">

固定值：`simbot.villa`

</def>
<def title="ticket">

bot用于登录的票据信息，必填。

<deflist style="wide">
<def title="botId">

`String`

bot开发配置中的 `bot_id`

</def>
<def title="botSecret">

`String`

bot开发配置中的 `secret`

</def>
</deflist>

</def>

<def title="config">
可选项，提供一些额外的可配置属性。

<deflist>
<def title="loginVillaId">

`String`

创建连接（获取 ws 连接信息）时提供给 API 的 token 中的 `villa_id` 属性。

机器人未上线时，`villa_id` 使用测试别野，上线后可传 `0`。

默认为 `0`。

</def>
<def title="loginMeta">

`Map<String, String>?`

应用在 `PLogin.meta` 上的属性。

默认为 `null`。

</def>
<def title="loginRegion">

`String?`

应用在 `PLogin.region` 上的属性。如果为 `null` 则会使用一个随机值。

默认为 `null`。

</def>
<def title="timeout" style="wide">

`TimeoutConfig?`

与部分超时相关的配置信息。
当任意属性不为 `null` 时会为 bot 中用于请求API的 `HttpClient`
配置 [HttpTimeout][HttpTimeout] 插件。

默认为 `null`。

<deflist>
<def title="apiHttpRequestTimeoutMillis">

`Long?`

API请求中的超时请求配置。参考 [HttpTimeout][HttpTimeout] 中的相关说明。

默认为 `null`。

</def>
<def title="apiHttpConnectTimeoutMillis">

`Long?`

API请求中的超时请求配置。参考 [HttpTimeout][HttpTimeout] 中的相关说明。

默认为 `null`。

</def>
<def title="apiHttpSocketTimeoutMillis">

`Long?`

API请求中的超时请求配置。参考 [HttpTimeout][HttpTimeout] 中的相关说明。

默认为 `null`。

</def>
</deflist>

</def>
</deflist>

</def>

</deflist>

[HttpTimeout]: https://ktor.io/docs/timeout.html
