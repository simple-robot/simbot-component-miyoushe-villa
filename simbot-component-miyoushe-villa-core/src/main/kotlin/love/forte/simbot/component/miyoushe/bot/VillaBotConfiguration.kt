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

import love.forte.simbot.miyoushe.stdlib.bot.BotConfiguration


/**
 *
 * @author ForteScarlet
 */
public class VillaBotConfiguration {
    /**
     * 直接提供给 [标准库 Bot][love.forte.simbot.miyoushe.stdlib.bot.Bot] 的源配置类。
     */
    public var botConfiguration: BotConfiguration = BotConfiguration()

}

public inline fun VillaBotConfiguration.botConfiguration(block: BotConfiguration.() -> Unit) {
    botConfiguration.apply(block)
}
