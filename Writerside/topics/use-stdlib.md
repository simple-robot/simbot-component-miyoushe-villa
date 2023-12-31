---
switcher-label: JavaAPI风格
---

<!--suppress XmlDeprecatedElement -->
<var name="jb" value="阻塞"/>
<var name="ja" value="异步"/>

# 使用标准库

<tldr>
本章节介绍如何使用标准库模块来实现构建、启动一个 Bot，以及订阅、处理事件。
</tldr>

<note>
<include from="snippets.md" element-id="doc-desc-version" />
</note>

## 安装

<include from="snippets.md" element-id="component-install">
    <var name="name" value="stdlib" />
    <var name="maven-name-suffix" value="-jvm" />
</include>

然后选择一个合适的 Ktor 引擎。

<include from="snippets.md" element-id="engine-choose"></include>

## 使用

<tooltip term="大别野组件">大别野组件</tooltip> 
的标准库模块在 [API模块](use-api.md) 之上提供了构建 Bot、订阅并处理事件的能力。

<tabs group="Code">
<tab title="Kotlin" group-key="Kotlin">

```kotlin
// 准备 bot 的必要信息
val botId = "bot_xxxx"
val botSecret = "xxxx"
// 用于注册 bot 的 “票据” 信息。
val ticket = Bot.Ticket(botId, botSecret)
// 登录时获取 ws 连接信息时所需的 villa_id 信息。
// 根据文档描述，如果bot处于测试阶段，可以使用测试大别野的id，如果已经上线，则可以使用 "0"
val villaId = "0"

// 构建一个 Bot，并可选的进行一些配置。
val bot = BotFactory.create(ticket) {
    loginVillaId = villaId
    loginRegion = null
    // 其他配置...
}

// 注册事件有一些不同但类似的方式
// 1️⃣ 通过 registerProcessor 注册一个普通的事件处理器，此处理器会接收并处理所有类型的事件
//     registerProcessor 是最基本的注册方式，也是其他方式的最终汇集点
bot.registerProcessor { source ->
    // source 代表事件的来源以及它的原始样貌，比如通过 ws 获取到的原始样貌就是接收到的二进制数据以及解析出来的含有 protobuf 格式body的协议包
    // this: Event<EventExtendData>, 事件的统一结构体以及它内部的具体事件结构体
    println("event: $this")
    println("event.extendData: $extendData")
    println("eventSource: $source")
}

// 2️⃣ 通过 processEvent<ExtendDataType> 注册一个针对具体 extendData 类型的事件处理器，它只有在 extendData 与目标类型一致时才会处理。
//     此示例展示处理 SendMessage 也就是消息事件，并在对方发送了包含 'stop' 的文本时终止 bot。
bot.processEvent<SendMessage> { source ->
    val content = extendData.content
    println("extendData.content: $content")
    // 如果是文本类型消息的话
    if (extendData.objectName == SendMessage.OBJECT_NAME_TEXT) {
        // 解析 content 中的文本消息为 MsgContentInfo<TextMsgContent>，并在之后进一步判断文本消息的内容
        val msgContentInfo =
            MsgContentInfo.decode(content = content, deserializationStrategy = MsgContentInfo.TextSerializer)
        println(msgContentInfo)
        println(msgContentInfo.content)
        println(msgContentInfo.content.text)
        println(msgContentInfo.content.entities)

        if ("stop" in msgContentInfo.content.text) {
            bot.cancel()
        }
    }
}

// 启动 bot, 此时会开始获取ws、连接并接收消息。
bot.start()

// 挂起 bot，直到它结束（被终止）
bot.join()
```

</tab>
<tab title="Java" group-key="Java">

```java
// 准备 bot 的必要信息
var botId = "bot_xxxx";
var botSecret = "xxxx";
// 用于注册 bot 的 “票据” 信息。
var ticket = new Bot.Ticket(botId, botSecret);

// 准备配置类，当然，也可以不准备，即省略配置。
var conf = new BotConfiguration();
// 登录时获取 ws 连接信息时所需的 villa_id 信息。
// 根据文档描述，如果bot处于测试阶段，可以使用测试大别野的id，如果已经上线，则可以使用 "0"
conf.setLoginVillaId("0");
// 其他配置...

// 构建一个 Bot
var bot = BotFactory.create(ticket, conf);

// 注册事件有一些不同但类似的方式
// 1️⃣ 通过 registerProcessor 注册一个普通的事件处理器，此处理器会接收并处理所有类型的事件
//     使用 EventProcessors.blocking 可以构建一个阻塞的事件处理器示例
bot.registerProcessor(
        EventProcessors.blocking((event, source) -> {
            System.out.println("event: " + event);
            System.out.println("event.data: " + event.getExtendData());
            System.out.println("eventSource: " + source);
        }));

// 2️⃣ 通过 registerProcessor 注册一个针对具体 extendData 类型的事件处理器，它只有在 extendData 与目标类型一致时才会处理。
//     使用 EventProcessors.blocking 可以构建一个阻塞的事件处理器实例, 并在开头指定目标事件的类型
//     此示例展示处理 SendMessage 也就是消息事件，并在对方发送了包含 'stop' 的文本时终止 bot。                
bot.registerProcessor(
        EventProcessors.blocking(SendMessage.class, (event, source) -> {
            var extendData = event.getExtendData();
            var content = extendData.getContent();
            System.out.println("extendData.content: " + content);
            // 如果是文本类型消息的话
            if (extendData.getObjectName() == SendMessage.OBJECT_NAME_TEXT) {
                // 解析 content 中的文本消息为 MsgContentInfo<TextMsgContent>，并在之后进一步判断文本消息的内容
                var msgContentInfo = MsgContentInfo.decode(content, MsgContentInfo.TextSerializer);
                System.out.println(msgContentInfo);
                System.out.println(msgContentInfo.getContent());
                System.out.println(msgContentInfo.getContent().getText());
                System.out.println(msgContentInfo.getContent().getEntities());
                if (msgContentInfo.getContent().getText().contains("stop")) {
                    bot.cancel();
                }
            }
        }));

// 阻塞地启动bot
bot.startBlocking();
// 阻塞当前线程，直到它结束（被终止）
bot.joinBlocking();
```

