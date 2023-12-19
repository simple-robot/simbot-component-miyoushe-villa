/*
 * Copyright (c) 2023. ForteScarlet.
 *
 * This file is part of simbot-component-miyoushe.
 *
 * simbot-component-miyoushe is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-miyoushe is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-miyoushe,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package love.forte.simbot.miyoushe.stdlib.bot

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.EventExtendData
import love.forte.simbot.miyoushe.event.EventSource
import love.forte.simbot.miyoushe.stdlib.DisposableHandle
import love.forte.simbot.miyoushe.ws.PKickOff
import love.forte.simbot.miyoushe.ws.PLogout
import love.forte.simbot.miyoushe.ws.PLogoutReply
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmSynthetic


/**
 *
 * 一个米游社大别野的 bot。
 *
 * [Bot] 基于 [Websocket](https://webstatic.mihoyo.com/vila/bot/doc/websocket/)
 * 与米游社大别野建立连接并订阅、处理事件。
 *
 * ### 终止
 *
 * [Bot] 可能会通过 [logout]、[cancel] 主动终止，或收到了 [PKickOff] 数据包而被动终止。
 * [Bot] 被终止后 **不可重新启动**。
 *
 * @author ForteScarlet
 */

public interface Bot : CoroutineScope {
    override val coroutineContext: CoroutineContext

    /**
     * bot 的配置信息。
     */
    public val configuration: BotConfiguration

    /**
     * Bot 的相关信息。可能存在敏感信息。
     */
    public val ticket: Ticket

    /**
     * Bot 的信息。用于构建 token、建立连接等。
     *
     */
    public data class Ticket(
        val botId: String,
        val botSecret: String,
        // pubKey?
    )

    /**
     * bot用于api请求的 [HttpClient].
     */
    public val apiClient: HttpClient

    /**
     * 用于api请求并反序列化的 [Json]
     */
    public val apiDecoder: Json

    /**
     * 添加一个事件**预处理器**。
     *
     * [registerPreProcessor] 与 [processor] 不同的是，
     * [registerPreProcessor] 所注册的所有事件处理器会在接收到事件的时候以**同步顺序**的方式依次执行，
     * 而后再交由下游的 [processor] 处理。
     *
     * 也同样因此，尽可能避免在 [registerPreProcessor] 中执行任何耗时逻辑，这可能会对事件处理流程造成严重阻塞。
     *
     * @param processor 用于处理事件的函数类型
     */
    public fun registerPreProcessor(processor: EventProcessor): DisposableHandle

    /**
     * 注册一个事件处理器。
     */
    public fun registerProcessor(processor: EventProcessor): DisposableHandle

    /**
     * bot 是否使用过 [start] 且成功启动过至少一次。
     */
    public val isStarted: Boolean

    /**
     * bot 协程任务是否存活
     */
    public val isActive: Boolean

    /**
     * bot 是否已经被终止。
     */
    public val isCancelled: Boolean

    /**
     * bot 是否已经完成（结束）。
     */
    public val isCompleted: Boolean

    /**
     * 启动当前 bot （建立 ws 连接）并开始接收、处理事件。
     * 如果已经启动，则重新调用会断开现有连接并重新建立连接。
     *
     * @throws IllegalArgumentException 如果 bot 已经被关闭或已经结束
     * @throws PLoginFailedException 如果登录时失败
     */
    @JvmSynthetic // TODO JST?
    public suspend fun start()

    /**
     * 向服务器发出 [登出](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E7%99%BB%E5%87%BA) ([PLogout]) 数据包。
     * bot 会继续运行直到服务器响应了 [PLogoutReply]。
     * 如果希望直接终止 bot，则考虑使用 [cancel] 以协程的方式关闭。
     */
    public fun logout()

    /**
     * 挂起直到此 bot 的协程被终止。
     * 可能会通过 [logout]、[cancel] 主动终止，或收到了 [PKickOff] 数据包而被动终止。
     */
    @JvmSynthetic // TODO JST?
    public suspend fun join()

    /**
     * 关闭当前 bot 所处协程任务。也会使得 bot 与服务器的连接断开。
     */
    public fun cancel(reason: Throwable? = null)
}

/**
 * 注册一个仅处理指定 [EventExtendData] 类型的事件处理器。
 *
 * ```kotlin
 * bot.processEvent<SendMessage> { source ->
 *     println("source: ${source.source}")
 *     println("event:  $extendData")
 * }
 * ```
 */
public inline fun <reified E : EventExtendData> Bot.processEvent(crossinline processor: suspend Event<E>.(EventSource) -> Unit): DisposableHandle {
    return registerProcessor { source ->
        if (extendData is E) {
            @Suppress("UNCHECKED_CAST")
            processor.invoke(this as Event<E>, source)
        }
    }
}

/**
 * 注册一个仅处理指定源 [S]、指定 [EventExtendData] 类型 [E] 的事件处理器。
 *
 * ```kotlin
 * bot.processEventWithSource<SendMessage, ProtoPacketEventSource> { source ->
 *     println("source: ${source.source}")
 *     println("event:  $extendData")
 * }
 * ```
 */
public inline fun <reified E : EventExtendData, reified S : EventSource> Bot.processEventWithSource(crossinline processor: suspend Event<E>.(S) -> Unit): DisposableHandle {
    return registerProcessor { source ->
        if (extendData is E && source is S) {
            @Suppress("UNCHECKED_CAST")
            processor.invoke(this as Event<E>, source)
        }
    }
}

/**
 * 注册一个仅处理指定源 [S] 的事件处理器。
 *
 * ```kotlin
 * bot.processSource<ProtoPacketEventSource> { source ->
 *     println("source: ${source.source}")
 *     println("event:  $extendData")
 * }
 * ```
 */
@JvmSynthetic
public inline fun <reified S : EventSource> Bot.processSource(crossinline processor: suspend Event<*>.(S) -> Unit): DisposableHandle {
    return registerProcessor { source ->
        if (source is S) {
            processor.invoke(this, source)
        }
    }
}

/**
 * 对事件进行处理的事件处理器。
 *
 * 对于Java或JS会提供各自平台兼容的函数类型，例如 Java 中 `EventProcessors` 提供的静态方法:
 *
 * ```java
 * EventProcessors.blocking((event, source) -> { ... })
 * EventProcessors.async((event, source) -> { /* return CompletableFuture */ })
 * ```
 *
 * @see Event
 * @see EventSource
 */
public fun interface EventProcessor {
    @JvmSynthetic
    public suspend fun Event<EventExtendData>.process(source: EventSource)
}
