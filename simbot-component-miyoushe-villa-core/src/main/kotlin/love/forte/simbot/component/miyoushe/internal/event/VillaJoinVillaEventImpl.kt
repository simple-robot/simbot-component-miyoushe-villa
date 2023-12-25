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

import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.event.VillaJoinVillaEvent
import love.forte.simbot.component.miyoushe.internal.VillaGuild
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.EventSource
import love.forte.simbot.miyoushe.event.JoinVilla


/**
 *
 * @author ForteScarlet
 */
internal class VillaJoinVillaEventImpl(
    override val bot: VillaBotImpl,
    override val sourceEvent: Event<JoinVilla>,
    override val sourceEventSource: EventSource
) : VillaJoinVillaEvent() {
    override suspend fun member(): VillaMember = with(sourceEventExtend) {
        bot.memberInternal(joinUid, villaIdStrValue)
    }

    override suspend fun source(): VillaGuild = with(sourceEventExtend) {
        bot.guildInternal(villaIdStrValue)
    }
}
