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
import love.forte.simbot.Timestamp
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.QuoteMessage
import love.forte.simbot.miyoushe.event.SendMessage


/**
 * [SendMessage] 事件中接收到的引用消息内容 [SendMessage.quoteMsg].
 *
 * @see QuoteMessage
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.quote_msg")
public data class VillaQuoteMessage(val source: QuoteMessage) : VillaMessageElement<VillaQuoteMessage>() {

    /**
     * @see QuoteMessage.content
     */
    val content: String get() = source.content

    /**
     * @see QuoteMessage.msgUid
     */
    val msgUid: String get() = source.msgUid

    /**
     * @see QuoteMessage.sendAt
     */
    val sendAt: Timestamp get() = Timestamp.byMillisecond(source.sendAt)

    /**
     * @see QuoteMessage.msgType
     */
    val msgType: String get() = source.msgType

    /**
     * @see QuoteMessage.botMsgId
     */
    val botMsgId: ID get() = source.botMsgId.ID

    /**
     * @see QuoteMessage.fromUserId
     */
    val fromUserId: ID get() = source.fromUserId.ID

    /**
     * @see QuoteMessage.fromUserIdStr
     */
    val fromUserIdStr: ID get() = source.fromUserIdStr.ID

    /**
     * @see QuoteMessage.fromUserNickname
     */
    val fromUserNickname: String get() = source.fromUserNickname

    /**
     * @see QuoteMessage.images
     */
    val images: List<String> get() = source.images

    override val key: Message.Key<VillaQuoteMessage>
        get() = Key

    public companion object Key : Message.Key<VillaQuoteMessage> {
        override fun safeCast(value: Any): VillaQuoteMessage? = doSafeCast(value)
    }
}
