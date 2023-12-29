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

package love.forte.simbot.component.miyoushe.internal.message

import kotlinx.serialization.json.Json
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaComponent
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.message.*
import love.forte.simbot.component.miyoushe.requestResultBy
import love.forte.simbot.message.Messages
import love.forte.simbot.message.MessagesBuilder
import love.forte.simbot.message.buildMessages
import love.forte.simbot.miyoushe.api.msg.*
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.QuoteMessage
import love.forte.simbot.miyoushe.event.SendMessage


/**
 *
 * @author ForteScarlet
 */
internal class VillaReceivedMessageContentImpl(private val bot: VillaBot, override val source: Event<SendMessage>) :
    VillaReceivedMessageContent() {


    override val messages: Messages by lazy(LazyThreadSafetyMode.PUBLICATION) {
        source.extendData.toMessages(bot.source.apiDecoder)
    }

    override suspend fun delete(): Boolean {
        val result =
            RecallMessageApi.create(source.extendData.msgUid, source.extendData.roomId, source.extendData.sendAt)
                .requestResultBy(bot, source.extendData.villaIdStrValue)

        // TODO 只有重复删除时才返回false?

        return result.isSuccess
    }
}


internal fun SendMessage.toMessages(decoder: Json): Messages {
    return when (objectName) {
        SendMessage.OBJECT_NAME_TEXT -> {
            val msgContentInfo =
                decoder.decodeFromString(MsgContentInfo.TextSerializer, content)

            msgContentInfo.textToMessages(quoteMsg)
        }

        SendMessage.OBJECT_NAME_POST -> {
            val msgContentInfo = decoder.decodeFromString(MsgContentInfo.PostSerializer, content)
            msgContentInfo.postToMessages(quoteMsg)
        }

        else -> Messages.emptyMessages()
    }


}

/**
 * 不解析 [MsgContentInfo.mentionedInfo]
 */
internal fun MsgContentInfo<TextMsgContent>.textToMessages(quoteMsg: QuoteMessage?): Messages {
    return buildMessages {
        quoteInfo?.appendTo(this)
        quoteMsg?.appendTo(this)

        content.appendTo(this)
        // mention 在 text entity 中处理
        panel?.appendTo(this)
    }
}

/**
 * 不解析 [PostMsgContent]
 *
 */
internal fun MsgContentInfo<PostMsgContent>.postToMessages(quoteMsg: QuoteMessage?): Messages {
    return buildMessages {
        quoteInfo?.appendTo(this)
        quoteMsg?.appendTo(this)
        mentionedInfo?.appendTo(this)
        panel?.appendTo(this)
    }
}

/**
 * [ImgMsgContent] 解析为 [VillaImgMsgContent]
 *
 */
internal fun MsgContentInfo<ImgMsgContent>.imgToMessages(quoteMsg: QuoteMessage?): Messages {
    return buildMessages {
        quoteInfo?.appendTo(this)
        quoteMsg?.appendTo(this)
        append(VillaImgMsgContent(content))
        mentionedInfo?.appendTo(this)
        panel?.appendTo(this)
    }
}

private fun QuoteMessage.appendTo(builder: MessagesBuilder) {
    builder.append(VillaQuoteMessage(this))
}

private fun TextMsgContent.appendTo(builder: MessagesBuilder) {
    var lastIndex: Int = -1
    var lastTextAppended = false
    val styleElements = mutableListOf<VillaStyleText>()
    for (entity in entities) {
        val entity0 = entity.entity
        if (entity0 is TextMsgContent.EntityContent.Style) {
            // 保留 lastIndex, 添加 style buf 然后跳过
            styleElements.add(VillaStyleText(text.substring(entity.offset, entity.offset + entity.length), entity0))
            continue
        }

        if (entity.offset != 0) {
            if (lastIndex == -1) {
                // first entity, but not first text
                // append text, from 0
                if (!lastTextAppended) {
                    builder.append(text.substring(entity.offset))
                    lastTextAppended = true
                }
            } else {
                // not first.
                if (!lastTextAppended) {
                    builder.append(text.substring(lastIndex, entity.offset))
                    lastTextAppended = true
                }
            }
        }

        if (styleElements.isNotEmpty()) {
            styleElements.forEach { builder.append(it) }
            styleElements.clear()
        }

        entity.entity.appendTo(this, entity, builder)

        val newLastIndex = entity.offset + entity.length
        if (lastIndex != newLastIndex) {
            lastIndex = newLastIndex
            lastTextAppended = false
        }
    }

    if (lastIndex < text.lastIndex) {
        if (lastIndex == -1) {
            builder.text(text)
        } else {
            builder.text(text.substring(lastIndex, text.length))
        }
    }

    styleElements.forEach { builder.append(it) }
}

private fun TextMsgContent.EntityContent.appendTo(
    content: TextMsgContent,
    entity: TextMsgContent.Entity,
    builder: MessagesBuilder
) {
    when (this) {
        is TextMsgContent.EntityContent.Link -> {
            builder.append(VillaLink(text = entity.substring(content.text), link = this))
        }

        TextMsgContent.EntityContent.MentionAll -> builder.atAll()
        is TextMsgContent.EntityContent.MentionedRobot -> {
            builder.at(botId.ID, atType = VillaComponent.MENTION_BOT_TYPE)
        }

        is TextMsgContent.EntityContent.MentionedUser -> builder.at(userId.ID)

        // 在外层处理
        is TextMsgContent.EntityContent.Style -> {
            // nothing.
        }

        is TextMsgContent.EntityContent.VillaRoomLink -> {
            builder.append(VillaVillaRoomLink(entity.substring(content.text), this))
        }
    }
}

private fun MentionedInfo.appendTo(builder: MessagesBuilder) {
    when (type) {
        MentionedInfo.TYPE_MENTION_MEMBER -> {
            userIdList?.forEach { id ->
                builder.at(id.ID)
            }
        }

        MentionedInfo.TYPE_MENTION_ALL -> builder.atAll()
    }
}

private fun QuoteInfo.appendTo(builder: MessagesBuilder) {
    builder.append(VillaQuote(this))
}

private fun Panel.appendTo(builder: MessagesBuilder) {
    builder.append(VillaPanel(this))
}
