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

package love.forte.simbot.miyoushe.api.msg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.msg.TextMsgContent.EntityContent.Style.Factory.STYLE_BOLD
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic


/**
 * [文本消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/text_msg_content.html)
 */
@Serializable
public data class TextMsgContent(
    val text: String,
    val entities: List<Entity> = emptyList()
) : MsgContent() {
    public companion object {
        @JvmField
        public val EMPTY: TextMsgContent = TextMsgContent(text = "", entities = emptyList())
        public const val OBJECT_NAME: String = "MHY:Text"
    }

    /**
     * [Entity](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/text_msg_content.html#entity) 定义了文本消息中内嵌的实体信息，根据开发者设定的内容替换对应的文本。
     *
     * 目前支持的内嵌实体类型有：
     *
     * - `mentioned_robot`：提及机器人 (see [EntityContent.MentionedRobot])
     * - `mentioned_user`：@指定用户 (see [EntityContent.MentionedUser])
     * - `mention_all`：@全体成员 (see [EntityContent.MentionAll])
     * - `villa_room_link`：跳转大别野内的房间 (see [EntityContent.VillaRoomLink])
     * - `link`：跳转到外部链接 (see [EntityContent.Link])
     * - `style`: 文本样式 (see [EntityContent.Style])
     *
     * 注：支持在文本消息中内嵌表态表情，但不需要专门指定实体信息，客户端会自动尝试将 方框+描述文本（如\[爱心]) 的文本转化为对应的表情展示出来。
     *
     * @property offset 表示 UTF-16 编码下对应实体在 text 中的起始位置
     * @property length 表示 UTF-16 编码下对应实体的长度
     * @property entity 具体的实体信息
     *
     */
    @Serializable
    public data class Entity(
        val offset: Int,
        val length: Int,
        val entity: EntityContent
    )

    /**
     * 用于 [Entity.entity] 的值类型。
     *
     */
    @Serializable
    public sealed class EntityContent {
        /**
         * 提及机器人
         *
         * ```json
         * {
         *   "entity": {
         *     "type": "mentioned_robot", // 提及机器人
         *     "bot_id": "bot_c85497cf3408304022" // 机器人 id
         *   },
         *   "length": 7,
         *   "offset": 0
         * }
         * ```
         *
         * @property botId 机器人 id
         */
        @Serializable
        @SerialName(MentionedRobot.TYPE)
        public data class MentionedRobot(@SerialName("bot_id") val botId: String) : EntityContent() {
            public companion object Factory : EntityContentBuilderFactory<Builder> {
                public const val TYPE: String = "mentioned_robot"

                @JvmStatic
                override fun builder(): Builder = Builder()
            }

            /**
             * Builder for [MentionedRobot].
             */
            public class Builder : EntityContentBuilder {
                public var botId: String? = null

                override fun build(): MentionedRobot =
                    MentionedRobot(botId = requireNotNull(botId) { "Required 'botId' was null" })
            }
        }

        /**
         * 提及成员
         *
         * ```json
         * {
         *   "entity": {
         *     "type": "mentioned_user", // 提及成员
         *     "user_id": "339501" // 成员id
         *   },
         *   "length": 7,
         *   "offset": 11
         * }
         * ```
         *
         * @property userId 成员id
         */
        @Serializable
        @SerialName(MentionedUser.TYPE)
        public data class MentionedUser(
            @SerialName("user_id")
            @get:JvmName("getUserId")
            @Serializable(ULongWriteStringSerializer::class)
            val userId: ULong
        ) : EntityContent() {
            val userIdStrValue: String get() = userId.toString()

            public companion object Factory : EntityContentBuilderFactory<Builder> {
                public const val TYPE: String = "mentioned_user"

                @JvmStatic
                override fun builder(): Builder = Builder()
            }

            /**
             * Builder for [MentionedUser].
             */
            public class Builder : EntityContentBuilder {
                @JvmSynthetic
                public var userId: ULong? = null

                /**
                 * @throws NumberFormatException
                 */
                public var userIdStrValue: String?
                    get() = userId?.toString()
                    set(value) {
                        userId = value?.toULong()
                    }

                @JvmName("setUserId")
                public fun setUserId(userId: ULong) {
                    this.userId = userId
                }

                @get:JvmName("getUserId")
                public val userIdAsLong: Long? get() = userId?.toLong()

                override fun build(): MentionedUser =
                    MentionedUser(requireNotNull(userId) { "Required 'userId' was null" })
            }
        }

        /**
         * 房间标签，点击会跳转到指定房间（仅支持跳转本大别野的房间）
         *
         * ```json
         * {
         *   "entity": {
         *     // 房间标签，点击会跳转到指定房间（仅支持跳转本大别野的房间）
         *     "type": "villa_room_link",
         *     "villa_id": "15842", // 大别野 id
         *     "room_id": "95337" // 房间 id
         *   },
         *   "length": 6,
         *   "offset": 27
         * }
         * ```
         *
         * @property villaId 大别野 id
         * @property roomId 房间 id
         */
        @Serializable
        @SerialName(VillaRoomLink.TYPE)
        public data class VillaRoomLink(
            @SerialName("villa_id")
            @get:JvmName("getVillaId")
            @Serializable(ULongWriteStringSerializer::class)
            val villaId: ULong,
            @SerialName("room_id")
            @get:JvmName("getRoomId")
            @Serializable(ULongWriteStringSerializer::class)
            val roomId: ULong,
        ) : EntityContent() {
            val villaIdStrValue: String get() = villaId.toString()
            val roomIdStrValue: String get() = roomId.toString()

            public companion object Factory : EntityContentBuilderFactory<Builder> {
                public const val TYPE: String = "villa_room_link"

                @JvmStatic
                override fun builder(): Builder = Builder()
            }

            /**
             * Builder for [VillaRoomLink].
             */
            public class Builder : EntityContentBuilder {
                @JvmSynthetic
                public var villaId: ULong? = null

                @JvmSynthetic
                public var roomId: ULong? = null

                public var villaIdStrValue: String?
                    get() = villaId?.toString()
                    set(value) {
                        villaId = value?.toULong()
                    }

                public var roomIdStrValue: String?
                    get() = roomId?.toString()
                    set(value) {
                        roomId = value?.toULong()
                    }

                @JvmName("setVillaId")
                public fun setVillaId(villaId: ULong) {
                    this.villaId = villaId
                }

                @JvmName("setRoomId")
                public fun setRoomId(roomId: ULong) {
                    this.roomId = roomId
                }

                @get:JvmName("getVillaId")
                public val villaIdAsLong: Long? get() = villaId?.toLong()

                @get:JvmName("getRoomId")
                public val roomIdAsLong: Long? get() = roomId?.toLong()

                override fun build(): VillaRoomLink =
                    VillaRoomLink(
                        villaId = requireNotNull(villaId) { "Required 'villaId' was null" },
                        roomId = requireNotNull(roomId) { "Required 'roomId' was null" },
                    )
            }
        }

        /**
         * 提及全员
         */
        @Serializable
        @SerialName(MentionAll.TYPE)
        public data object MentionAll : EntityContent() {
            public const val TYPE: String = "mention_all"
            public const val CONTENT: String = "@全体成员"

            /**
             * Factory for [Builder].
             */
            @JvmField
            public val Factory: EntityContentBuilderFactory<Builder> = object : EntityContentBuilderFactory<Builder> {
                override fun builder(): Builder = Builder
            }

            @JvmStatic
            public fun builder(): Builder = Factory.builder()

            /**
             * Builder for [MentionAll]
             */
            public object Builder : EntityContentBuilder {
                override fun build(): MentionAll = MentionAll
            }
        }

        /**
         * 跳转外部链接
         *
         * @property url 链接
         * @property requiresBotAccessToken 为 `true` 时，跳转链接会带上含有用户信息的token
         *
         */
        @Serializable
        @SerialName(Link.TYPE)
        public data class Link(
            val url: String,
            @SerialName("requires_bot_access_token")
            val requiresBotAccessToken: Boolean = false
        ) : EntityContent() {
            public companion object Factory : EntityContentBuilderFactory<Builder> {
                public const val TYPE: String = "link"

                @JvmStatic
                override fun builder(): Builder = Builder()
            }

            /**
             * Builder for [Link].
             */
            public class Builder : EntityContentBuilder {
                public var url: String? = null
                public var requiresBotAccessToken: Boolean = false

                override fun build(): Link = Link(
                    url = requireNotNull(url) { "Required 'url' was null" },
                    requiresBotAccessToken = requiresBotAccessToken
                )
            }
        }

        /**
         * [更多文本样式支持](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/text_msg_content.html#%E6%9B%B4%E5%A4%9A%E6%96%87%E6%9C%AC%E6%A0%B7%E5%BC%8F%E6%94%AF%E6%8C%81)
         *
         * 目前支持4种文本样式，支持对同一段文本重叠使用多种样式（针对同一段offset声明多个entity）：
         *
         * - `bold`：加粗
         * - `italic`：斜体
         * - `strikethrough`：删除线
         * - `underline`：分割线
         *
         * @property fontStyle 样式. 可参考 [Style] 中 `STYLE` 开头的常量属性，例如 [STYLE_BOLD]
         */
        @Serializable
        @SerialName(Style.TYPE)
        public data class Style(@SerialName("font_style") val fontStyle: String) : EntityContent() {
            public companion object Factory : EntityContentBuilderFactory<Builder> {
                public const val TYPE: String = "style"

                /** 加粗 */
                public const val STYLE_BOLD: String = "bold"

                /** 斜体 */
                public const val STYLE_ITALIC: String = "italic"

                /** 删除线 */
                public const val STYLE_STRIKETHROUGH: String = "strikethrough"

                /** 分割线 */
                public const val STYLE_UNDERLINE: String = "underline"

                @JvmStatic
                override fun builder(): Builder = Builder()
            }

            /**
             * Builder for [Style]
             */
            public class Builder : EntityContentBuilder {
                public var fontStyle: String? = null

                public fun bold() {
                    fontStyle = STYLE_BOLD
                }

                public fun italic() {
                    fontStyle = STYLE_ITALIC
                }

                public fun strikethrough() {
                    fontStyle = STYLE_STRIKETHROUGH
                }

                public fun underline() {
                    fontStyle = STYLE_UNDERLINE
                }

                override fun build(): Style =
                    Style(requireNotNull(fontStyle) { "Required 'fontStyle' was null" })
            }
        }
    }
}

