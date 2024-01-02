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

package love.forte.simbot.component.miyoushe.internal

import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.miyoushe.api.member.Member


/**
 *
 * @author ForteScarlet
 */
internal class VillaMemberImpl(
    override val bot: VillaBotImpl,
    override val source: Member,
    private val villaId: String,
    private val villa: VillaGuild? = null
) : VillaMember {

    override suspend fun guild(): VillaGuild {
        if (villa != null) return villa

        return bot.guildInternal(villaId)
    }

    override suspend fun delete(): Boolean {
        // TODO("Not yet implemented")
        return false
    }

    override fun toString(): String = "VillaMember(source=$source, villaId=$villaId)"
}
