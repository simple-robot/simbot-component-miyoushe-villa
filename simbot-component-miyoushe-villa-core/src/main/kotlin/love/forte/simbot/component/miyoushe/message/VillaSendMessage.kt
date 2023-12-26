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
import kotlinx.serialization.json.Json
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.msg.MsgContentInfo
import love.forte.simbot.miyoushe.api.msg.SendMessageApi
import love.forte.simbot.miyoushe.api.msg.objectName
import love.forte.simbot.miyoushe.api.msg.serialize


/**
 * 一个仅用于发送的、直接提供一个 [SendMessageApi] 的消息元素。
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.msg_content_info")
public data class VillaSendMessage(val objectName: String, val msgContent: String) :
    VillaMessageElement<VillaSendMessage>() {

    @JvmName("createApi")
    public fun createApi(roomId: ULong): SendMessageApi = SendMessageApi.create(roomId, objectName, msgContent)

    override val key: Message.Key<VillaSendMessage>
        get() = Key

    public companion object Key : Message.Key<VillaSendMessage> {
        override fun safeCast(value: Any): VillaSendMessage? = doSafeCast(value)

        @OptIn(ExperimentalSimbotApi::class)
        @JvmStatic
        public fun create(msgContent: MsgContentInfo<*>, decoder: Json = MiyousheVilla.DefaultJson): VillaSendMessage =
            VillaSendMessage(msgContent.content.objectName, msgContent.serialize(decoder))
    }
}
