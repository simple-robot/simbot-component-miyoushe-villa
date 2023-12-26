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

package love.forte.simbot.miyoushe.stdlib.bot.internal

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import love.forte.simbot.logger.Logger
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.miyoushe.api.GetWebsocketInfoApi
import love.forte.simbot.miyoushe.api.WebsocketInfo
import love.forte.simbot.miyoushe.event.*
import love.forte.simbot.miyoushe.stdlib.DisposableHandle
import love.forte.simbot.miyoushe.stdlib.bot.*
import love.forte.simbot.miyoushe.utils.atomic.AtomicLong
import love.forte.simbot.miyoushe.utils.atomic.AtomicULong
import love.forte.simbot.miyoushe.utils.atomic.atomic
import love.forte.simbot.miyoushe.utils.atomic.atomicULong
import love.forte.simbot.miyoushe.utils.collection.SimpleConcurrentQueue
import love.forte.simbot.miyoushe.utils.collection.createSimpleConcurrentQueue
import love.forte.simbot.miyoushe.ws.*
import love.forte.simbot.util.stageloop.State
import love.forte.simbot.util.stageloop.loop
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextULong


/**
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSerializationApi::class)
internal class BotImpl(
    override val ticket: Bot.Ticket,
    override val configuration: BotConfiguration,
) : Bot {
    private val job: Job
    override val coroutineContext: CoroutineContext
    private val started: AtomicLong = atomic(0L) // TODO to AtomicBool?
    private val protoBuf: ProtoBuf = ProtoBuf.Default
    private val startingLock = Mutex()
    override val apiDecoder: Json = configuration.apiDecoder
    private val wsClient: HttpClient
    override val apiClient: HttpClient

    private val logger: Logger = LoggerFactory.getLogger("love.forte.simbot.miyoushe.stdlib.bot.${ticket.botId}")

    init {
        //region job and context
        val configCoroutineContext = configuration.coroutineContext
        val configJob = configCoroutineContext[Job]
        job = SupervisorJob(configJob)
        coroutineContext = configCoroutineContext.minusKey(Job) + job
        //endregion

        wsClient = configuration.let {
            val wsClientEngine = it.wsClientEngine
            val wsClientEngineFactory = it.wsClientEngineFactory

            when {
                wsClientEngine != null -> HttpClient(wsClientEngine) {
                    configWsHttpClient()
                }

                wsClientEngineFactory != null -> HttpClient(wsClientEngineFactory) {
                    configWsHttpClient()
                }

                else -> HttpClient {
                    configWsHttpClient()
                }
            }
        }

        apiClient = configuration.let {
            val apiClientEngine = it.apiClientEngine
            val apiClientEngineFactory = it.apiClientEngineFactory

            when {
                apiClientEngine != null -> HttpClient(apiClientEngine) {
                    configApiHttpClient()
                }

                apiClientEngineFactory != null -> HttpClient(apiClientEngineFactory) {
                    configApiHttpClient()
                }

                else -> HttpClient {
                    configApiHttpClient()
                }
            }
        }

        job.invokeOnCompletion {
            wsClient.close()
            apiClient.close()
        }
    }

    private fun HttpClientConfig<*>.configWsHttpClient() {
        install(WebSockets) {
            // ...?
        }
    }

    private fun HttpClientConfig<*>.configApiHttpClient() {
        install(ContentNegotiation) {
            json(configuration.apiDecoder)
        }

        val apiHttpRequestTimeoutMillis = configuration.apiHttpRequestTimeoutMillis
        val apiHttpConnectTimeoutMillis = configuration.apiHttpConnectTimeoutMillis
        val apiHttpSocketTimeoutMillis = configuration.apiHttpSocketTimeoutMillis

        if (apiHttpRequestTimeoutMillis != null || apiHttpConnectTimeoutMillis != null || apiHttpSocketTimeoutMillis != null) {
            install(HttpTimeout) {
                apiHttpRequestTimeoutMillis?.also { requestTimeoutMillis = it }
                apiHttpConnectTimeoutMillis?.also { connectTimeoutMillis = it }
                apiHttpSocketTimeoutMillis?.also { socketTimeoutMillis = it }
            }
        }

        install(HttpRequestRetry) {
            maxRetries = 3
        }
    }

    private data class ProcessorWrapper(val processor: EventProcessor) {
        suspend fun process(event: Event<EventExtendData>, source: EventSource) {
            processor.apply { event.process(source) }
        }
    }


    // TODO WeakRef?
    private class RegisterHandle(wrapper: ProcessorWrapper, queue: SimpleConcurrentQueue<ProcessorWrapper>) :
        DisposableHandle {
        private var wrapper: ProcessorWrapper? = wrapper
        private var queue: SimpleConcurrentQueue<ProcessorWrapper>? = queue

        private val disposed = atomic(false)

        override fun dispose() {
            if (disposed.compareAndSet(expect = false, value = true)) {
                val w = wrapper ?: return
                val q = queue ?: return
                queue = null
                wrapper = null

                q.remove(w)
            }
        }
    }

    private val processorQueue = createSimpleConcurrentQueue<ProcessorWrapper>()
    private val preProcessorQueue = createSimpleConcurrentQueue<ProcessorWrapper>()

    override fun registerPreProcessor(processor: EventProcessor): DisposableHandle {
        val wrapper = ProcessorWrapper(processor)
        preProcessorQueue.add(wrapper)
        return RegisterHandle(wrapper, preProcessorQueue)
    }

    override fun registerProcessor(processor: EventProcessor): DisposableHandle {
        val wrapper = ProcessorWrapper(processor)
        processorQueue.add(wrapper)
        return RegisterHandle(wrapper, processorQueue)
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    private fun started() {
        started.value = 1L
    }

    override val isStarted: Boolean
        get() = started.value == 1L

    override val isActive: Boolean get() = job.isActive
    override val isCancelled: Boolean get() = job.isCancelled
    override val isCompleted: Boolean get() = job.isCompleted

    override suspend fun join() {
        job.join()
    }

    override fun cancel(reason: Throwable?) {
        if (reason != null) {
            job.cancel(CancellationException(reason.message, reason))
        } else {
            job.cancel()
        }
    }

    private fun checkState() {
        if (isCancelled || isCompleted) {
            check(!isCancelled && !isCompleted) {
                if (isCancelled) "Bot is cancelled" else "Bot is completed"
            }
        }
    }

    @Volatile
    private var stateJob: Job? = null

    override suspend fun start(): Unit = startingLock.withLock {
        checkState()
        stateJob?.cancel()
        stateJob = null

        val state = GetWebsocketInfo(this).loop(onEach = { it !is ReceiveEvent })
        if (state !is ReceiveEvent) {
            throw IllegalStateException("The 'ReceiveEvent' state has not been reached")
        }

        // is receiveEvent. loop async
        stateJob = launch {
            state.loop()
            logger.info("State loop done.")
        }

        started()
    }


    private sealed class BotLinkState : State<BotLinkState>()

    private class GetWebsocketInfo(val bot: BotImpl) : BotLinkState() {
        override suspend fun invoke(): BotLinkState {
            val wsInfo = GetWebsocketInfoApi.create().requestDataBy(bot, bot.configuration.loginVillaId)
            bot.logger.debug("Got WebsocketInfo: $wsInfo")
            return Connect(bot, wsInfo)
        }
    }

    /**
     * 建立ws连接
     */
    private class Connect(
        val bot: BotImpl,
        val info: WebsocketInfo
    ) : BotLinkState() {
        override suspend fun invoke(): BotLinkState {
            val session = bot.wsClient.webSocketSession(info.websocketUrl)
            bot.logger.debug("Created session: {}", session)
            return SendLogin(bot, info, session)
        }
    }

    /**
     * 发送 [PLogin] 登录包并等待响应；
     * 处初始化 id 的 [AtomicULong]
     */
    private class SendLogin(
        val bot: BotImpl,
        val info: WebsocketInfo,
        val session: DefaultClientWebSocketSession
    ) : BotLinkState() {
        override suspend fun invoke(): BotLinkState {
            val id = atomicULong(0u)
            val configuration = bot.configuration

            val pl = PLogin(
                uid = info.uid.toULong(),
                token = "${configuration.loginVillaId}.${bot.ticket.botSecret}.${bot.ticket.botId}",
                platform = info.platform,
                appId = info.appId,
                deviceId = info.deviceId,
                region = configuration.loginRegion ?: Random.nextULong().toString(),
                meta = configuration.loginMeta
            )

            // bizType 7
            val loginBiz = BizType.LONG_CONN_LOGIN.value

            val packet = ProtoPacket.request(
                bizType = loginBiz,
                id = id.getAndDecrement(),
                appid = info.appId.toUInt(),
                body = bot.protoBuf.encodeToByteArray(PLogin.serializer(), pl)
            )

            bot.logger.debug("Send PLogin packet: {}, {}", pl, packet)

            session.outgoing.sendPacket(packet)

            while (true) {
                val frame = session.incoming.receive()
                bot.logger.trace("Receiving PLoginReply frame: {}", frame)
                if (frame is Frame.Binary) {
                    val protoPacket = ByteReadChannel(frame.data).readToProtoPacket()
                    bot.logger.trace("Receiving PLoginReply frame packet: {}", protoPacket)
                    if (protoPacket.bizType == loginBiz) {
                        val loginReply =
                            bot.protoBuf.decodeFromByteArray(PLoginReply.serializer(), protoPacket.bodyData)

                        bot.logger.debug("Received PLoginReply: {}", loginReply)
                        if (!loginReply.isSuccess) {
                            throw PLoginFailedException(loginReply)
                        }

                        break
                    }
                }
            }

            return InitHB(bot, info, session, id)
        }
    }

    /**
     * 初始化心跳任务
     */
    private class InitHB(
        val bot: BotImpl,
        val info: WebsocketInfo,
        val session: DefaultClientWebSocketSession,
        val id: AtomicULong
    ) : BotLinkState() {
        override suspend fun invoke(): BotLinkState {
            val hbJob = HBJob(session, HB_SECONDS) {
                try {
                    // send hb
                    val hb = PHeartBeat(GMTDate().timestamp.toString())
                    val hbPacket = ProtoPacket.request(
                        bizType = BizType.LONG_CONN_HB_KEEPALIVE.value,
                        id = id.getAndIncrement(),
                        appid = info.appId.toUInt(),
                        body = bot.protoBuf.encodeToByteArray(PHeartBeat.serializer(), hb)
                    )

                    session.outgoing.sendPacket(hbPacket)
                    bot.logger.debug("Send PHeartBeat: {}", hb)
                } catch (e: Throwable) {
                    bot.logger.error("Send PHeartBeat failed: {}", e.message, e)
                }
            }

            return ReceiveEvent(bot, info, session, id, hbJob)
        }
    }


    private class ReceiveEvent(
        val bot: BotImpl,
        val info: WebsocketInfo,
        val session: DefaultClientWebSocketSession,
        val id: AtomicULong,
        val hbJob: HBJob
    ) : BotLinkState() {
        val logger get() = bot.logger

        override suspend fun invoke(): BotLinkState? {

            if (!session.isActive) {
                // TODO Not Active!
                logger.error("Bot session is not active. Cancel the bot {}", bot)
                val reason = session.closeReason.await()
                bot.cancel(CancellationException("reason: $reason", null))
                // done.
                return null
            }

            logger.trace("Receiving next frame...")
            val frameCatching = session.incoming.receiveCatching()
            frameCatching.onClosed { e ->
                logger.error(
                    "Bot session receive failed: session closed and reason: {}. Cancel the bot {}",
                    e?.message,
                    bot,
                    e
                )
                bot.cancel(CancellationException(e?.message?.let { "Session closed: $it" } ?: "Session closed", e))
                return null
            }

            frameCatching.onFailure { e ->
                logger.error(
                    "Bot session receive failed: {}. Cancel the bot {}",
                    e?.message,
                    bot,
                    e
                )
                bot.cancel(CancellationException(e?.message?.let { "Session receive onFailure: $it" }
                    ?: "Session receive onFailure", e))
                return null
            }

            try {
                val frame = frameCatching.getOrThrow()

                logger.trace("Received next frame: {}", frame)
                if (frame !is Frame.Binary) {
                    logger.warn("Frame {} !is Frame.Binary, skip.")
                    return this
                }

                val packet = ByteReadChannel(frame.data).readToProtoPacket()
                logger.debug("Received next packet: {}", packet)

                when (packet.bizType) {
                    BizType.LONG_CONN_HB_KEEPALIVE.value -> {
                        val hbReply =
                            bot.protoBuf.decodeFromByteArray(PHeartBeatReply.serializer(), packet.bodyData)
                        bot.logger.debug("Received PHeartBeatReply: {}", hbReply)
                    }

                    // logout
                    BizType.LONG_CONN_LOGOUT.value -> {
                        val logoutReply =
                            bot.protoBuf.decodeFromByteArray(PLogoutReply.serializer(), packet.bodyData)
                        bot.logger.debug("Received PLogoutReply: {}", logoutReply)
                        if (logoutReply.isSuccess) {
                            bot.logger.debug(
                                "Received PLogoutReply, will cancel current bot {}",
                                logoutReply,
                                bot
                            )
                            bot.cancel(CancellationException("PLogoutReply received: $logoutReply", null))
                            return null
                        } else {
                            bot.logger.error(
                                "Received PLogoutReply, but it's code is not success and will not cancel current bot {}",
                                logoutReply,
                                bot
                            )
                        }
                    }

                    // uplink
                    BizType.UPLINK.value -> {
                        bot.logger.error("Received bizType = BizType.UPLINK: {}", BizType.UPLINK)
                    }

                    // SHUTDOWN
                    BizType.SHUTDOWN.value -> {
                        bot.logger.debug(
                            "Received {}, cancel current session {} and reconnect.",
                            BizType.SHUTDOWN,
                            session
                        )
                        session.cancel(CancellationException(BizType.SHUTDOWN.toString(), null))
                        return GetWebsocketInfo(bot)
                    }
                    // KICK_OFF
                    BizType.KICK_OFF.value -> {
                        bot.logger.warn(
                            "Received {}, cancel current session {} and bot {}",
                            BizType.KICK_OFF,
                            session,
                            bot
                        )
                        session.cancel(CancellationException(BizType.KICK_OFF.toString(), null))
                        bot.cancel(CancellationException(BizType.KICK_OFF.toString(), null))
                        return null
                    }

                    // Event
                    BizType.BOT_OPEN.value -> {
                        val rawEvent =
                            bot.protoBuf.decodeFromByteArray(PRawEvent.serializer(), packet.bodyData)
                        bot.logger.debug("Received PRawEvent: {}")

                        processEvent(packet, rawEvent)
                    }

                    else -> {
                        bot.logger.debug("Received other packet, didn't deserialization.")
                    }
                }


            } catch (serEx: SerializationException) {
                // ser e?
            } catch (e: Throwable) {
                // e?
            }


            return this
        }


        // process event.
        private suspend fun processEvent(packet: ProtoPacket, event: PRawEvent) {
            val realEvent = event.toEvent()
            val source = ProtoPacketEventSource(packet)

            // 顺序地使用 preProcessor 处理
            for (processorWrapper in bot.preProcessorQueue) {
                kotlin.runCatching {
                    processorWrapper.process(realEvent, ProtoPacketEventSource(packet))
                }.onFailure { e ->
                    bot.logger.error(
                        "PreProcessor [{}] process({}, {}) onFailure: {}",
                        processorWrapper.processor,
                        realEvent,
                        source,
                        e.message,
                        e
                    )
                }
            }

            // use Bot scope
            bot.launch {
                for (processorWrapper in bot.processorQueue) {
                    kotlin.runCatching {
                        processorWrapper.process(realEvent, ProtoPacketEventSource(packet))
                    }.onFailure { e ->
                        bot.logger.error(
                            "Processor [{}] process({}, {}) onFailure: {}",
                            processorWrapper.processor,
                            realEvent,
                            source,
                            e.message,
                            e
                        )
                    }
                }
            }
        }
    }


    companion object {
        const val HB_SECONDS = 20
    }
}


private class HBJob(scope: CoroutineScope, val second: Int, val block: suspend () -> Unit) {
    val state = MutableStateFlow(second)

    init {
        scope.launch {
            while (true) {
                delay(1000)
                decr()
            }
        }

        scope.launch {
            state.filter { it <= 0 }.collect {
                block()
                reset()
            }
        }

    }

    fun decr() = state.updateAndGet { max(it - 1, 0) }
    fun reset() {
        state.value = second
    }
}

private suspend fun SendChannel<Frame>.sendPacket(packet: ProtoPacket) {
    send(Frame.Binary(true, packet.toPacket()))
}
