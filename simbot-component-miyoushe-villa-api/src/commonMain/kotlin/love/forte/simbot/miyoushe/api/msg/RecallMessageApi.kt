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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

// TODO 似乎说这应该是 POST
/**
 * [撤回消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/recall_message.html)
 *
 * `GET /vila/api/bot/platform/recallMessage`
 *
 * @author ForteScarlet
 */
public class RecallMessageApi private constructor(override val body: Body) : MiyousheVillaPostApi<Unit>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/recallMessage"

        /**
         * Create an instance of [RecallMessageApi].
         *
         * @param msgUid 消息 id
         * @param roomId 房间 id
         * @param msgTime 消息发送时间，对应消息回调事件中的send_at字段
         *
         */
        @JvmName("create")
        @JvmStatic
        public fun create(msgUid: String, roomId: ULong, msgTime: Long?): RecallMessageApi =
            RecallMessageApi(Body(msgUid, roomId, msgTime))
    }

    @Serializable
    public data class Body(
        @SerialName("msg_uid")
        val msgUid: String,
        @SerialName("room_id")
        @Serializable(ULongWriteStringSerializer::class)
        val roomId: ULong,
        @SerialName("msg_time")
        val msgTime: Long?
    )

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<Unit>>
        get() = ApiResult.emptySerializer()

    override fun toString(): String {
        return "RecallMessageApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecallMessageApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }


}
