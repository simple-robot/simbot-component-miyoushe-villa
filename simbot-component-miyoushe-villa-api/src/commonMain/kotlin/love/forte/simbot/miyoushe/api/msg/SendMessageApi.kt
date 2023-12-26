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
import kotlinx.serialization.json.Json
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic


/**
 * [发送消息](https://webstatic.mihoyo.com/vila/bot/doc/message_api/send_message.html)
 *
 * `POST /vila/api/bot/platform/sendMessage`
 *
 * @author ForteScarlet
 */
@Serializable
public class SendMessageApi private constructor(override val body: Body) : MiyousheVillaPostApi<SendMessageResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/sendMessage"
        private val RESULT_SER = ApiResult.serializer(SendMessageResult.serializer())

        //region basic
        /**
         * Create an instance of [SendMessageApi].
         *
         * @param roomId 房间 id
         * @param objectName 消息类型
         * @param msgContent 将 MsgContentInfo 结构体序列化后的字符串
         */
        @JvmName("create")
        @JvmStatic
        public fun create(roomId: ULong, objectName: String, msgContent: String): SendMessageApi =
            SendMessageApi(Body(roomId, objectName, msgContent))

        /**
         * Create an instance of [SendMessageApi].
         *
         * @param roomIdString 房间 id 的字符串值
         * @param objectName 消息类型
         * @param msgContent 将 MsgContentInfo 结构体序列化后的字符串
         *
         * @throws NumberFormatException 如果 [roomIdString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(roomIdString: String, objectName: String, msgContent: String): SendMessageApi =
            create(roomIdString.toULong(), objectName, msgContent)
        //endregion

        //region entity
        /**
         * Create an instance of [SendMessageApi].
         *
         * @param roomId 房间 id
         * @param msgContent [MsgContentInfo] 对象，会被序列化为JSON字符串。
         * @param decoder 用于将 [msgContent] 序列化为JSON字符串的序列化器。默认为 [MiyousheVilla.DefaultJson]
         */
        @OptIn(ExperimentalSimbotApi::class)
        @JvmName("create")
        @JvmStatic
        public fun create(
            roomId: ULong,
            msgContent: MsgContentInfo<*>,
            decoder: Json
        ): SendMessageApi =
            create(roomId, msgContent.content.objectName, msgContent.serialize(decoder))

        /**
         * Create an instance of [SendMessageApi].
         *
         * @param roomId 房间 id
         * @param msgContent [MsgContentInfo] 对象，会被序列化为JSON字符串。
         */
        @JvmName("create")
        @JvmStatic
        public fun create(
            roomId: ULong,
            msgContent: MsgContentInfo<*>,
        ): SendMessageApi =
            create(roomId, msgContent, MiyousheVilla.DefaultJson)

        /**
         * Create an instance of [SendMessageApi].
         *
         * @param roomIdString 房间 id 的字符串值
         * @param msgContent [MsgContentInfo] 对象，会被序列化为JSON字符串。
         * @param decoder 用于将 [msgContent] 序列化为JSON字符串的序列化器。默认为 [MiyousheVilla.DefaultJson]
         *
         * @throws NumberFormatException 如果 [roomIdString] 无法转化为 [ULong]
         */
        @JvmStatic
        @JvmOverloads
        public fun create(
            roomIdString: String,
            msgContent: MsgContentInfo<*>,
            decoder: Json = MiyousheVilla.DefaultJson
        ): SendMessageApi =
            create(roomIdString.toULong(), msgContent, decoder)
        //endregion

    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<SendMessageResult>
        get() = SendMessageResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<SendMessageResult>>
        get() = RESULT_SER


    /**
     * @property roomId 房间 id
     * @property objectName 消息类型
     * @property msgContent 将 MsgContentInfo 结构体序列化后的字符串
     */
    @Serializable
    public data class Body(
        @SerialName("room_id") val roomId: ULong,
        @SerialName("object_name") val objectName: String,
        @SerialName("msg_content") val msgContent: String,
    )

    override fun toString(): String {
        return "SendMessageApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SendMessageApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}

/**
 * Result of [SendMessageApi]
 *
 * @property botMsgId 机器人所发送消息的唯一标识符
 */
@Serializable
public data class SendMessageResult(@SerialName("bot_msg_id") val botMsgId: String)
