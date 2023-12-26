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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.bot.VillaBotManager
import love.forte.simbot.component.miyoushe.event.*
import love.forte.simbot.component.miyoushe.internal.VillaGuildImpl
import love.forte.simbot.component.miyoushe.internal.VillaMemberImpl
import love.forte.simbot.component.miyoushe.internal.VillaRoomGroupImpl
import love.forte.simbot.component.miyoushe.internal.VillaRoomImpl
import love.forte.simbot.component.miyoushe.internal.event.*
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.literal
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.miyoushe.api.member.GetMemberApi
import love.forte.simbot.miyoushe.api.member.GetVillaMembersApi
import love.forte.simbot.miyoushe.api.room.GetRoomApi
import love.forte.simbot.miyoushe.api.room.GetVillaGroupRoomListApi
import love.forte.simbot.miyoushe.api.villa.GetVillaApi
import love.forte.simbot.miyoushe.event.*
import love.forte.simbot.miyoushe.stdlib.DisposableHandle
import love.forte.simbot.miyoushe.stdlib.bot.Bot
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
                    pushIfProcessableInBotAsync(VillaSendMessageEvent) {
                        VillaSendMessageEventImpl(this@VillaBotImpl, this as Event<SendMessage>, source)
                    }
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

    override suspend fun guild(id: ID): VillaGuild? {
        return guildInternal(id.literal)
//        try {
//            return guildInternal(id.literal)
//        } catch (ae: ApiResultNotSuccessException) {
//            // TODO
//            if (ae.apiResult.retcode == ApiResult.RETCODE_BOT_ACCESS_DEFIED) {
//
//            }
//            throw ae
//        }
    }

    internal suspend fun guildInternal(id: String): VillaGuildImpl {
        val villa = GetVillaApi.create().requestDataBy(this, id)
        return VillaGuildImpl(this, villa.villa)
    }

    internal suspend fun roomInternal(id: ULong, villaId: String, villa: VillaGuild? = null): VillaRoomImpl {
        val result = GetRoomApi.create(id).requestDataBy(this, villaId)

        return VillaRoomImpl(this, result.room, villaId, villa = villa)
    }

    internal fun roomFlowInternal(
        villaId: String,
        villa: VillaGuild? = null
    ): Flow<VillaRoomImpl> {
        return flow {
            val listResult = GetVillaGroupRoomListApi.create().requestDataBy(bot, villaId)
            for (groupRoom in listResult.list) {
                val group = VillaRoomGroupImpl(groupRoom.groupId, groupRoom.groupName)
                for (room in groupRoom.roomList) {
                    val roomImpl = VillaRoomImpl(this@VillaBotImpl, room, villaId, villa = villa)
                    emit(roomImpl)
                }
            }
        }
    }

    internal suspend fun memberInternal(id: ULong, villaId: String, villa: VillaGuild? = null): VillaMemberImpl {
        val result = GetMemberApi.create(id).requestDataBy(this, villaId)

        return VillaMemberImpl(this, result.member, villaId, villa)
    }

    internal fun memberFlowInternal(
        villaId: String,
        fixSize: ULong = 100u,
        villa: VillaGuild? = null
    ): Flow<VillaMemberImpl> {
        return GetVillaMembersApi.getVillaMembersApiFlow(fixSize) { requestDataBy(this@VillaBotImpl, villaId) }
            .map { member ->
                VillaMemberImpl(this, member, villaId, villa)
            }
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

