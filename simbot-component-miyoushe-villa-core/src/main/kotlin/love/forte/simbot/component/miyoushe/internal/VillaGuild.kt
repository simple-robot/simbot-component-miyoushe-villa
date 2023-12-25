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

import love.forte.simbot.ID
import love.forte.simbot.JST
import love.forte.simbot.definition.Guild
import love.forte.simbot.miyoushe.api.villa.Villa
import love.forte.simbot.utils.item.Items


/**
 * 米游社大别野中的频道服务器。
 * 也就是 `大别野 (Villa)`。
 *
 * @author ForteScarlet
 */
public interface VillaGuild : Guild {
    /**
     * 原始的大别野对象。
     */
    public val source: Villa

    override val channels: Items<VillaRoom>
        get() = TODO("Not yet implemented")

    @JST
    override suspend fun channel(id: ID): VillaRoom? {
        TODO("Not yet implemented")
    }

    override val currentChannel: Int
        get() = TODO("Not yet implemented")

    override val maximumChannel: Int
        get() = TODO("Not yet implemented")
}
