---
switcher-label: JavaAPI风格
---

<!--suppress XmlDeprecatedElement -->
<var name="jb" value="阻塞"/>
<var name="ja" value="异步"/>


# 请求API

在 [快速开始 - 使用API](use-api.md) 中其实已经介绍过如何对一个 `API` 发起请求。
此处会将在API模块、标准库模块中请求API的异同列举出来。

为了方便演示，下文中我们会使用一个虚构的 `FooApi` 类型作为示例。因为所有的 `API` 类型都具有共性，
有关这方面的内容可参考章节 [API#通用特性](API.md#common)

## API模块

API模块是一种底层库模块，因此此模块中提供的API请求方式也会相对“原始”一些。

对API的请求有那么几种基本形式，它们在 Kotlin 中以扩展函数的形式提供，
而 Java 中则可以在 `APIRequests` 的静态函数中找到它们。 


<deflist>
<def title="request">

最基础的请求方式，其他几种方式也是针对此方式的一种封装。
它的请求会直接返回最原始的结果 `HttpResponse`，不会做任何额外判断，
比较适合需要自行处理 HTTP 原始响应结果的情况。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val response = fooApi.request(client, token, token)
```

</tab>
<tab title="Java" group-key="Java">

```java
HttpResponse response = APIRequests.requestBlocking(api, client, token);
```
{switcher-key="%jb%"}

```java
CompletableFuture<? extends HttpResponse> response = APIRequests.requestAsync(api, client, token);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
<def title="requestResult">

`requestResult` 会在 `request` 的基础上对结果进行解析，
如果响应HTTP状态码并非异常，则会将其解析为 `ApiResult` 类型后返回。

这也是大别野API中的统一响应体结构。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val result = fooApi.requestResult(client, token, token)
```

</tab>
<tab title="Java" group-key="Java">

```java
ApiResult<Foo> result = APIRequests.requestResultBlocking(api, client, token);
```
{switcher-key="%jb%"}

```java
CompletableFuture<ApiResult<Foo>> result = APIRequests.requestResultAsync(api, client, token);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
<def title="requestData">

`requestData` 会在 `requestResult` 的基础上对结果进行校验，
如果 result 中的状态码代表为成功，则会将其中的真正响应数据拿出并返回。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val result = fooApi.requestResult(client, token)
```

</tab>
<tab title="Java" group-key="Java">

```java
Foo data = APIRequests.requestDataBlocking(api, client, token);
```
{switcher-key="%jb%"}

```java
CompletableFuture<Foo> data = APIRequests.requestDataAsync(api, client, token);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
</deflist>


## 标准库模块

在标准库模块中，因为 `Bot` 类型已经包含了大多数所需信息，
因此不再需要像API模块那样提供大量基础性的参数了（比如 client、token），
取而代之的是一个 `Bot` 实例和API请求的目标别野ID。

对API的请求有那么几种基本形式，它们在 Kotlin 中以扩展函数的形式提供，
而 Java 中则可以在 `BotRequests` 的静态函数中找到它们。 


<deflist>
<def title="requestBy">

最基础的请求方式，其他几种方式也是针对此方式的一种封装。
它的请求会直接返回最原始的结果 `HttpResponse`，不会做任何额外判断，
比较适合需要自行处理 HTTP 原始响应结果的情况。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val response = fooApi.requestBy(bot, villaId)
```

</tab>
<tab title="Java" group-key="Java">

```java
HttpResponse response = APIRequests.requestByBlocking(api, bot, villaId);
```
{switcher-key="%jb%"}

```java
CompletableFuture<? extends HttpResponse> response = APIRequests.requestByAsync(api, bot, villaId);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
<def title="requestResultBy">

`requestResultBy` 会在 `requestBy` 的基础上对结果进行解析，
如果响应HTTP状态码并非异常，则会将其解析为 `ApiResult` 类型后返回。

这也是大别野API中的统一响应体结构。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val result = fooApi.requestResultBy(bot, villaId)
```

</tab>
<tab title="Java" group-key="Java">

```java
ApiResult<Foo> result = APIRequests.requestResultByBlocking(api, bot, villaId);
```
{switcher-key="%jb%"}

```java
CompletableFuture<ApiResult<Foo>> result = APIRequests.requestResultByAsync(api, bot, villaId);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
<def title="requestDataBy">

`requestDataBy` 会在 `requestResultBy` 的基础上对结果进行校验，
如果 result 中的状态码代表为成功，则会将其中的真正响应数据拿出并返回。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
val result = fooApi.requestResultBy(bot, villaId)
```

</tab>
<tab title="Java" group-key="Java">

```java
Foo data = APIRequests.requestDataByBlocking(api, bot, villaId);
```
{switcher-key="%jb%"}

```java
CompletableFuture<Foo> data = APIRequests.requestDataByAsync(api, bot, villaId);
```
{switcher-key="%ja%"}

</tab>
</tabs>

</def>
</deflist>
