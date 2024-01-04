---
switcher-label: JavaAPI风格
---

<!--suppress XmlDeprecatedElement -->
<var name="jb" value="阻塞"/>
<var name="ja" value="异步"/>

# 使用API


<tldr>
本章节介绍如何使用 API 模块来构建、请求一个米游社大别野的API。
</tldr>

<note>
<include from="snippets.md" element-id="doc-desc-version" />
</note>

## 安装

<include from="snippets.md" element-id="component-install">
    <var name="name" value="api" />
    <var name="maven-name-suffix" value="-jvm" />
</include>

然后选择一个合适的 Ktor 引擎。

<include from="snippets.md" element-id="engine-choose"></include>

## 使用

<tooltip term="大别野组件">大别野组件</tooltip> 的API模块提供了针对
[米游社大别野API](https://webstatic.mihoyo.com/vila/bot/doc/) 的基本对应封装。

API封装的命名与API具有一定关联，例如 [`获取大别野信息`](https://webstatic.mihoyo.com/vila/bot/doc/villa_api/get_villa.html)：

<compare first-title="API" second-title="API封装" style="top-bottom">

```HTTP
GET /vila/api/bot/platform/getVilla
```

```
love.forte.simbot.miyoushe.api.villa.GetVillaApi
```
</compare>

> 所有的API实现均在包路径 `love.forte.simbot.miyoushe.api` 中。

API的应用大差不差，因此此处仅使用部分类型作为示例，
不会演示所有API。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
// 准备 bot 的必要信息
val botId = "bot_xxxx"
val botSecret = "xxxx"
// 大别野ID，请求时作为token的一员。
val villaId = "1234"
// 构建请求用的 token
val token = MiyousheVillaApiToken(botId, botSecret, villaId)

// 准备一个用于 HTTP 请求的 [[[HttpClient|https://ktor.io/docs/create-client.html]]]
// 如果使用 JVM，你需要确保 classpath 环境中添加了合适的 [[[引擎|https://ktor.io/docs/http-client-engines.html]]]
// 如果使用其他平台，例如 JS 或 native，则可能需要添加了合适的引擎依赖后手动填入它们，以 mingwx64 平台为例：`HttpClient(WinHttp)`。
val httpClient = HttpClient()

// 构建需要请求的 API 示例，此处为 [[[获取房间信息|https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_room.html]]] API
val roomId: ULong = 1234u // 假设房间ID是 1234
val api = GetRoomApi.create(roomId)

// 发起请求并得到结果。在 API 模块中有三种请求方式（均为挂起函数）：
// 1️⃣ 使用 api.request, 得到 Ktor 进行 HTTP 请求的原始响应类型 HttpResponse
val response: HttpResponse = api.request(httpClient, token)

// 2️⃣ 使用 api.requestResult, 得到请求结果的统一响应体结构。假如此响应代表成功(result.retcode == 0), 则可以通过 result.data 获取响应结果。
val result: ApiResult<GetRoomResult> = api.requestResult(httpClient, token)

// 3️⃣ 使用 api.requestData, 得到当响应体代表成功时的响应数据。如果响应体不为成功则会抛出异常。
val requestData: GetRoomResult = api.requestData(httpClient, token)
```

</tab>
<tab title="Java" group-key="Java">

```java
// 准备 bot 的必要信息
var botId = "bot_xxxx";
var botSecret = "xxxx";
// 大别野ID，请求时作为token的一员。
var villaId = "1234";
// 构建请求用的 token
var token = new MiyousheVillaApiToken(botId, botSecret, villaId);

// 准备一个用于 HTTP 请求的 [[[HttpClient|https://ktor.io/docs/create-client.html]]]
// 你需要确保 classpath 环境中添加了合适的 [[[引擎|https://ktor.io/docs/http-client-engines.html]]]
var client = HttpClientJvmKt.HttpClient((config) -> Unit.INSTANCE);

// 构建需要请求的 API 示例，此处为 [[[获取房间信息|https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_room.html]]] API
// 假设房间ID是 1234
var api = GetRoomApi.create(Long.parseUnsignedLong("1234"));
// Java 中操作无符号数字比较麻烦，因此面向Java的大部分相关操作也会提供基于字符串操作的重载、扩展API
var api = GetRoomApi.create("1234");

// 发起请求并得到结果。在 API 模块中有三种请求方式（均使用 `APIRequests` 的静态方法）：
// 1️⃣ 使用 api.request, 得到 Ktor 进行 HTTP 请求的原始响应类型 HttpResponse
HttpResponse response = APIRequests.requestBlocking(api, client, token);

// 2️⃣ 使用 api.requestResult, 得到请求结果的统一响应体结构。假如此响应代表成功(result.retcode == 0), 则可以通过 result.data 获取响应结果。
ApiResult<GetRoomResult> result = APIRequests.requestResultBlocking(api, client, token);

// 3️⃣ 使用 api.requestData, 得到当响应体代表成功时的响应数据。如果响应体不为成功则会抛出异常。
GetRoomResult requestData = APIRequests.requestDataBlocking(api, client, token);
```
{switcher-key="%jb%"}

```java
import java.util.concurrent.CompletableFuture;// 准备 bot 的必要信息
var botId = "bot_xxxx";
var botSecret = "xxxx";
// 大别野ID，请求时作为token的一员。
var villaId = "1234";
// 构建请求用的 token
var token = new MiyousheVillaApiToken(botId, botSecret, villaId);

// 准备一个用于 HTTP 请求的 [[[HttpClient|https://ktor.io/docs/create-client.html]]]
// 你需要确保 classpath 环境中添加了合适的 [[[引擎|https://ktor.io/docs/http-client-engines.html]]]
var client = HttpClientJvmKt.HttpClient((config) -> Unit.INSTANCE);

// 构建需要请求的 API 示例，此处为 [[[获取房间信息|https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_room.html]]] API
// 假设房间ID是 1234
var api = GetRoomApi.create(Long.parseUnsignedLong("1234"));
// Java 中操作无符号数字比较麻烦，因此面向Java的大部分相关操作也会提供基于字符串操作的重载、扩展API
var api = GetRoomApi.create("1234");

// 发起请求并得到结果。在 API 模块中有三种请求方式（均使用 `APIRequests` 的静态方法）：
// 1️⃣ 使用 api.request, 得到 Ktor 进行 HTTP 请求的原始响应类型 HttpResponse
CompletableFuture<HttpResponse> response = APIRequests.requestAsync(api, client, token);

// 2️⃣ 使用 api.requestResult, 得到请求结果的统一响应体结构。假如此响应代表成功(result.retcode == 0), 则可以通过 result.data 获取响应结果。
CompletableFuture<ApiResult<GetRoomResult>> result = APIRequests.requestResultAsync(api, client, token);

// 3️⃣ 使用 api.requestData, 得到当响应体代表成功时的响应数据。如果响应体不为成功则会抛出异常。
CompletableFuture<GetRoomResult> requestData = APIRequests.requestDataAsync(api, client, token);
```
{switcher-key="%ja%"}

<tip switcher-key="%ja%">
异步API的响应结果都是 <code>CompletableFuture</code> 类型的，
在处理它们的时候需要注意尽可能避免使用任何会造成阻塞的方法，
例如 <code>get</code> 或 <code>join</code> 。

```java
// ↓ CompletableFuture<? extends GetRoomResult>
var requestData = APIRequests.requestDataAsync(api, client, token);
// 使用 CompletableFuture 的 thenAccept 或诸如此类的流程化API来接收、处理你的结果
requestData.thenAccept(result -> System.out.println("Result: " + result));
```

当然，你也可以选择配合使用一些与 <code>CompletableFuture</code> 兼容的其他异步/响应式框架，
例如 <a href="https://github.com/reactor/reactor-core">reactor</a>。

同样的，异步结果的 <b>异常</b> 也需要特别处理，否则你可能会在不经意间错过它：

```java
// ↓ CompletableFuture<? extends GetRoomResult>
var requestData = APIRequests.requestDataAsync(api, client, token);
requestData.exceptionally(ex -> {
    // 生产环境里应该使用更好的处理方法或可靠的日志输出
    ex.printStackTrace();
    return null;
})
.thenAccept(...);
```
</tip>


</tab>
</tabs>

<seealso>
<category ref="related">
    <a href="https://simbot.forte.love/" summary="前往simbot3官网来了解simbot3的各通用能力。">simbot3官网</a>
    <a href="https://docs.simbot.forte.love/" summary="前往API文档或通过源码翻阅、搜索并了解具体的功能。">API文档</a>
    <a href="https://github.com/orgs/simple-robot/discussions" summary="前往社区提出疑惑。">社区</a>
</category>
</seealso>
