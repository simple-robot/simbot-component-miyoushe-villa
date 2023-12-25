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
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.api.msg.TextMsgContent


/**
 * 接收到的消息中的 [TextMsgContent.EntityContent.Link]
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.link")
public data class VillaLink(val source: TextMsgContent.EntityContent.Link) : VillaMessageElement<VillaLink>() {

    /**
     * @see TextMsgContent.EntityContent.Link.url
     */
    val url: String get() = source.url

    /**
     * @see TextMsgContent.EntityContent.Link.requiresBotAccessToken
     */
    val requiresBotAccessToken: Boolean get() = source.requiresBotAccessToken

    override val key: Message.Key<VillaLink>
        get() = Key

    public companion object Key : Message.Key<VillaLink> {
        override fun safeCast(value: Any): VillaLink? = doSafeCast(value)
    }
}
