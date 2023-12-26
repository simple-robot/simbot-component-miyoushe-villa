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
import kotlin.jvm.JvmStatic

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

        @JvmStatic
        public fun textBuilder(): MsgContentInfoBuilder<TextMsgContentBuilder, TextMsgContent> =
            MsgContentInfoBuilder(MsgContent.textBuilder())

        @JvmStatic
        public fun imgBuilder(): MsgContentInfoBuilder<ImgMsgContentBuilder, ImgMsgContent> =
            MsgContentInfoBuilder(MsgContent.imgBuilder())

        @JvmStatic
        public fun postBuilder(): MsgContentInfoBuilder<PostMsgContentBuilder, PostMsgContent> =
            MsgContentInfoBuilder(MsgContent.postBuilder())
    }
}

public inline fun buildTextMsgContentInfo(block: MsgContentInfoBuilder<TextMsgContentBuilder, TextMsgContent>.() -> Unit): MsgContentInfo<TextMsgContent> =
    MsgContentInfo.textBuilder().also(block).build()

public inline fun buildImgMsgContentInfo(block: MsgContentInfoBuilder<ImgMsgContentBuilder, ImgMsgContent>.() -> Unit): MsgContentInfo<ImgMsgContent> =
    MsgContentInfo.imgBuilder().also(block).build()

public inline fun buildPostMsgContentInfo(block: MsgContentInfoBuilder<PostMsgContentBuilder, PostMsgContent>.() -> Unit): MsgContentInfo<PostMsgContent> =
    MsgContentInfo.postBuilder().also(block).build()

/**
 * Builder for [MsgContentInfo].
 */
public class MsgContentInfoBuilder<B : MsgContent.Builder<C>, C : MsgContent>(public val builder: B) {
    public var mentionedInfo: MentionedInfo? = null //  = MentionedInfo.builder()
    public var quoteInfo: QuoteInfo? = null //  = QuoteInfo.builder()
    public var panel: Panel? = null //  = Panel.Builder()

    /**
     * in [builder].
     */
    public inline fun content(block: B.() -> Unit) {
        builder.also(block)
    }

    public inline fun mentionedInfo(block: MentionedInfo.Builder.() -> Unit) {
        mentionedInfo = MentionedInfo.builder().also(block).build()
    }

    public inline fun quoteInfo(block: QuoteInfo.Builder.() -> Unit) {
        quoteInfo = QuoteInfo.builder().also(block).build()
    }

    public inline fun panel(block: Panel.Builder.() -> Unit) {
        panel = Panel.Builder().also(block).build()
    }

    public fun build(): MsgContentInfo<C> {
        return MsgContentInfo(
            content = builder.build(),
            mentionedInfo = mentionedInfo,
            quoteInfo = quoteInfo,
            panel = panel,
        )
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
public sealed class MsgContent {

    /**
     * Builder interface for [MsgContent]
     */
    public interface Builder<T : MsgContent> {
        public fun build(): T
    }

    public companion object {
        @JvmStatic
        public fun textBuilder(): TextMsgContentBuilder = TextMsgContentBuilder()

        @JvmStatic
        public fun imgBuilder(): ImgMsgContentBuilder = ImgMsgContentBuilder()

        @JvmStatic
        public fun postBuilder(): PostMsgContentBuilder = PostMsgContentBuilder()
    }
}

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
 * 根据 [MsgContent] 的具体类型进行序列化
 *
 */
@Suppress("UNCHECKED_CAST")
@ExperimentalSimbotApi
public fun MsgContentInfo<*>.serialize(format: StringFormat): String = when (content) {
    is ImgMsgContent -> format.encodeToString(MsgContentInfo.ImgSerializer, this as MsgContentInfo<ImgMsgContent>)
    is PostMsgContent -> format.encodeToString(MsgContentInfo.PostSerializer, this as MsgContentInfo<PostMsgContent>)
    is TextMsgContent -> format.encodeToString(MsgContentInfo.TextSerializer, this as MsgContentInfo<TextMsgContent>)
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

        private val MENTION_ALL = MentionedInfo(TYPE_MENTION_ALL, null)

        /**
         * 以 [TYPE_MENTION_ALL] 为 type 构建 [MentionedInfo]
         */
        @JvmStatic
        public fun mentionAll(): MentionedInfo = MENTION_ALL

        /**
         * 以 [TYPE_MENTION_MEMBER] 为 type 构建 [MentionedInfo]
         */
        @JvmStatic
        public fun mentionMembers(idList: List<String>): MentionedInfo = MentionedInfo(TYPE_MENTION_MEMBER, idList)

        @JvmStatic
        public fun builder(): Builder = Builder()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public class Builder {
        public var type: Int? = null
        public var userIdList: MutableList<String> = mutableListOf()

        public fun build(): MentionedInfo {
            if (type == TYPE_MENTION_ALL && userIdList.isEmpty()) {
                return MENTION_ALL
            }

            return MentionedInfo(
                type = type ?: error("Required 'type' is null"),
                userIdList = userIdList.toList()
            )
        }
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
) {
    public companion object {
        @JvmStatic
        public fun builder(): Builder = Builder()
    }

    /**
     * Builder for [QuoteInfo].
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public class Builder {
        public var quotedMessageId: String? = null
        public var quotedMessageSendTime: Long? = null
        public var originalMessageId: String? = null
        public var originalMessageSendTime: Long? = null

        public fun build(): QuoteInfo {
            val quotedMessageId = quotedMessageId ?: error("Required 'quotedMessageId' is null")
            val quotedMessageSendTime = quotedMessageSendTime ?: error("Required 'quotedMessageSendTime' is null")

            return QuoteInfo(
                quotedMessageId = quotedMessageId,
                quotedMessageSendTime = quotedMessageSendTime,
                originalMessageId = originalMessageId ?: quotedMessageId,
                originalMessageSendTime = originalMessageSendTime ?: quotedMessageSendTime,
            )
        }
    }


}
