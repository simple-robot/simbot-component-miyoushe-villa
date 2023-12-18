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

@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package love.forte.simbot.miyoushe.api.image

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic


/**
 * [图片转存](https://webstatic.mihoyo.com/vila/bot/doc/img_api/transfer.html)
 *
 * 将非米游社的三方图床图片转存到米游社官方图床。
 *
 * 只有以下后缀的图片资源支持转存：
 * - jpg, jpeg, png, gif, webp
 *
 * `POST /vila/api/bot/platform/transferImage`
 *
 * @author ForteScarlet
 */
public class TransferImageApi private constructor(override val body: Body) :
    MiyousheVillaPostApi<TransferImageResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/transferImage"
        private val RESULT_SER = ApiResult.serializer(TransferImageResult.serializer())

        /**
         * Create an instance of [TransferImageApi]
         *
         *  @param url 三方图床的图片链接
         */
        @JvmStatic
        public fun create(url: String): TransferImageApi = TransferImageApi(Body(url))
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<TransferImageResult>
        get() = TransferImageResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<TransferImageResult>>
        get() = RESULT_SER

    /**
     * Body of [TransferImageApi]
     *
     * @property url 三方图床的图片链接
     */
    @Serializable
    public data class Body(val url: String)

    override fun toString(): String {
        return "TransferImageApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TransferImageApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }


}

/**
 * Result of [TransferImageApi]
 *
 * @property newUrl 新的米游社官方图床的图片链接
 */
@Serializable
public data class TransferImageResult(@SerialName("new_url") val newUrl: String)
