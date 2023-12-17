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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmStatic


/**
 * [获取房间列表信息](https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_villa_group_room_list.html)
 *
 * 获取大别野下的所有分组和房间的列表
 *
 * `GET /vila/api/bot/platform/getVillaGroupRoomList`
 *
 * @author ForteScarlet
 */
public class GetVillaGroupRoomListApi private constructor() : MiyousheVillaGetApi<GetVillaGroupRoomListResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getVillaGroupRoomList"
        private val RESULT_RES = ApiResult.serializer(GetVillaGroupRoomListResult.serializer())
        private val INSTANCE = GetVillaGroupRoomListApi()

        /**
         * Get instance of [GetVillaGroupRoomListApi]
         *
         */
        @JvmStatic
        public fun create(): GetVillaGroupRoomListApi = INSTANCE
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetVillaGroupRoomListResult>
        get() = GetVillaGroupRoomListResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetVillaGroupRoomListResult>>
        get() = RESULT_RES

    override fun toString(): String {
        return "GetVillaGroupRoomListApi()"
    }


}

/**
 * Result of [GetVillaGroupRoomListApi]
 *
 * @property list 房间列表
 *
 */
@Serializable
public data class GetVillaGroupRoomListResult(val list: List<GroupRoom> = emptyList())
