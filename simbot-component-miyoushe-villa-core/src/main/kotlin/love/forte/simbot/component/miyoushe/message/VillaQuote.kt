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
import love.forte.simbot.miyoushe.api.msg.QuoteInfo


/**
 * 大别野引用消息
 *
 * @see QuoteInfo
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
@SerialName("villa.quote")
public data class VillaQuote(val source: QuoteInfo) : VillaMessageElement<VillaQuote>() {

//    public constructor(
//        quotedMessageId: ID,
//        quotedMessageSendTime: Timestamp,
//        originalMessageId: ID,
//        originalMessageSendTime: Timestamp,
//    ): this(QuoteInfo())

    /**
     * 引用消息 id
     * @see QuoteInfo.quotedMessageId
     */
    val quotedMessageId: ID get() = source.quotedMessageId.ID

    /**
     * 引用消息发送时间戳
     * @see QuoteInfo.quotedMessageSendTime
     */
    val quotedMessageSendTime: Timestamp get() = Timestamp.byMillisecond(source.quotedMessageSendTime)

    /**
     * 引用树初始消息 id，和 [quotedMessageId] 保持一致即可
     * @see QuoteInfo.originalMessageId
     */
    val originalMessageId: ID get() = source.originalMessageId.ID

    /**
     * 引用树初始消息发送时间戳，和 [quotedMessageSendTime] 保持一致即可
     * @see QuoteInfo.originalMessageSendTime
     */
    val originalMessageSendTime: Timestamp get() = Timestamp.byMillisecond(source.originalMessageSendTime)

    override val key: Message.Key<VillaQuote>
        get() = Key

    public companion object Key : Message.Key<VillaQuote> {
        override fun safeCast(value: Any): VillaQuote? = doSafeCast(value)
    }
}
