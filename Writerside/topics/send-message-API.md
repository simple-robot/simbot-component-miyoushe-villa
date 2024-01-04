# 发送消息

发送消息是一个比较常见的需求，因此可以单独拿出来说一说。

## API

发送消息的API是 `SendMessageApi`，也就是大别野API中 
[发送消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/send_message.html)
的API封装。

TODO

## 核心库

首先，你需要先去simbot3官网中了解一下 [消息](https://simbot.forte.love/docs/basic/messages)。
在核心库中，所有消息相关的内容都会被封装、处理成 
<control>消息元素（`Message.Element`）</control>

有关大别野中消息元素的详细内容可参考章节 [消息元素](message-element.md)，
此处只对如何 
<control>发送消息</control>
进行介绍。

TODO

