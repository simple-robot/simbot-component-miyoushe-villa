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
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [置顶消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/pin_message.html)
 *
 * `POST /vila/api/bot/platform/pinMessage`
 *
 * @author ForteScarlet
 */
public class PinMessageApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/pinMessage"

        /**
         * Create an instance of [PinMessageApi]
         *
         * @param msgUid 消息 id
         * @param isCancel 是否取消置顶
         * @param roomId 房间 id
         * @param sendAt 发送时间
         *
         */
        @JvmName("create")
        @JvmStatic
        public fun create(
            msgUid: String, isCancel: Boolean, roomId: ULong, sendAt: Long
        ): PinMessageApi = PinMessageApi(Body(msgUid, isCancel, roomId, sendAt))
    }

    override val path: String
        get() = PATH


    /**
     * @property msgUid 消息 id
     * @property isCancel 是否取消置顶
     * @property roomId 房间 id
     * @property sendAt 发送时间
     *
     */
    @Serializable
    public data class Body(
        @SerialName("msg_uid") val msgUid: String,
        @SerialName("is_cancel") val isCancel: Boolean,
        @SerialName("room_id")
        @Serializable(ULongWriteStringSerializer::class)
        val roomId: ULong,
        @SerialName("send_at") val sendAt: Long
    )

    override fun toString(): String {
        return "PinMessageApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PinMessageApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}

/*
msg_uid	string	消息 id
is_cancel	bool	是否取消置顶
room_id	uint64	房间 id
send_at	int64	发送时间
 */
