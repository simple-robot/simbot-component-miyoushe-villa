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

package love.forte.simbot.component.miyoushe.internal.bot

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.bot.VillaBotManager
import love.forte.simbot.component.miyoushe.event.VillaAuditCallbackEvent
import love.forte.simbot.component.miyoushe.event.VillaCreateRobotEvent
import love.forte.simbot.component.miyoushe.event.VillaDeleteRobotEvent
import love.forte.simbot.component.miyoushe.event.VillaJoinVillaEvent
import love.forte.simbot.component.miyoushe.internal.VillaGuild
import love.forte.simbot.component.miyoushe.internal.VillaRoom
import love.forte.simbot.component.miyoushe.internal.event.VillaAuditCallbackEventImpl
import love.forte.simbot.component.miyoushe.internal.event.VillaCreateRobotEventImpl
import love.forte.simbot.component.miyoushe.internal.event.VillaDeleteRobotEventImpl
import love.forte.simbot.component.miyoushe.internal.event.VillaJoinVillaEventImpl
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.literal
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.miyoushe.api.member.GetMemberApi
import love.forte.simbot.miyoushe.api.room.GetRoomApi
import love.forte.simbot.miyoushe.api.villa.GetVillaApi
import love.forte.simbot.miyoushe.event.*
import love.forte.simbot.miyoushe.stdlib.DisposableHandle
import love.forte.simbot.miyoushe.stdlib.bot.Bot
import love.forte.simbot.utils.item.Items
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater
import kotlin.concurrent.Volatile
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class VillaBotImpl(
    override val source: Bot,
    override val component: VillaComponent,
    override val eventProcessor: EventProcessor,
    override val manager: VillaBotManager,
    override val job: Job,
    override val coroutineContext: CoroutineContext,
) : VillaBot() {
    private val startLock = Mutex()
    override val logger = LoggerFactory.getLogger("love.forte.simbot.component.miyoushe.bot.${source.ticket.botId}")

    @Volatile
    private var robotInfo: Robot? = null

    override fun isMe(id: ID): Boolean {
        if (this.id == id) {
            return true
        }

        val rb = robotInfo ?: return true
        return rb.template.id == id.literal
    }

    override val username: String
        get() = robotInfo?.template?.name ?: source.ticket.botId

    override val avatar: String
        get() = robotInfo?.template?.icon ?: ""

    @Volatile
    private var handlerDisposable: DisposableHandle? = null

    override suspend fun start(): Boolean {
        if (!job.isActive) return false

        withJob(job) {
            startLock.withLock {
                if (handlerDisposable == null) {
                    handlerDisposable = registerHandler(source)
                }
                source.start()
            }
        }

        return true
    }


    @Suppress("UNCHECKED_CAST")
    private fun registerHandler(bot: Bot): DisposableHandle {
        return bot.registerPreProcessor { source ->
            // try to set robot info if it's null, and ignore the result.
            robotInfoUpdater.weakCompareAndSet(this@VillaBotImpl, null, robot)

            when (extendData) {
                is SendMessage -> {

                }

                is JoinVilla -> {
                    pushIfProcessableInBotAsync(VillaJoinVillaEvent) {
                        VillaJoinVillaEventImpl(this@VillaBotImpl, this as Event<JoinVilla>, source)
                    }
                }

                is CreateRobot -> {
                    pushIfProcessableInBotAsync(VillaCreateRobotEvent) {
                        VillaCreateRobotEventImpl(this@VillaBotImpl, this as Event<CreateRobot>, source)
                    }
                }

                is DeleteRobot -> {
                    pushIfProcessableInBotAsync(VillaDeleteRobotEvent) {
                        VillaDeleteRobotEventImpl(this@VillaBotImpl, this as Event<DeleteRobot>, source)
                    }
                }

                is AuditCallback -> {
                    pushIfProcessableInBotAsync(VillaAuditCallbackEvent) {
                        VillaAuditCallbackEventImpl(this@VillaBotImpl, this as Event<AuditCallback>, source)
                    }
                }

                is AddQuickEmoticon -> {
                    // TODO
                }

                is ClickMsgComponent -> {
                    // TODO
                }

                UnknownEventExtendData -> {
                    // TODO
                }

                else -> {
                    // TODO unknown
                }
            }
        }
    }

    private suspend inline fun pushIfProcessableInBotAsync(
        eventKey: love.forte.simbot.event.Event.Key<*>,
        crossinline block: () -> love.forte.simbot.event.Event
    ) {
        if (eventProcessor.isProcessable(eventKey)) {
            launch { eventProcessor.push(block()) }
        }
    }

    override val guilds: Items<VillaGuild>
        get() = TODO("Not yet implemented")

    override suspend fun guild(id: ID): VillaGuild? {
        TODO("Not yet implemented")
    }

    internal suspend fun guildInternal(id: String): VillaGuild { // TODO nullable?
        val villa = GetVillaApi.create().requestDataBy(this, id)

        TODO("VillaGuild")
    }

    internal suspend fun roomInternal(id: ULong, villaId: String, villa: VillaGuild? = null): VillaRoom {
        val result = GetRoomApi.create(id).requestDataBy(this, villaId)

        TODO("VillaRoom")
    }

    internal suspend fun memberInternal(id: ULong, villaId: String, villa: VillaGuild? = null): VillaMember {
        val result = GetMemberApi.create(id).requestDataBy(this, villaId)

        TODO("VillaMember")
    }


    companion object {
        private val robotInfoUpdater =
            AtomicReferenceFieldUpdater.newUpdater(VillaBotImpl::class.java, Robot::class.java, "robotInfo")
    }
}


private suspend inline fun withJob(parent: Job?, crossinline block: suspend CoroutineScope.() -> Unit) {
    val job = SupervisorJob(parent)
    try {
        withContext(job) {
            block()
        }
    } finally {
        job.complete()
    }
}
