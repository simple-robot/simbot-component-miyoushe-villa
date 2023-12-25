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

import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import love.forte.simbot.ID
import love.forte.simbot.bot.BotAlreadyRegisteredException
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.bot.VillaBotConfiguration
import love.forte.simbot.component.miyoushe.bot.VillaBotManager
import love.forte.simbot.component.miyoushe.bot.VillaBotManagerConfiguration
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.literal
import love.forte.simbot.miyoushe.stdlib.bot.Bot
import love.forte.simbot.miyoushe.stdlib.bot.BotFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.coroutines.CoroutineContext


/**
 *
 * @author ForteScarlet
 */
internal class VillaBotManagerImpl(
    configuration: VillaBotManagerConfiguration,
    override val eventProcessor: EventProcessor,
    override val component: VillaComponent,
    override val job: Job,
    override val coroutineContext: CoroutineContext,
) : VillaBotManager() {
    private sealed class Container {
        abstract fun add(bot: VillaBot)
        abstract fun remove(bot: VillaBot): Boolean
        abstract fun remove(id: ID): Boolean
        abstract fun all(): List<VillaBot>
        abstract fun get(id: ID): VillaBot?
    }

    private class QueueContainer(private val queue: ConcurrentLinkedQueue<VillaBot> = ConcurrentLinkedQueue()) :
        Container() {
        override fun add(bot: VillaBot) {
            queue.add(bot)
        }

        override fun remove(id: ID): Boolean = queue.removeIf { it.id == id }
        override fun remove(bot: VillaBot): Boolean = queue.remove(bot)
        override fun all(): List<VillaBot> = queue.toList()
        override fun get(id: ID): VillaBot? = queue.find { it.id == id }
    }

    private class MapContainer(private val map: ConcurrentHashMap<String, VillaBot> = ConcurrentHashMap()) :
        Container() {
        override fun add(bot: VillaBot) {
            val currentValue = map.putIfAbsent(bot.id.literal, bot)
            if (currentValue != null) {
                throw BotAlreadyRegisteredException("id(${currentValue.id}): $currentValue")
            }
        }

        override fun remove(bot: VillaBot): Boolean = map.remove(bot.id.literal, bot)
        override fun remove(id: ID): Boolean = map.remove(id.literal) != null
        override fun all(): List<VillaBot> = map.values.toList()
        override fun get(id: ID): VillaBot? = map[id.literal]
    }

    private val container: Container = if (configuration.uniqueId) {
        MapContainer()
    } else {
        QueueContainer()
    }

    override fun register(ticket: Bot.Ticket, configuration: VillaBotConfiguration): VillaBot {
        val bot = BotFactory.create(ticket, configuration.botConfiguration)

        val sourceBotJob = bot.coroutineContext[Job]
        val botJob = SupervisorJob(sourceBotJob)
        val botContext = bot.coroutineContext.minusKey(Job) + botJob

        return VillaBotImpl(bot, component, eventProcessor, this, botJob, botContext)
    }

    override fun get(id: ID): VillaBot? = container.get(id)
    override fun all(): List<VillaBot> = container.all()
}
