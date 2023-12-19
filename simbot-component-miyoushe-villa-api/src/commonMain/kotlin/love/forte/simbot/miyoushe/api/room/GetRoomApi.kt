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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [获取房间信息](https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_room.html)
 *
 * 获取房间
 *
 * `GET /vila/api/bot/platform/getRoom`
 *
 * @author ForteScarlet
 */
public class GetRoomApi private constructor(private val roomId: ULong) : MiyousheVillaGetApi<GetRoomResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getRoom"
        private val RESULT_RES = ApiResult.serializer(GetRoomResult.serializer())

        /**
         * Create an instance of [GetRoomApi].
         *
         * @param roomId 房间 ID
         */
        @JvmStatic
        @JvmName("create")
        public fun create(roomId: ULong): GetRoomApi = GetRoomApi(roomId)

        /**
         * Create an instance of [GetRoomApi].
         *
         * @param roomIdString 房间 ID
         * @throws NumberFormatException 如果 [roomIdString] 无法转化为 [ULong].
         */
        @JvmStatic
        public fun create(roomIdString: String): GetRoomApi = create(roomIdString.toULong())
    }

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        with(parameters) {
            append("room_id", roomId.toString())
        }
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetRoomResult>
        get() = GetRoomResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetRoomResult>>
        get() = RESULT_RES

    override fun toString(): String {
        return "GetRoomApi(roomId=$roomId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GetRoomApi) return false

        if (roomId != other.roomId) return false

        return true
    }

    override fun hashCode(): Int {
        return roomId.hashCode()
    }


}

/**
 * Result of [GetRoomApi].
 *
 * @property room 房间信息
 */
@Serializable
public data class GetRoomResult(val room: Room)
