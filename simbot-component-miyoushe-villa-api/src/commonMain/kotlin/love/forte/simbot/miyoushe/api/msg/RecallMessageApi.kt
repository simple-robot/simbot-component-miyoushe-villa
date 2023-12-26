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

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [撤回消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/recall_message.html)
 *
 * `GET /vila/api/bot/platform/recallMessage`
 *
 * @author ForteScarlet
 */
public class RecallMessageApi private constructor(
    private val msgUid: String,
    private val roomId: ULong,
    private val msgTime: Long?
) : MiyousheVillaGetApi<Unit>() {
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
            RecallMessageApi(msgUid, roomId, msgTime)
    }

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        with(parameters) {
            append("msg_uid", msgUid)
            append("room_id", roomId.toString())
            msgTime?.also { append("msg_time", it.toString()) }
        }
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<Unit>>
        get() = ApiResult.emptySerializer()

    override fun toString(): String {
        return "RecallMessageApi(msgUid='$msgUid', roomId=$roomId, msgTime=$msgTime)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecallMessageApi) return false

        if (msgUid != other.msgUid) return false
        if (roomId != other.roomId) return false
        if (msgTime != other.msgTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = msgUid.hashCode()
        result = 31 * result + roomId.hashCode()
        result = 31 * result + msgTime.hashCode()
        return result
    }


}
