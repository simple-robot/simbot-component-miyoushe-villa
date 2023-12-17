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
@file:Suppress("NON_EXPORTABLE_TYPE")
@file:JsExport
package love.forte.simbot.miyoushe.ws

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import kotlin.js.JsExport

/**
 * 心跳请求命令字
 *
 * ## 心跳检查
 * 登录成功后，需要每隔一段时间发送一次 HeartBeatRequest 到服务端，保活长连接。
 *
 * 服务端超过一分钟没有从长连接读取到新数据，就会将连接标记失活，最终断开连接。
 *
 * 简单实现，建议每20秒一次心跳包，更好的做法是结合其它数据包判断，距离上一个数据包发出过了20秒就发出一个心跳包。
 *
 * `bizType = 6`
 *
 * 参考：[平台 Websocket 数据协议#心跳检查](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E5%BF%83%E8%B7%B3%E6%A3%80%E6%9F%A5)
 *
 * @see BizType.LONG_CONN_HB_KEEPALIVE
 *
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PHeartBeat(
    /**
     * 客户端时间戳，精确到ms
     */
    @ProtoNumber(1) @SerialName("client_timestamp") val clientTimestamp: String
)

/**
 * 心跳返回
 *
 * `bizType = 6`
 *
 * 参考：[平台 Websocket 数据协议#心跳检查](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E5%BF%83%E8%B7%B3%E6%A3%80%E6%9F%A5)
 *
 *
 * @see PHeartBeat
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PHeartBeatReply(
    /**
     * 错误码 非0表示失败
     */
    @ProtoNumber(1) val code: Int = 0,
    /**
     * 服务端时间戳，精确到ms
     */
    @ProtoNumber(2) @SerialName("server_timestamp") val serverTimestamp: ULong
)