public fun TextMsgContent.Entity.substring(text: String): String = text.substring(offset, offset + length)


/**
 * Builder for [TextMsgContent]
 */
public class TextMsgContentBuilder : MsgContent.Builder<TextMsgContent> {
    public var textBuilder: StringBuilder = StringBuilder()

    private var _lastText: String? = null
    private val lastText: String
        get() = _lastText ?: textBuilder.toString().also { _lastText = it }

    /**
     * 设置属性 `text`。实际上是对 [textBuilder] 的操作。
     * @see TextMsgContent.text
     */
    public var text: String
        get() = lastText
        set(value) {
            textBuilder = StringBuilder(value)
            _lastText = value
        }

    public fun appendText(value: String?) {
        textBuilder.append(value)
        _lastText = null
    }

    /**
     * @see TextMsgContent.entities
     */
    public var entities: MutableList<TextMsgContent.Entity> = mutableListOf()

    /**
     * 添加一个 [entity] 到 [entities] 中。
     */
    public fun entity(entity: TextMsgContent.Entity) {
        entities.add(entity)
    }

    override fun build(): TextMsgContent {
        return TextMsgContent(textBuilder.toString(), entities.toList())
    }
}

public inline fun buildTextMsgContent(block: TextMsgContentBuilder.() -> Unit): TextMsgContent =
    TextMsgContentBuilder().also(block).build()


