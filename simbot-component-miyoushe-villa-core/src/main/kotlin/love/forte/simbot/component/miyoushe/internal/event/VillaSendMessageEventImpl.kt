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

package love.forte.simbot.component.miyoushe.internal.event

import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.VillaRoom
import love.forte.simbot.component.miyoushe.event.VillaSendMessageEvent
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.component.miyoushe.internal.message.VillaReceivedMessageContentImpl
import love.forte.simbot.component.miyoushe.internal.message.VillaSendSingleMessageReceiptImpl
import love.forte.simbot.component.miyoushe.message.VillaReceivedMessageContent
import love.forte.simbot.component.miyoushe.message.VillaSendMessageReceipt
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.miyoushe.api.msg.MsgContentInfo
import love.forte.simbot.miyoushe.api.msg.QuoteInfo
import love.forte.simbot.miyoushe.api.msg.SendMessageApi
import love.forte.simbot.miyoushe.api.msg.TextMsgContent
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.EventSource
import love.forte.simbot.miyoushe.event.SendMessage


/**
 *
 * @author ForteScarlet
 */
internal class VillaSendMessageEventImpl(
    override val bot: VillaBotImpl,
    override val sourceEvent: Event<SendMessage>,
    override val sourceEventSource: EventSource,
) : VillaSendMessageEvent() {
    override val messageContent: VillaReceivedMessageContent = VillaReceivedMessageContentImpl(bot, sourceEvent)
    override suspend fun author(): VillaMember = with(sourceEvent.extendData) {
        bot.memberInternal(fromUserId, villaIdStrValue)
    }

    override suspend fun channel(): VillaRoom = with(sourceEvent.extendData) {
        bot.roomInternal(roomId, villaIdStrValue)
    }

    override suspend fun reply(message: Message): VillaSendMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun reply(message: MessageContent): VillaSendMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun reply(text: String): VillaSendMessageReceipt {
        val roomId = sourceEventExtend.roomId
        val result = SendMessageApi.create(
            roomId = roomId,
            msgContent = MsgContentInfo(
                TextMsgContent(text = text), quoteInfo = QuoteInfo(
                    quotedMessageId = sourceEventExtend.msgUid,
                    quotedMessageSendTime = sourceEventExtend.sendAt
                )
            ),
            decoder = bot.source.apiDecoder
        ).requestDataBy(bot, sourceEventExtend.villaIdStrValue)

        return VillaSendSingleMessageReceiptImpl(bot, sourceEventExtend.villaIdStrValue, roomId, null, result)
    }
}
