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

package love.forte.simbot.miyoushe.api.emoticons

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmStatic


/**
 * [获取全量表情](https://webstatic.mihoyo.com/vila/bot/doc/emoticon_api/get_all_emoticons.html)
 * @author ForteScarlet
 */
public class GetAllEmoticonsApi private constructor() : MiyousheVillaGetApi<EmoticonList>() {
    public companion object Factory {
        private val INSTANCE = GetAllEmoticonsApi()
        private const val API_PATH = "/vila/api/bot/platform/getAllEmoticons"
        private val apiResultSer = ApiResult.serializer(EmoticonList.serializer())

        /**
         * 构建 [GetAllEmoticonsApi] 实例。
         */
        @JvmStatic
        public fun create(): GetAllEmoticonsApi = INSTANCE

    }

    override val path: String
        get() = API_PATH

    override val resultSerializer: KSerializer<EmoticonList>
        get() = EmoticonList.serializer()

    override val apiResultSerializer: DeserializationStrategy<ApiResult<EmoticonList>>
        get() = apiResultSer

    override fun toString(): String {
        return "GetAllEmoticonsApi()"
    }
}


@Serializable
public data class EmoticonList(val list: List<Emoticon>)


@Serializable
public data class Emoticon(
    /**
     * 表情 id
     */
    @SerialName("emoticon_id") val emoticonId: ULong,
    /**
     * 描述文本
     */
    @SerialName("describe_text") val describeText: String,
    /**
     * 表情图片链接
     */
    val icon: String,
)
