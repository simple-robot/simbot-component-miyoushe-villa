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

package love.forte.simbot.component.miyoushe.bot

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import love.forte.simbot.*
import love.forte.simbot.application.ApplicationConfiguration
import love.forte.simbot.application.EventProviderFactory
import love.forte.simbot.bot.Bot
import love.forte.simbot.bot.BotManager
import love.forte.simbot.bot.BotVerifyInfo
import love.forte.simbot.bot.ComponentMismatchException
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotManagerImpl
import love.forte.simbot.event.EventProcessor
import love.forte.simbot.logger.LoggerFactory
import love.forte.simbot.logger.logger


/**
 * 对 [VillaBot] 进行管理与构建的 [BotManager] 实现。
 *
 * [VillaBotManager] 不限制 [Bot] 的注册数量
 *
 * @author ForteScarlet
 */
public abstract class VillaBotManager : BotManager<VillaBot>() {
    protected abstract val job: Job
    abstract override val component: VillaComponent
    protected abstract val eventProcessor: EventProcessor

    @OptIn(InternalSimbotApi::class)
    override fun register(verifyInfo: BotVerifyInfo): VillaBot {
        val serializer = VillaSerializableBotConfiguration.serializer()

        val component = verifyInfo.componentId

        val currentComponent = this.component.id
        if (component != currentComponent) {
            logger.debug(
                "[{}] mismatch: [{}] != [{}]",
                verifyInfo.name,
                component,
                currentComponent
            )
            throw ComponentMismatchException("[$component] != [$currentComponent]")
        }

        val decodeConfiguration = verifyInfo.decode(serializer)
        val configuration = decodeConfiguration.toConfiguration()

        return register(ticket = decodeConfiguration.ticket.toTicket(), configuration)
    }

    /**
     * 注册一个 bot
     */
    public abstract fun register(
        ticket: love.forte.simbot.miyoushe.stdlib.bot.Bot.Ticket,
        configuration: VillaBotConfiguration
    ): VillaBot

    /**
     * 注册一个 bot
     */
    public fun register(ticket: love.forte.simbot.miyoushe.stdlib.bot.Bot.Ticket): VillaBot =
        register(ticket, VillaBotConfiguration())

    abstract override fun get(id: ID): VillaBot?

    abstract override fun all(): List<VillaBot>

    override fun invokeOnCompletion(handler: CompletionHandler) {
        job.invokeOnCompletion(handler)
    }

    override suspend fun join() {
        job.join()
    }

    override val isActive: Boolean
        get() = job.isActive

    override val isCancelled: Boolean
        get() = job.isCancelled

    override val isStarted: Boolean
        get() = false

    override suspend fun start(): Boolean = false

    override suspend fun doCancel(reason: Throwable?): Boolean {
        if (!job.isActive) return false
        job.cancel(reason?.let { CancellationException(it.localizedMessage, it) })
        job.join()
        return true
    }

    public companion object Factory : EventProviderFactory<VillaBotManager, VillaBotManagerConfiguration> {
        override val key: Attribute<VillaBotManager> = attribute("${VillaComponent.ID_VALUE}-botManager")
        private val logger = LoggerFactory.logger<VillaBotManager>()

        override suspend fun create(
            eventProcessor: EventProcessor,
            components: List<Component>,
            applicationConfiguration: ApplicationConfiguration,
            configurator: VillaBotManagerConfiguration.() -> Unit
        ): VillaBotManager {
            val component = components.firstOrNull { it is VillaComponent } as? VillaComponent
                ?: throw ComponentMismatchException("[${VillaComponent.ID_VALUE}] type of ${VillaComponent::class}")

            val configuration = VillaBotManagerConfiguration().also(configurator)

            val applicationCoroutineContext = applicationConfiguration.coroutineContext
            val configurationCoroutineContext = configuration.coroutineContext

            val applicationJob = applicationCoroutineContext[Job]
            val configurationJob = configurationCoroutineContext[Job]

            val job: Job = when {
                applicationJob == null && configurationJob == null -> SupervisorJob()
                applicationJob == null -> SupervisorJob(configurationJob)
                configurationJob == null -> SupervisorJob(applicationJob)
                else -> {
                    // not null, both
                    SupervisorJob(configurationJob).also { job ->
                        applicationJob.invokeOnCompletion { cause ->
                            if (job.isActive) {
                                job.cancel(cause?.let { CancellationException(it.message, it) })
                            }
                        }
                    }
                }
            }

            val coroutineContext =
                applicationCoroutineContext.minusKey(Job) + configurationCoroutineContext.minusKey(Job) + job


            return VillaBotManagerImpl(configuration, eventProcessor, component, job, coroutineContext)
        }
    }
}

/**
 * 注册一个 bot
 */
public inline fun VillaBotManager.register(
    ticket: love.forte.simbot.miyoushe.stdlib.bot.Bot.Ticket,
    block: VillaBotConfiguration.() -> Unit = {}
): VillaBot =
    register(ticket, VillaBotConfiguration().also(block))

/**
 * 注册一个 bot
 */
public inline fun VillaBotManager.register(
    botId: String,
    botSecret: String,
    block: VillaBotConfiguration.() -> Unit = {}
): VillaBot =
    register(love.forte.simbot.miyoushe.stdlib.bot.Bot.Ticket(botId, botSecret), VillaBotConfiguration().also(block))
