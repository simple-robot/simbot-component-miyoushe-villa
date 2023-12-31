---
switcher-label: JavaAPI风格
---

<!--suppress XmlDeprecatedElement -->
<var name="jb" value="阻塞"/>
<var name="ja" value="异步"/>
<var name="jr" value="Reactor"/>

# 使用 Spring Boot

<tldr>
本章节介绍如何在 Spring Boot 中使用大别野组件。
</tldr>

## 安装

首先你需要选择使用一个 
[`simboot-core-spring-boot-starter`](https://central.sonatype.com/artifact/love.forte.simbot.boot/simboot-core-spring-boot-starter/versions)
的 3.x 版本（下文以 `simbot_version` 代表之）。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("love.forte.simbot.boot:simboot-core-spring-boot-starter:$simbot_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'love.forte.simbot.boot:simboot-core-spring-boot-starter:$simbot_version'
```

</tab>
<tab title="Maven" group-key="maven">

```xml

<dependency>
    <groupId>love.forte.simbot.boot</groupId>
    <!-- Maven 需要添加 `-jvm` 后缀来选择使用 JVM 平台 -->
    <artifactId>simboot-core-spring-boot-starter</artifactId>
    <version>${simbot_version}</version>
</dependency>
```

</tab>
</tabs>

然后安装大别野组件的core模块依赖。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("love.forte.simbot.component:simbot-component-miyoushe-villa-core:%version%")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'love.forte.simbot.component:simbot-component-miyoushe-villa-core:%version%'
```

</tab>
<tab title="Maven" group-key="maven">

```xml

<dependency>
    <groupId>love.forte.simbot.component</groupId>
    <!-- Maven 需要添加 `-jvm` 后缀来选择使用 JVM 平台 -->
    <artifactId>simbot-component-miyoushe-villa-core</artifactId>
    <version>%version%</version>
</dependency>
```

</tab>
</tabs>

然后选择一个合适的 Ktor 引擎。

> 你只能选择 JVM 平台的引擎 —— Spring Boot Starter 并没有其他平台。

<include from="snippets.md" element-id="engine-choose" />

## BOT配置

接下来，在项目资源文件目录下的 `simbot-bots` 文件夹中创建一个用于配置bot的配置文件 `xxx.bot.json`
(文件名随意，扩展名应为 `.bot` 或 `.bot.json`)，
而配置文件的内容则参考章节 [BOT配置文件](bot-config.md) 。

```
${PROJECT_SRC}/main/resources/simbot-bots/xxx.bot.json
```

<tip>
如果想要修改此路径，可在 Spring Boot 的配置文件中进行配置：

<tabs>
<tab title="properties">

```
# 自定义配置bot资源文件的扫描路径。
# 默认为 classpath:simbot-bots/*.bot*
# 如果要使用本地文件可以使用 `file:` 开头
simbot.bot-configuration-resources[0]=classpath:simbot-bots/*.bot*
```

{noinject=true}

</tab>
<tab title="yaml">

```yaml
simbot:
  # 自定义配置bot资源文件的扫描路径。
  # 默认为 classpath:simbot-bots/*.bot*
  # 如果要使用本地文件可以使用 `file:` 开头
  bot-configuration-resources:
    - 'classpath:simbot-bots/*.bot*'
```

</tab>
</tabs>

</tip>

## 启动类

像每一个 Spring Boot 应用一样，你需要一个启动类，并通过标注 `@EnableSimbot` 来启用 simbot ：

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
@EnableSimbot
@SpringBootApplication
class App

fun main(vararg args: String) {
    runApplication<App>(args = args)
}
```

</tab>
<tab title="Java" group-key="Java">

```java
@EnableSimbot
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

> 如果你在 Java 中遇到了无法引用 `@EnableSimbot` 的情况，
> 或许可以参考 [这篇FAQ](https://simbot.forte.love/faq/包引用异常)。

</tab>
</tabs>

## 监听事件

接下来就是逻辑代码所在的地方了，编写一个监听函数并监听一个事件。

此处我们监听 `ChannelMessageEvent`，也就是 **子频道的消息事件**。

假设：要求 bot 必须 **被AT**，并且说一句 `你好`，此时 bot 会引用用户发送的消息并回复 `你也好!` ，类似于：

<deflist>
<def title="用户：">@BOT 你好</def>
<def title="BOT：">

_\> 用户: @BOT 你好_

你也好!

</def>
</deflist>

<tabs>
<tab title="Kotlin">

```kotlin
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent

@Component
class ExampleListener {

    @Listener
    @Filter(value = "你好", targets = Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    suspend fun onChannelMessage(event: ChannelMessageEvent) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        event.reply("你也好!")
    }
}
```

</tab>
<tab title="Java">

```java
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent

@Component
public class ExampleListener {
    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public void onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // Java中的阻塞式API
        event.replyBlocking("你也好!");
    }
}
```

{switcher-key="%jb%"}

```java
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent

@Component
public class ExampleListener {
    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public CompletableFuture<?> onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // 将 CompletableFuture 作为返回值，simbot会以非阻塞的形式处理它
        return event.replyAsync("你也好!");
    }
}
```

{switcher-key="%ja%"}

```java
import love.forte.simboot.annotation.ContentTrim
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.ChannelMessageEvent

@Component
public class ExampleListener {

    @Listener
    @Filter(value = "你好", targets = @Filter.Targets(atBot = true))
    @ContentTrim // 当匹配被at时，将'at'这个特殊消息移除后，剩余的文本消息大概率存在前后空格，通过此注解在匹配的时候忽略前后空格
    public Mono<?> onChannelMessage(ChannelMessageEvent event) { // 将要监听的事件类型放在参数里，即代表监听此类型的消息
        // 将 Mono 等响应式类型作为返回值，simbot会以非阻塞的形式处理它
        return Mono.fromCompletionStage(event.replyAsync("你也好!"));
    }

}
```

{switcher-key="%jr%"}

<note switcher-key="%jr%">

如果返回值是需要第三方库的响应式类型，那么你的项目环境依赖中必须存在 `Kotlin Coroutines` 对其的支持库才可使用。
你可以参考文档中
[响应式的处理结果](https://simbot.forte.love/docs/basic/event-listener#可响应式的处理结果)
的内容。

</note>

</tab>
</tabs>

## 启动

接下来，启动程序并在你的测试别野中@它试试看吧。

当然，如果遇到了预期外的问题也不要慌，积极反馈问题才能使我们变得更好，可以前往
[Issues](https://github.com/simple-robot/simpler-robot/issues)
反馈问题或在
[社区](https://github.com/orgs/simple-robot/discussions)
提出疑问。

<seealso>
<category ref="related">
    <a href="https://simbot.forte.love/" summary="前往simbot3官网来了解simbot3的各通用能力。">simbot3官网</a>
    <a href="https://docs.simbot.forte.love/" summary="前往API文档或通过源码翻阅、搜索并了解具体的功能。">API文档</a>
    <a href="https://github.com/orgs/simple-robot/discussions" summary="前往社区提出疑惑。">社区</a>
</category>
</seealso>