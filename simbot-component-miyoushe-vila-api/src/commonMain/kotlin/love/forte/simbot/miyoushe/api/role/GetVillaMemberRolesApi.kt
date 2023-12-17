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

package love.forte.simbot.miyoushe.api.role

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmStatic


/**
 * [获取大别野下所有身份组](https://webstatic.mihoyo.com/vila/bot/doc/role_api/get_villa_member_roles.html)
 *
 * 获取大别野下所有身份组
 *
 * `GET /vila/api/bot/platform/getVillaMemberRoles`
 *
 * @author ForteScarlet
 */
public class GetVillaMemberRolesApi private constructor() : MiyousheVillaGetApi<GetVillaMemberRolesResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getVillaMemberRoles"
        private val RESULT_RES = ApiResult.serializer(GetVillaMemberRolesResult.serializer())
        private val INSTANCE = GetVillaMemberRolesApi()

        /**
         * Get instance of [GetVillaMemberRolesApi].
         */
        @JvmStatic
        public fun create(): GetVillaMemberRolesApi = INSTANCE
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetVillaMemberRolesResult>
        get() = GetVillaMemberRolesResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetVillaMemberRolesResult>>
        get() = RESULT_RES

    override fun toString(): String {
        return "GetVillaMemberRolesApi()"
    }


}

/**
 * Result of [GetVillaMemberRolesApi]
 *
 * @property list role list
 *
 */
@Serializable
public data class GetVillaMemberRolesResult(val list: List<VillaRole> = emptyList())
