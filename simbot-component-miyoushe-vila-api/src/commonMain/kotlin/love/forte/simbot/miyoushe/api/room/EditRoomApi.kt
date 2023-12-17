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
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [编辑房间](https://webstatic.mihoyo.com/vila/bot/doc/room_api/edit_room.html)
 *
 * 编辑房间，只允许编辑房间的名称
 *
 * `POST /vila/api/bot/platform/editRoom`
 *
 * @author ForteScarlet
 */
public class EditRoomApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/editRoom"

        /**
         * Create an instance of [EditRoomApi].
         *
         * @param roomId 房间 id
         * @param roomName 房间名称
         */
        @JvmStatic
        @JvmName("create")
        public fun create(roomId: ULong, roomName: String): EditRoomApi = EditRoomApi(Body(roomId, roomName))

        /**
         * Create an instance of [EditRoomApi].
         *
         * @param roomIdString 房间 id 字符串
         * @param roomName 房间名称
         * @throws NumberFormatException
         */
        @JvmStatic
        public fun create(roomIdString: String, roomName: String): EditRoomApi =
            create(roomIdString.toULong(), roomName)

    }

    override val path: String
        get() = PATH


    /**
     * Body of [EditRoomApi]
     *
     * @property roomId 房间 id
     * @property roomName 房间名称
     */
    @Serializable
    public data class Body(
        @SerialName("room_id") val roomId: ULong,
        @SerialName("room_name") val roomName: String,
    )

    override fun toString(): String {
        return "EditRoomApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EditRoomApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }

    /*
    room_id	uint64	房间 id
room_name	string	房间名称
     */
}
