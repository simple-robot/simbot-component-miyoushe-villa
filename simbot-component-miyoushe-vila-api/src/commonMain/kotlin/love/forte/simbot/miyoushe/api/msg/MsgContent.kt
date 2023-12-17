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
import kotlin.jvm.JvmName


/**
 * [文本消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/text_msg_content.html)
 */
@Serializable
public data class TextMsgContent(
    val text: String,
    val entities: List<Entity> = emptyList()
) : MsgContent() {
    public companion object {
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
        @get:JvmName("getOffset")
        val offset: ULong,
        @get:JvmName("getLength")
        val length: ULong,
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
        public data class MentionedRobot(val botId: String) : EntityContent() {
            public companion object {
                public const val TYPE: String = "mentioned_robot"
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
        public data class MentionedUser(@get:JvmName("getUserId") val userId: ULong) : EntityContent() {
            val userIdStrValue: String get() = userId.toString()

            public companion object {
                public const val TYPE: String = "mentioned_user"
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
            val villaId: ULong,
            @SerialName("room_id")
            @get:JvmName("getRoomId")
            val roomId: UInt,
        ) : EntityContent() {
            val villaIdStrValue: String get() = villaId.toString()
            val roomIdStrValue: String get() = roomId.toString()

            public companion object {
                public const val TYPE: String = "villa_room_link"
            }
        }

        /**
         * 提及全员
         */
        @Serializable
        @SerialName(MentionAll.TYPE)
        public data object MentionAll : EntityContent() {
            public const val TYPE: String = "mention_all"
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
            public companion object {
                public const val TYPE: String = "link"
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
            public companion object {
                public const val TYPE: String = "style"

                /** 加粗 */
                public const val STYLE_BOLD: String = "bold"

                /** 斜体 */
                public const val STYLE_ITALIC: String = "italic"

                /** 删除线 */
                public const val STYLE_STRIKETHROUGH: String = "strikethrough"

                /** 分割线 */
                public const val STYLE_UNDERLINE: String = "underline"
            }
        }

    }


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
 * [米游社主站帖子](https://webstatic.mihoyo.com/vila/bot/doc/message_api/msg_define/post_msg_content.html)
 *
 */
@Serializable
public data class PostMsgContent(@SerialName("post_id") val postId: String) : MsgContent() {
    public companion object {
        public const val OBJECT_NAME: String = "MHY:Post"
    }
}
