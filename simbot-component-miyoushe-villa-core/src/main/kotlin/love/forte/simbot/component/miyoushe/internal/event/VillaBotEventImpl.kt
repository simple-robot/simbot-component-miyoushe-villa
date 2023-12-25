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

package love.forte.simbot.component.miyoushe.internal.event

import love.forte.simbot.component.miyoushe.event.VillaCreateRobotEvent
import love.forte.simbot.component.miyoushe.event.VillaDeleteRobotEvent
import love.forte.simbot.component.miyoushe.internal.VillaGuild
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.literal
import love.forte.simbot.miyoushe.event.CreateRobot
import love.forte.simbot.miyoushe.event.DeleteRobot
import love.forte.simbot.miyoushe.event.Event


internal class VillaCreateRobotEventImpl(override val bot: VillaBotImpl, override val sourceEvent: Event<CreateRobot>) :
    VillaCreateRobotEvent() {
    override suspend fun guild(): VillaGuild = bot.guildInternal(villaId.literal)
}


internal class VillaDeleteRobotEventImpl(override val bot: VillaBotImpl, override val sourceEvent: Event<DeleteRobot>) :
    VillaDeleteRobotEvent() {
    override suspend fun guild(): VillaGuild = bot.guildInternal(villaId.literal)
}
