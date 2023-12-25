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

package love.forte.simbot.component.miyoushe

import kotlinx.serialization.json.Json
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.message.*
import love.forte.simbot.message.*
import love.forte.simbot.miyoushe.api.msg.*
import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.QuoteMessage
import love.forte.simbot.miyoushe.event.SendMessage


/**
 *
 * 从接收到的事件对象 [SendMessage] 中解析消息信息。
 *
 * @author ForteScarlet
 */
public class ReceivedVillaMessageContent(
    private val bot: VillaBot,
    public val source: Event<SendMessage>,
) : ReceivedMessageContent() {

    /**
     * 对 [SendMessage] 解析并得到的 [Messages].
     * 如果希望得到原始的事件 `content` 字符串，从 [source] 中获取。
     *
     * ### 元素处理
     *
     * 在转化为 [Messages] 过程中，会对消息内容进行一定程度的处理。
     *
     * - 如果 [MsgContentInfo.quoteInfo] 和 [SendMessage.quoteMsg] 不为 `null`，则会依次被解析到首位。
     *
     * 当消息为内容为 [TextMsgContent] 类型时：
     *
     * #### [TextMsgContent]
     *
     * - 文本消息（[TextMsgContent.text]）会根据 [TextMsgContent.entities] 中的附加特殊元素进行删减。
     * 例如 text 的值为 `"大家好 @全体成员"`，并且 `entities` 中包含一个 [TextMsgContent.EntityContent.MentionAll]，
     * 其对应的位置 ([offset][TextMsgContent.Entity.offset] 和 [length][TextMsgContent.Entity.length]) 对应了 `"@全体成员"`，
     * 那么最终的解析结果将会是两个 [Message.Element]，分别为 [Text] 类型的 `"大家好 "` 和一个 [AtAll]。
     * 可以注意到，对应位置的特殊字符串 "@全体成员" 已经被替换为了 [AtAll]，而不会再作为文本元素 [Text] 出现。
     *
     * - 书接上条，比较特殊的是 [Style][TextMsgContent.EntityContent.Style]。特殊文本消息允许多条附加，比如一个文本 `"你好"`
     * 可以同时附加多个 [Style][TextMsgContent.EntityContent.Style] 类型。
     * 因此在遇到 [Style][TextMsgContent.EntityContent.Style] 时，会对其临时缓存。
     * 当连续的 [Style][TextMsgContent.EntityContent.Style] 结束后，会一口气将经过的纯文本消息聚合为 [Text],
     * 并在此 [Text] 元素后之后附加对应数量的 [VillaStyleText] 元素。
     * 例如：
     * ```json
     *  {
     *   "content": {
     *     "text": "加粗斜体删除线分割线",
     *     "entities": [{
     *       "offset": 0,
     *       "length": 2,
     *       "entity": {
     *         "type": "style",
     *         "font_style": "bold"
     *       }
     *     }, {
     *       "offset": 0,
     *       "length": 6,
     *       "entity": {
     *         "type": "style",
     *         "font_style": "strikethrough"
     *       }
     *     }, {
     *       "offset": 2,
     *       "length": 5,
     *       "entity": {
     *         "type": "style",
     *         "font_style": "italic"
     *       }
     *     }]
     *   }
     * }
     * ```
     * 那么最终解析的元素有4个，第一个为 [Text], 值为 `"加粗斜体删除线分割线"`，而后续三个元素都是 [VillaStyleText]。
     *
     * - [TextMsgContent] 的情况下会使用 [TextMsgContent.entities] 解析 at 或 atAll 等提及类型而不是 [MsgContentInfo.mentionedInfo]。
     * - [MsgContentInfo.panel] 会在最后解析，因此它始终在 [Messages] 链的末端（如果有的话）。
     *
     *
     *
     */
    override val messages: Messages by lazy(LazyThreadSafetyMode.PUBLICATION) {
        source.extendData.toMessages(bot.source.apiDecoder)
    }

    override val messageId: ID
        get() = source.extendData.msgUid.ID

    @JvmSynthetic
    override suspend fun delete(): Boolean {
        val result =
            RecallMessageApi.create(source.extendData.msgUid, source.extendData.roomId, source.extendData.sendAt)
                .requestResultBy(bot, source.extendData.villaIdStrValue)

        // TODO 只有重复删除时才返回false

        return result.isSuccess
    }
}

internal fun SendMessage.toMessages(decoder: Json): Messages {
    return when (objectName) {
        SendMessage.OBJECT_NAME_TEXT -> {
            val msgContentInfo =
                decoder.decodeFromString(MsgContentInfo.TextSerializer, content)

            msgContentInfo.toMessages(quoteMsg)
        }

        SendMessage.OBJECT_NAME_POST -> {
            // TODO
            val elements = mutableListOf<Message.Element<*>>()
            val msgContentInfo = decoder.decodeFromString(MsgContentInfo.PostSerializer, content)

            msgContentInfo.toMessages(quoteMsg)
        }

        else -> Messages.emptyMessages()
    }


}

/**
 * 不解析 [MsgContentInfo.mentionedInfo]
 */
internal fun MsgContentInfo<TextMsgContent>.toMessages(quoteMsg: QuoteMessage?): Messages {
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
internal fun MsgContentInfo<PostMsgContent>.toMessages(quoteMsg: QuoteMessage?): Messages {
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
internal fun MsgContentInfo<ImgMsgContent>.toMessages(quoteMsg: QuoteMessage?): Messages {
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
        // TODO style 支持重叠
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

        entity.entity.appendTo(builder)

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

private fun TextMsgContent.EntityContent.appendTo(builder: MessagesBuilder) {
    when (this) {
        is TextMsgContent.EntityContent.Link -> {
            builder.append(VillaLink(this))
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
            builder.append(VillaVillaRoomLink(this))
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
