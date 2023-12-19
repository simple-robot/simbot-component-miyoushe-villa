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
 * [获取分组列表](https://webstatic.mihoyo.com/vila/bot/doc/room_api/get_group_list.html)
 *
 * 获取大别野下的所有分组的列表，只返回分组的基本信息，不包含分组内的房间信息。
 *
 * `GET /vila/api/bot/platform/getGroupList`
 *
 * @author ForteScarlet
 */
public class GetGroupListApi private constructor() : MiyousheVillaGetApi<GetGroupListResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getGroupList"
        private val INSTANCE = GetGroupListApi()
        private val RESULT_RES = ApiResult.serializer(GetGroupListResult.serializer())

        /**
         * Get instance of [GetGroupListApi].
         */
        @JvmStatic
        public fun create(): GetGroupListApi = INSTANCE

    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetGroupListResult>
        get() = GetGroupListResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetGroupListResult>>
        get() = RESULT_RES

    override fun toString(): String {
        return "GetGroupListApi()"
    }
}

/**
 * Result of [GetGroupListApi].
 *
 * @property list 分组列表
 */
@Serializable
public data class GetGroupListResult(val list: Group)
