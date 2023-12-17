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
 * ## 登出
 * 登录成功后，机器人可以通过 LogoutRequest 协议请求登出长连接。
 *
 * 客户端收到 [PLogoutReply] 回复成功后，代表该长连接的用户 `session` 信息已注销，
 * 此时客户端需要主动断链，服务器兜底1分钟心跳超时后也会被动断开
 *
 * `bizType = 8`
 *
 * 参考：[平台 Websocket 数据协议#登出](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E7%99%BB%E5%87%BA)
 *
 * @see BizType.LONG_CONN_LOGOUT
 *
 * @author ForteScarlet
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PLogout(
    /**
     * 长连接侧唯一id，uint64格式
     */
    @ProtoNumber(1) val uid: ULong,
    /**
     * 客户端操作平台枚举
     */
    @ProtoNumber(2) val platform: Int,
    /**
     * 业务所在客户端应用标识，用于在同一个客户端隔离不同业务的长连接通道。
     */
    @ProtoNumber(3) @SerialName("app_id") val appId: Int,
    /**
     * 客户端设备唯一标识
     */
    @ProtoNumber(4) @SerialName("device_id") val deviceId: String,
    /**
     * 区域划分字段，通过uid+app_id+platform+region四个字段唯一确定一条长连接
     */
    @ProtoNumber(5) val region: String
)

/**
 * 登出命令返回
 *
 * `bizType = 8`
 *
 * 参考：[平台 Websocket 数据协议#登出](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E7%99%BB%E5%87%BA)
 *
 * @see BizType.LONG_CONN_LOGOUT
 * @see PLogout
 *
 * @author ForteScarlet
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PLogoutReply(
    /**
     * 错误码 非0表示失败
     */
    @ProtoNumber(1) val code: Int = 0,
    /**
     * 错误信息
     */
    @ProtoNumber(2) val msg: String? = null,
    /**
     * 连接id
     */
    @ProtoNumber(3) @SerialName("conn_id") val connId: ULong
)
