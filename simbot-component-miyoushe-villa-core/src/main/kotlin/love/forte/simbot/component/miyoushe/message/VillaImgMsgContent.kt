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

package love.forte.simbot.component.miyoushe.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.api.msg.ImgMsgContent
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL


/**
 *
 * 大别野图片消息正文作为消息元素。
 *
 * @see ImgMsgContent
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.img_msg_content")
public data class VillaImgMsgContent(val source: ImgMsgContent) : VillaMessageElement<VillaImgMsgContent>(),
    Image<VillaImgMsgContent> {

    override val id: ID
        get() = source.url.ID

    public val url: String
        get() = source.url

    public val size: ImgMsgContent.Size?
        get() = source.size

    val fileSize: Int?
        get() = source.fileSize

    @JvmSynthetic
    override suspend fun resource(): Resource = URL(source.url).toResource()

    override val key: Message.Key<VillaImgMsgContent>
        get() = Key

    public companion object Key : Message.Key<VillaImgMsgContent> {
        override fun safeCast(value: Any): VillaImgMsgContent? = doSafeCast(value)
    }
}
