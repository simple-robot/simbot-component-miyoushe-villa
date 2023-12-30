---
switcher-label: JavaAPI风格
---

<var name="jb" value="阻塞"/>
<var name="ja" value="异步"/>

# 使用API

## 安装

<tabs group="Build">
<tab title="Gradle Kotlin DSL">

```kotlin
plugins {
    // 或使用 kotlin("multiplatform")
    // 但是不管是什么，你都需要 kotlin 插件来支持自动选择对应平台的依赖。
    kotlin("jvm") version "%kt-version%"
    // 其他一些插件, 随你喜欢
    [[[java|https://docs.gradle.org/current/userguide/java_plugin.html#header]]] // 你依旧可以使用 Java 编写代码
    [[[application|https://docs.gradle.org/current/userguide/application_plugin.html]]] // 使用 application 可以打包你的应用程序
}
// 其他配置...
dependencies {
    implementation 'love.forte.simbot.component:simbot-component-miyoushe-villa-api:%version%'
    // 其他依赖..
}
```

</tab>
<tab title="Gradle Groovy">

```groovy
plugins {
    // 或使用 org.jetbrains.kotlin.multiplatform，
    // 但是不管是什么，你都需要 kotlin 插件来支持自动选择对应平台的依赖。
    id 'org.jetbrains.kotlin.jvm' version '%kt-version%'
    // 其他一些插件, 随你喜欢
    id '[[[java|https://docs.gradle.org/current/userguide/java_plugin.html#header]]]' // 你依旧可以使用 Java 编写代码
    id '[[[application|https://docs.gradle.org/current/userguide/application_plugin.html]]]' // 使用 application 可以打包你的应用程序
}
// 其他配置...
dependencies {
    implementation 'love.forte.simbot.component:simbot-component-miyoushe-villa-api:%version%'
    // 其他依赖..
}

```

</tab>
<tab title="Maven">

```xml
<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- Maven 需要添加 `-jvm` 后缀来选择使用 JVM 平台 -->
    <artifactId>simbot-component-miyoushe-villa-api-jvm</artifactId>
    <version>%version%</version>
</dependency>
```

</tab>
</tabs>

## 使用

<tooltip term="大别野组件">大别野组件</tooltip> 的API模块提供了针对
[米游社大别野API](https://webstatic.mihoyo.com/vila/bot/doc/) 的基本对应封装。

API封装的命名与API具有一定关联，例如 [`获取大别野信息`](https://webstatic.mihoyo.com/vila/bot/doc/villa_api/get_villa.html)：

<compare first-title="API" second-title="API封装">

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
<tab title="Kotlin">

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
<tab title="Java">

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
CompletableFuture<? extends HttpResponse> response = APIRequests.requestAsync(api, client, token);

// 2️⃣ 使用 api.requestResult, 得到请求结果的统一响应体结构。假如此响应代表成功(result.retcode == 0), 则可以通过 result.data 获取响应结果。
CompletableFuture<? extends ApiResult<GetRoomResult>> result = APIRequests.requestResultAsync(api, client, token);

// 3️⃣ 使用 api.requestData, 得到当响应体代表成功时的响应数据。如果响应体不为成功则会抛出异常。
CompletableFuture<? extends GetRoomResult> requestData = APIRequests.requestDataAsync(api, client, token);
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

