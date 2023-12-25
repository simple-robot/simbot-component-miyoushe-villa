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

@file:JvmMultifileClass
@file:JvmName("MsgContentInfos")

package love.forte.simbot.miyoushe.api.msg

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import love.forte.simbot.ExperimentalSimbotApi
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * [消息 api](https://webstatic.mihoyo.com/vila/bot/doc/message_api/).
 * [MsgContentInfo] 是消息的最外层结构，发送消息时的 msg_content 参数就是将 MsgContentInfo 对象经 json 序列化后得到的字符串
 *
 * @property content 消息内容
 * @property mentionedInfo 消息的提及信息
 * @property quoteInfo 引用消息的信息
 * @property panel 组件面板
 *
 */
@Serializable
public data class MsgContentInfo<C : MsgContent>(
    val content: C,
    val mentionedInfo: MentionedInfo? = null,
    val quoteInfo: QuoteInfo? = null,
    val panel: Panel? = null
) {
    public companion object {
        public val TextSerializer: KSerializer<MsgContentInfo<TextMsgContent>> = serializer(TextMsgContent.serializer())
        public val PostSerializer: KSerializer<MsgContentInfo<PostMsgContent>> = serializer(PostMsgContent.serializer())
        public val ImgSerializer: KSerializer<MsgContentInfo<ImgMsgContent>> = serializer(ImgMsgContent.serializer())

    }
}

/**
 * [MsgContent](https://webstatic.mihoyo.com/vila/bot/doc/message_api/#msgcontent)
 *
 * MsgContent是一个通用消息接口，不同消息有各自的数据字段
 *
 * @see objectName
 * @see MsgContent.serialize
 *
 */
public sealed class MsgContent

/**
 * 根据 [MsgContent] 的具体类型获取对应的 `object_name` 值。
 */
@ExperimentalSimbotApi
public val MsgContent.objectName: String
    get() = when (this) {
        is TextMsgContent -> TextMsgContent.OBJECT_NAME
        is ImgMsgContent -> ImgMsgContent.OBJECT_NAME
        is PostMsgContent -> PostMsgContent.OBJECT_NAME
    }

/**
 * 根据 [MsgContent] 的具体类型进行序列化
 *
 */
@ExperimentalSimbotApi
public fun MsgContent.serialize(format: StringFormat): String = when (this) {
    is ImgMsgContent -> format.encodeToString(ImgMsgContent.serializer(), this)
    is PostMsgContent -> format.encodeToString(PostMsgContent.serializer(), this)
    is TextMsgContent -> format.encodeToString(TextMsgContent.serializer(), this)
}

/**
 * [MentionedInfo](https://webstatic.mihoyo.com/vila/bot/doc/message_api#mentionedinfo)
 *
 * 如果消息需要@群成员，应该在 mentionedInfo 里定义被提及用户。
 *
 * > Note: 只有文本类型消息可以携带提及信息
 *
 * @property type 提及类型:
 * - 值为 `1`: @全员
 * - 值为 `2`: @部分成员
 * @property userIdList 如果不是提及全员，应该填写被提及的用户 id 列表
 */
@Serializable
public data class MentionedInfo(
    val type: Int,
    @SerialName("user_id_list")
    val userIdList: List<String>? = null
) {
    public companion object {
        /**
         * 提及类型: @全员
         */
        public const val TYPE_MENTION_ALL: Int = 1

        /**
         * 提及类型: @部分成员
         */
        public const val TYPE_MENTION_MEMBER: Int = 2
    }
}

/**
 * [QuoteInfo](https://webstatic.mihoyo.com/vila/bot/doc/message_api/#quoteinfo)
 *
 * @property quotedMessageId 引用消息 id
 * @property quotedMessageSendTime 引用消息发送时间戳
 * @property originalMessageId 引用树初始消息 id，和 [quotedMessageId] 保持一致即可
 * @property originalMessageSendTime 引用树初始消息发送时间戳，和 [quotedMessageSendTime] 保持一致即可
 *
 */
@Serializable
public data class QuoteInfo(
    @SerialName("quoted_message_id")
    val quotedMessageId: String,
    @SerialName("quoted_message_send_time")
    val quotedMessageSendTime: Long,
    @SerialName("original_message_id")
    val originalMessageId: String = quotedMessageId,
    @SerialName("original_message_send_time")
    val originalMessageSendTime: Long = quotedMessageSendTime,
)