public inline fun TextMsgContentBuilder.entity(block: EntityBuilder.() -> Unit) {
    entity(EntityBuilder().also(block).build())
}

/**
 * Builder for [TextMsgContent.Entity]
 */
public class EntityBuilder {
    public var offset: Int? = null
    public var length: Int? = null
    public var content: TextMsgContent.EntityContent? = null

    public fun build(): TextMsgContent.Entity {
        return TextMsgContent.Entity(
            offset = requireNotNull(offset) { "Required 'offset' was null" },
            length = requireNotNull(length) { "Required 'length' was null" },
            entity = requireNotNull(content) { "Required 'content' was null" }
        )
    }
}

/**
 *
 * e.g.
 * ```kotlin
 * buildTextMsgContent {
 *     text = ""
 *     entity {
 *         offset = 2
 *         length = 1
 *         content(TextMsgContent.EntityContent.Link) {
 *             url = "x"
 *         }
 *     }
 *
 *     entity {
 *         offset = 3
 *         length = 2
 *         content(TextMsgContent.EntityContent.MentionAll.Factory) {
 *             // nothing.
 *         }
 *     }
 *
 *     entity {
 *         offset = 5
 *         length = 10
 *         content(TextMsgContent.EntityContent.Style) {
 *             bold()
 *         }
 *     }
 * }
 * ```
 *
 */
