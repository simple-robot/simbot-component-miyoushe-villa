# 首页

欢迎来到 [<tooltip term="Simple Robot">Simple Robot</tooltip>](https://github.com/simple-robot/simpler-robot)
（下文简称 <tooltip term="simbot">simbot</tooltip> ）
的米游社大别野机器人组件 （下文简称 <tooltip term="大别野组件">大别野组件</tooltip> ） 文档。

大别野组件是基于 [simbot核心库](https://github.com/simple-robot/simpler-robot)
对 [米游社大别野机器人](https://open.miyoushe.com) 的组件实现，
也包括对API和事件订阅能力的底层库实现模块。

大别野组件由 Kotlin 语言编写，不同的模块分别基于 **KMP (Kotlin Multiplatform)** 或 **Kotlin/JVM** 构建。
在JVM平台上对 Java 友好，并基于 KMP 提供更多平台的可能性。

> 对于simbot绝大多数的标准、基本功能的介绍都在 [simbot官网](https://simbot.forte.love/) 中。
> {style="note"}

## 模块简介

simbot的大别野机器人组件整个项目分为三个主要模块。它们分别是 **API模块**、**stdlib（标准库）模块**和**core（核心库）模块**。

### API模块

<tldr>
    <p>平台：<b>JVM、JS、Native</b></p>
    <p>
    API模块基于 <b>KMP</b> 构建项目，支持 <b>JVM、JS、Native</b> 平台，
    使用 <a href="https://ktor.io/"><b>Ktor</b></a> 作为API请求（http请求）的解决方案。
    </p>
</tldr>

API模块的主要作用是提供针对大别野机器人开发平台中的各API和事件类型的底层封装。
此模块 **不提供** 过度的功能性封装，
主要宗旨为在风格统一的情况下将API和事件 **描述** 为可供使用的依赖库。

### stdlib 标准库模块

<tldr>
    <p>平台：<b>JVM、JS、Native</b></p>
    <p>
    标准库模块基于 <b>KMP</b> 构建项目，支持 <b>JVM、JS、Native</b> 平台，
    使用 <a href="https://ktor.io/"><b>Ktor</b></a> 作为API请求（http请求）的解决方案。
    </p>
</tldr>

标准库模块依赖API模块，在此基础上额外提供大别野中 **Bot** 概念的封装与能力实现，
达到对一个 Bot 的事件订阅、消息发送等能力。
与API模块类型，标准库模块的主要宗旨同样是在风格统一的情况下将Bot与事件订阅的能力 **描述** 为可供使用的依赖库。

> **API模块** 和 **标准库模块** 与 **simbot** 的关系主要体现在较为统一的 **风格** 上。
> 实质上这两个模块 **不直接依赖** 与simbot相关的库。（可能存在部分仅编译依赖或编译器插件依赖）
> 
> 它们两个是可以完全作为独立的底层API依赖库使用的。
{style="note"}


### core 核心库

<tldr>
    <p>平台：<b>JVM</b></p>
    <p>
    核心库模块基于 <b>Kotlin/JVM</b> 构建项目，支持 <b>JVM</b> 平台，
    兼容并提供友好的Java API。
    </p>
</tldr>


核心库模块是对 **simbot核心库** 的大别野机器人实现，也是此项目作为 **“simbot组件”** 的主要体现。

核心库模块依赖并实现 **simbot API**，针对其定义的各类型来提供simbot风格的大别野组件实现。例如实现 simbot 提供的 `Bot`
类型为 `VillaBot` 并提供大别野组件下的各种独特能力。

核心库模块是一种高级封装，它会借助 simbot API 强大的能力来提供大量高级功能，例如对事件的订阅和更便捷的消息发送、对 Spring
Boot 的支持等。

核心库模块会尽可能屏蔽掉**底层API**（上述两个模块），使其对开发者透明，取而代之的是更加清晰明了的API。

> 当然，对于一些特殊场景或不得已的情况，开发者依旧可以很轻松的使用底层API来达成所求目的。

<seealso>
    <category ref="related">
        <a href="https://open.miyoushe.com/">大别野开放平台</a>
        <a href="https://webstatic.mihoyo.com/vila/bot/doc/">大别野机器人开发文档</a>
    </category>
</seealso>