{switcher-key="%jb%"}

```java
// 准备 bot 的必要信息
var botId = "bot_xxxx";
var botSecret = "xxxx";
// 用于注册 bot 的 “票据” 信息。
var ticket = new Bot.Ticket(botId, botSecret);

// 准备配置类，当然，也可以不准备，即省略配置。
var conf = new BotConfiguration();
// 登录时获取 ws 连接信息时所需的 villa_id 信息。
// 根据文档描述，如果bot处于测试阶段，可以使用测试大别野的id，如果已经上线，则可以使用 "0"
conf.setLoginVillaId("0");
// 其他配置...

// 构建一个 Bot
var bot = BotFactory.create(ticket, conf);

// 注册事件有一些不同但类似的方式
// 1️⃣ 通过 registerProcessor 注册一个普通的事件处理器，此处理器会接收并处理所有类型的事件
//     使用 EventProcessors.async 可以构建一个异步的事件处理器示例
//     所有异步API都需要以 CompletableFuture 作为结果，且不建议在其中执行任何阻塞API —— 那样的话使用异步风格就没有意义了。
bot.registerProcessor(
        EventProcessors.async((event, source) -> {
            System.out.println("event: " + event);
            System.out.println("event.data: " + event.getExtendData());
            System.out.println("eventSource: " + source);

            // 因为上述代码没有什么必须真正异步的内容，所以直接返回一个空的 future 即可。
            // 实际应用中可以配合请求 API 时的异步API来做到真正的异步结果。
            return CompletableFuture.completedFuture(null);
        }));

// 2️⃣ 通过 registerProcessor 注册一个针对具体 extendData 类型的事件处理器，它只有在 extendData 与目标类型一致时才会处理。
//     使用 EventProcessors.async 可以构建一个异步的事件处理器实例, 并在开头指定目标事件的类型
//     此示例展示处理 SendMessage 也就是消息事件，并在对方发送了包含 'stop' 的文本时终止 bot。
bot.registerProcessor(
        EventProcessors.async(SendMessage.class, (event, source) -> {
            var extendData = event.getExtendData();
            var content = extendData.getContent();
            System.out.println("extendData.content: " + content);
            // 如果是文本类型消息的话
            if (extendData.getObjectName() == SendMessage.OBJECT_NAME_TEXT) {
                // 解析 content 中的文本消息为 MsgContentInfo<TextMsgContent>，并在之后进一步判断文本消息的内容
                var msgContentInfo = MsgContentInfo.decode(content, MsgContentInfo.TextSerializer);
                System.out.println(msgContentInfo);
                System.out.println(msgContentInfo.getContent());
                System.out.println(msgContentInfo.getContent().getText());
                System.out.println(msgContentInfo.getContent().getEntities());
                if (msgContentInfo.getContent().getText().contains("stop")) {
                    bot.cancel();
                }
            }

            return CompletableFuture.completedFuture(null);
        }));

// 异步地启动bot，并在启动后将 bot 转化为 Future。
// 转化后的 Future 会直到 bot 结束（被终止）时完成，因此可通过此 future 监测 bot 是否终止
var botAsFuture = bot.startAsync().thenCompose(unit -> bot.asFuture());

botAsFuture.thenAccept(unit -> {
    // 通过常规的 future API 操作结束后的行为，比如输出日志。
    System.out.println("Bot 终止");
});

// ⚠ 不建议，但是最简单粗暴的方式就是直接 join 它，就像阻塞API一样
botAsFuture.join();
```
{switcher-key="%ja%"}

<tip switcher-key="%ja%">
正如上述示例中注释描述所说，当使用异步API时候，请尽可能避免在异步的事件处理器中使用任何阻塞API。
</tip>

</tab>
</tabs>

> 封装的事件的结构、属性以及属性类型都基本与大别野文档中的一致或具有对应关系。
