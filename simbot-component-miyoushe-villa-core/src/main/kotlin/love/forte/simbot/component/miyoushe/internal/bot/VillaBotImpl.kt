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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.bot.VillaBotManager
import love.forte.simbot.component.miyoushe.internal.VillaGuild
import love.forte.simbot.component.miyoushe.internal.VillaRoom
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.miyoushe.api.member.GetMemberApi
import love.forte.simbot.miyoushe.api.room.GetRoomApi
import love.forte.simbot.miyoushe.api.villa.GetVillaApi
import love.forte.simbot.miyoushe.stdlib.bot.Bot
import love.forte.simbot.utils.item.Items
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
    private var registeredHandlers = false

    override suspend fun start(): Boolean {
        if (!job.isActive) return false

        withJob(job) {
            startLock.withLock {
                if (!registeredHandlers) {
                    // TODO register handlers

                    registeredHandlers = true
                }
                source.start()
            }
        }

        return true
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
