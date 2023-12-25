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
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.style.text")
public data class VillaStyleText(val text: String, val style: TextMsgContent.EntityContent.Style) :
    VillaMessageElement<VillaStyleText>() {

    override val key: Message.Key<VillaStyleText>
        get() = Key

    public companion object Key : Message.Key<VillaStyleText> {
        override fun safeCast(value: Any): VillaStyleText? = doSafeCast(value)
    }
}
