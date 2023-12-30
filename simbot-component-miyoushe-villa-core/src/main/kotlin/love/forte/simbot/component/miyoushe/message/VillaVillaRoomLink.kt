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
import love.forte.simbot.component.miyoushe.utils.toULong
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.api.msg.TextMsgContent


/**
 * 接收到的消息的 [TextMsgContent.EntityContent.VillaRoomLink]。
 *
 * Tips: 类名第一个 `Villa` 是作为组件消息元素的统一前缀，后面的 `VillaRoomLink` 则与原类型保持一致。
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.villa_room_link")
public data class VillaVillaRoomLink(val name: String, val link: TextMsgContent.EntityContent.VillaRoomLink) :
    VillaMessageElement<VillaVillaRoomLink>() {

    val villaId: ID get() = link.villaId.ID
    val roomId: ID get() = link.roomId.ID

    override val key: Message.Key<VillaVillaRoomLink>
        get() = Key

    public companion object Key : Message.Key<VillaVillaRoomLink> {
        override fun safeCast(value: Any): VillaVillaRoomLink? = doSafeCast(value)

        @JvmStatic
        public fun create(name: String, villaId: ID, roomId: ID): VillaVillaRoomLink =
            VillaVillaRoomLink(name, TextMsgContent.EntityContent.VillaRoomLink(villaId.toULong(), roomId.toULong()))
    }
}
