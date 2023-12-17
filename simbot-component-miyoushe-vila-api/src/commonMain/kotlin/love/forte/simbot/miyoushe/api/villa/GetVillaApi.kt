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

package love.forte.simbot.miyoushe.api.villa

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmStatic


/**
 * [获取大别野信息](https://webstatic.mihoyo.com/vila/bot/doc/villa_api/get_villa.html)
 *
 * `GET /vila/api/bot/platform/getVilla`
 *
 * @author ForteScarlet
 */
public class GetVillaApi private constructor() : MiyousheVillaGetApi<VillaResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getVilla"
        private val RESULT_SER = ApiResult.serializer(VillaResult.serializer())
        private val INSTANCE = GetVillaApi()

        /**
         * Get the instance of [GetVillaApi].
         */
        @JvmStatic
        public fun create(): GetVillaApi = INSTANCE
    }

    override val path: String
        get() = PATH

    override val resultSerializer: DeserializationStrategy<VillaResult>
        get() = VillaResult.serializer()

    override val apiResultSerializer: DeserializationStrategy<ApiResult<VillaResult>>
        get() = RESULT_SER

    override fun toString(): String {
        return "GetVillaApi()"
    }


}

/**
 * The result of [GetVillaApi].
 */
@Serializable
public data class VillaResult(val villa: Villa)
