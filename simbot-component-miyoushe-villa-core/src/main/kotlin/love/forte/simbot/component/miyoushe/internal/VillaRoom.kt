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

import love.forte.simbot.JSTP
import love.forte.simbot.definition.Channel
import love.forte.simbot.miyoushe.api.room.Room


/**
 * 米游社大别野中的子频道。
 * 也就是 `房间 (Room)`。
 *
 * @author ForteScarlet
 */
public interface VillaRoom : Channel {
    /**
     * 原始的房间信息。
     */
    public val room: Room

    @JSTP
    override suspend fun guild(): VillaGuild



}
