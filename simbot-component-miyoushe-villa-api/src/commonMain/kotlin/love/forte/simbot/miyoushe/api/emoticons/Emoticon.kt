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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.jvm.JvmName

/**
 * [表态表情 Emoticon](https://webstatic.mihoyo.com/vila/bot/doc/emoticon_api/)
 *
 */
@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
@Serializable
public data class Emoticon(
    /**
     * 表情 id
     */
    @SerialName("emoticon_id")
    @get:JvmName("getEmoticonId")
    val emoticonId: ULong,
    /**
     * 描述文本
     */
    @SerialName("describe_text")
    val describeText: String,
    /**
     * 表情图片链接
     */
    val icon: String,
) {
    val emoticonIdStrValue: String get() = emoticonId.toString()
}
