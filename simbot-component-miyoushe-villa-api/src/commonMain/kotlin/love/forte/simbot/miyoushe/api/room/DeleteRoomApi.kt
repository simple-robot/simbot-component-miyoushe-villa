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

package love.forte.simbot.miyoushe.api.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [删除房间](https://webstatic.mihoyo.com/vila/bot/doc/room_api/delete_room.html)
 *
 * 删除房间
 *
 * `POST /vila/api/bot/platform/deleteRoom`
 *
 * @author ForteScarlet
 */
public class DeleteRoomApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/deleteRoom"

        /**
         * Create an instance of [DeleteGroupApi].
         *
         * @param roomId 房间 ID
         */
        @JvmStatic
        @JvmName("create")
        public fun create(roomId: ULong): DeleteRoomApi = DeleteRoomApi(Body(roomId))

        /**
         * Create an instance of [DeleteGroupApi].
         *
         * @param roomIdString 房间 ID
         * @throws NumberFormatException 如果 [roomIdString] 无法转化为 [ULong].
         */
        @JvmStatic
        public fun create(roomIdString: String): DeleteRoomApi = create(roomIdString.toULong())
    }

    override val path: String
        get() = PATH


    /**
     * Body of [DeleteGroupApi]
     *
     * @property roomId 房间 id
     */
    @Serializable
    public data class Body(@SerialName("room_id") @Serializable(ULongWriteStringSerializer::class) val roomId: ULong)

    override fun toString(): String {
        return "DeleteRoomApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeleteRoomApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}