public inline fun <B : EntityContentBuilder> EntityBuilder.content(
    factory: EntityContentBuilderFactory<B>,
    block: B.() -> Unit
) {
    content = factory.builder().also(block).build()
}

/**
 * Builder for [TextMsgContent.EntityContent]
 */
public interface EntityContentBuilder {
    public fun build(): TextMsgContent.EntityContent
}

/**
 * Factory for [EntityContentBuilder]
 */
public interface EntityContentBuilderFactory<B : EntityContentBuilder> {
    public fun builder(): B
}

/**
 * 图片消息
 *
 * > Note: 图片的实际大小不大于10M字节。
 * > 图片链接需为米游社官方域名，第三方域名图片请先使用图片转存接口将其转成米游社域名图片后再发送消息。
 *
 * ```json
 * {
 *     "url": "https://bbs.mihoyo.com/image.png",
 *
 *     "size": {
 *         "width": 500,
 *         "height": 500
 *     },
 * }
 * ```
 *
 * @property url 【必填】图片链接
 * @property size 【选填】图片大小，单位：像素。如果图片长宽比不超过 9:16 或 16:9 , 那么按照开发者定义比例展示缩略图
 * @property fileSize 【选填】原始图片的文件大小，单位：字节
 *
 */
@Serializable
public data class ImgMsgContent(
    val url: String,
    val size: Size? = null,
    @SerialName("file_size") val fileSize: Int? = null
) : MsgContent() {
    public companion object {
        public const val OBJECT_NAME: String = "MHY:Image"
    }

    /**
     * Property type of [ImgMsgContent]
     * @see ImgMsgContent.size
     */
    @Serializable
    public data class Size(val width: Int, val height: Int)
}

/**
 * Builder for [ImgMsgContent]
 */
@Suppress("MemberVisibilityCanBePrivate")
public class ImgMsgContentBuilder : MsgContent.Builder<ImgMsgContent> {
    public var url: String? = null
    public var size: ImgMsgContent.Size? = null
    public var fileSize: Int? = null

    public fun size(width: Int, height: Int): ImgMsgContentBuilder = apply {
        size = ImgMsgContent.Size(width, height)
    }

    override fun build(): ImgMsgContent =
        ImgMsgContent(
            url = requireNotNull(url) { "Required 'url' was null" },
            size,
            fileSize
        )
}

public inline fun buildImgMsgContent(block: ImgMsgContentBuilder.() -> Unit): ImgMsgContent =
    ImgMsgContentBuilder().also(block).build()


/**
 * [米游社主站帖子](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/post_msg_content.html)
 *
 */
@Serializable
public data class PostMsgContent(@SerialName("post_id") val postId: String) : MsgContent() {
    public companion object {
        public const val OBJECT_NAME: String = "MHY:Post"
    }
}

/**
 * Builder for [PostMsgContent]
 */
@Suppress("MemberVisibilityCanBePrivate")
public class PostMsgContentBuilder : MsgContent.Builder<PostMsgContent> {
    public var postId: String? = null

    override fun build(): PostMsgContent =
        PostMsgContent(
            postId = requireNotNull(postId) { "Required 'postId' was null" }
        )
}

public inline fun buildPostMsgContent(block: PostMsgContentBuilder.() -> Unit): PostMsgContent =
    PostMsgContentBuilder().also(block).build()
