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

import love.forte.simbot.miyoushe.stdlib.bot.internal.BotImpl
import kotlin.jvm.JvmOverloads

/**
 * 用于构建 [Bot] 的工厂。
 *
 */
public object BotFactory {

    /**
     * 提供所需票据信息与配置，构建一个 [Bot]。
     */
    @JvmOverloads
    public fun create(ticket: Bot.Ticket, configuration: BotConfiguration = BotConfiguration()): Bot {
        return BotImpl(ticket, configuration)
    }

    /**
     * 提供所需票据信息与配置，构建一个 [Bot]。
     */
    @JvmOverloads
    public fun create(botId: String, botSecret: String, configuration: BotConfiguration = BotConfiguration()): Bot {
        return BotImpl(Bot.Ticket(botId, botSecret), configuration)
    }

}

/**
 * 提供所需票据信息与配置，构建一个 [Bot]。
 */
public inline fun BotFactory.create(ticket: Bot.Ticket, configBlock: BotConfiguration.() -> Unit): Bot =
    create(ticket, BotConfiguration().also(configBlock))

/**
 * 提供所需票据信息与配置，构建一个 [Bot]。
 */
public inline fun BotFactory.create(botId: String, botSecret: String, configBlock: BotConfiguration.() -> Unit): Bot =
    create(botId, botSecret, BotConfiguration().also(configBlock))
