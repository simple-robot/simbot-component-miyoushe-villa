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
import love.forte.simbot.message.PlainText
import love.forte.simbot.message.Text
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.api.msg.TextMsgContent


/**
 * 更多文本样式消息。
 * 文本样式不会作为 [PlainText] 使用（因为其对应的 [text] 已经直接作为 [Text] 被追加了）
 *
 * [VillaStyleText] 在作为用于发送的消息时，[text] **无效**，
 * 且 [offset] 与 [length] 会直接相对于当前解析过程中的正文信息。
 * 而当前解析过程中的文本信息除了 [PlainText] 以外，还可能由其他消息类型进行填充（例如 [VillaVillaRoomLink] 会填充房间名称信息），
 * 因此 [VillaStyleText] 作为发送用的消息（且不是直接使用 [VillaReceivedMessageContent] 原样发送时）需要仔细确认其参数是否正确。
 *
 * 如果希望发送复杂的消息结构体，可考虑直接使用 [VillaSendMessage]。
 *
 * @property offset 偏移量。当接收时此即为 `entity` 中的原值，参考 [TextMsgContent.Entity.offset]
 * @property length 长度。当接收时此即为 `entity` 中的原值，参考 [TextMsgContent.Entity.length]
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.style.text")
public data class VillaStyleText(
    val text: String,
    val offset: Int,
    val length: Int,
    val style: TextMsgContent.EntityContent.Style
) :
    VillaMessageElement<VillaStyleText>() {

    override val key: Message.Key<VillaStyleText>
        get() = Key

    public companion object Key : Message.Key<VillaStyleText> {
        override fun safeCast(value: Any): VillaStyleText? = doSafeCast(value)

        /**
         * 构建一个**用于发送**的 [VillaStyleText]。
         * 发送时 [text] 无效因此无需填写，默认为空字符串。
         *
         * @param fontStyle 参考 [TextMsgContent.EntityContent.Style.fontStyle]
         */
        @JvmStatic
        public fun create(offset: Int, length: Int, fontStyle: String): VillaStyleText =
            VillaStyleText("", offset, length, TextMsgContent.EntityContent.Style(fontStyle))
    }
}
