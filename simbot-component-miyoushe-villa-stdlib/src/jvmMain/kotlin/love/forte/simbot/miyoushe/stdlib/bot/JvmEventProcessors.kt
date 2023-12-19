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

@file:JvmName("EventProcessors")

package love.forte.simbot.miyoushe.stdlib.bot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import love.forte.simbot.Api4J
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.EventExtendData
import love.forte.simbot.miyoushe.event.EventSource
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.BiFunction
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 用于构建 [EventProcessor]、类似于 [BiConsumer] 的函数接口，但是可以允许任何异常的抛出。
 *
 * @see blocking
 * @see blockingInterruptible
 */
public fun interface BlockingEventProcessorFunction<E : EventExtendData, S : EventSource> {
    @Throws(Exception::class)
    public fun accept(event: Event<E>, source: S)
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数。
 * 可选地提供一个 [CoroutineContext], 如果 [context] 不为 [EmptyCoroutineContext], 则会切换上下文到其上后再执行 [function]。
 * 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 *
 */
@Api4J
@JvmOverloads
public fun blocking(
    context: CoroutineContext = Dispatchers.IO,
    function: BlockingEventProcessorFunction<EventExtendData, EventSource>
): EventProcessor {
    if (context != EmptyCoroutineContext) {
        return EventProcessor { source ->
            withContext(context) {
                function.accept(this@EventProcessor, source)
            }
        }
    }
    return EventProcessor { source ->
        function.accept(this, source)
    }
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数，并且使其逻辑处于 [runInterruptible] 中。
 * 可选地提供一个供于 [runInterruptible] 的 [CoroutineContext], 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 */
@Api4J
@JvmOverloads
public fun blockingInterruptible(
    context: CoroutineContext = Dispatchers.IO,
    function: BlockingEventProcessorFunction<EventExtendData, EventSource>
): EventProcessor {
    return EventProcessor { source ->
        runInterruptible(context) {
            function.accept(this, source)
        }
    }
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数。
 * 可选地提供一个 [CoroutineContext], 如果 [context] 不为 [EmptyCoroutineContext], 则会切换上下文到其上后再执行 [function]。
 * 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 *
 */
@Api4J
@JvmOverloads
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtendData> blocking(
    context: CoroutineContext = Dispatchers.IO,
    eventType: Class<E>,
    function: BlockingEventProcessorFunction<E, EventSource>
): EventProcessor {
    if (context != EmptyCoroutineContext) {
        return EventProcessor { source ->
            if (eventType.isInstance(extendData)) {
                withContext(context) {
                    function.accept(this@EventProcessor as Event<E>, source)
                }
            }
        }
    }
    return EventProcessor { source ->
        if (eventType.isInstance(extendData)) {
            function.accept(this as Event<E>, source)
        }
    }
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数，并且使其逻辑处于 [runInterruptible] 中。
 * 可选地提供一个供于 [runInterruptible] 的 [CoroutineContext], 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 */
@Api4J
@JvmOverloads
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtendData> blockingInterruptible(
    context: CoroutineContext = Dispatchers.IO,
    eventType: Class<E>,
    function: BlockingEventProcessorFunction<E, EventSource>
): EventProcessor {
    return EventProcessor { source ->
        if (eventType.isInstance(extendData)) {
            runInterruptible(context) {
                function.accept(this as Event<E>, source)
            }
        }
    }
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数。
 * 可选地提供一个 [CoroutineContext], 如果 [context] 不为 [EmptyCoroutineContext], 则会切换上下文到其上后再执行 [function]。
 * 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 *
 */
@Api4J
@JvmOverloads
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtendData, S : EventSource> blocking(
    context: CoroutineContext = Dispatchers.IO,
    eventType: Class<E>,
    sourceType: Class<S>,
    function: BlockingEventProcessorFunction<E, S>
): EventProcessor {
    if (context != EmptyCoroutineContext) {
        return EventProcessor { source ->
            if (eventType.isInstance(extendData) && sourceType.isInstance(source)) {
                withContext(context) {
                    function.accept(this@EventProcessor as Event<E>, sourceType.cast(source))
                }
            }
        }
    }
    return EventProcessor { source ->
        if (eventType.isInstance(extendData) && sourceType.isInstance(source)) {
            function.accept(this as Event<E>, sourceType.cast(source))
        }
    }
}

/**
 * 以阻塞的方式构建 [EventProcessor] 函数，并且使其逻辑处于 [runInterruptible] 中。
 * 可选地提供一个供于 [runInterruptible] 的 [CoroutineContext], 默认会使用 [Dispatchers.IO] 使其运行在 `IO` 调度器上。
 */
@Api4J
@JvmOverloads
@Suppress("UNCHECKED_CAST")
public fun <E : EventExtendData, S : EventSource> blockingInterruptible(
    context: CoroutineContext = Dispatchers.IO,
    eventType: Class<E>,
    sourceType: Class<S>,
    function: BlockingEventProcessorFunction<E, S>
): EventProcessor {
    return EventProcessor { source ->
        if (eventType.isInstance(extendData) && sourceType.isInstance(source)) {
            runInterruptible(context) {
                function.accept(this as Event<E>, sourceType.cast(source))
            }
        }
    }
}

/**
 * 以异步的方式构建 [EventProcessor] 函数。
 */
@Api4J
public fun async(
    function: BiFunction<Event<EventExtendData>, EventSource, CompletableFuture<*>>
): EventProcessor {
    return EventProcessor { source ->
        function.apply(this@EventProcessor, source).await()
    }
}

/**
 * 以异步的方式构建 [EventProcessor] 函数。
 */
@Suppress("UNCHECKED_CAST")
@Api4J
public fun <E : EventExtendData> async(
    eventType: Class<E>,
    function: BiFunction<Event<E>, EventSource, CompletableFuture<*>>
): EventProcessor {
    return EventProcessor { source ->
        if (eventType.isInstance(this)) {
            function.apply(this@EventProcessor as Event<E>, source).await()
        }
    }
}

/**
 * 以异步的方式构建 [EventProcessor] 函数。
 */
@Suppress("UNCHECKED_CAST")
@Api4J
public fun <E : EventExtendData, S : EventSource> async(
    eventType: Class<E>,
    sourceType: Class<S>,
    function: BiFunction<Event<E>, S, CompletableFuture<*>>
): EventProcessor {
    return EventProcessor { source ->
        if (eventType.isInstance(this) && sourceType.isInstance(source)) {
            function.apply(this@EventProcessor as Event<E>, sourceType.cast(source)).await()
        }
    }
}
