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

package love.forte.simbot.miyoushe.ws

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

/*
// 登录命令
message PLogin {
    // 长连接侧唯一id，uint64格式
    uint64 uid = 1;
    // 用于业务后端验证的token
    string token = 2;
    // 客户端操作平台枚举
    int32 platform = 3;
    // 业务所在客户端应用标识，用于在同一个客户端隔离不同业务的长连接通道。
    int32 app_id = 4;
    string device_id = 5;
    // 区域划分字段，通过uid+app_id+platform+region四个字段唯一确定一条长连接
    string region = 6;
    // 长连内部的扩展字段，是个map
    map<string, string> meta = 7;
}
bizType = 7
 */

/**
 * WebSocket建连成功后，需要通过 LoginRequest 协议携带 token 发起登录。
 *
 * `bizType = 7`
 *
 * 参考 [平台 Websocket 数据协议#登录](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E7%99%BB%E5%BD%95)
 *
 * @see BizType.LONG_CONN_LOGIN
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PLogin(
    /**
     * 长连接侧唯一id，uint64格式
     */
    @ProtoNumber(1)
    val uid: ULong,
    /**
     * 用于业务后端验证的token
     *
     * 机器人 Websocket 鉴权 token，格式为 {villa_id}.{secret}.{bot_id} 。机器人未上线时，villa_id 使用测试别野，上线后可传 0
     */
    @ProtoNumber(2)
    val token: String,
    /**
     * 客户端操作平台枚举
     */
    @ProtoNumber(3)
    val platform: Int,
    /**
     * 业务所在客户端应用标识，用于在同一个客户端隔离不同业务的长连接通道。
     */
    @ProtoNumber(4)
    @SerialName("app_id")
    val appId: Int,

    @ProtoNumber(5)
    @SerialName("device_id")
    val deviceId: String,
    /**
     * 区域划分字段，通过uid+app_id+platform+region四个字段唯一确定一条长连接
     */
    @ProtoNumber(6)
    val region: String,
    /**
     * 长连内部的扩展字段，是个map
     */
    @ProtoNumber(7)
    val meta: Map<String, String> = emptyMap()
)


// bizType = 7
/**
 *
 * `bizType = 7`
 *
 * @see PLogin
 * @see BizType.LONG_CONN_LOGIN
 *
 * @author ForteScarlet
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PLoginReply(
    /**
     * 错误码 非0表示失败
     * 错误码见[附录 网关错误码](https://webstatic.mihoyo.com/vila/bot/doc/websocket/#%E7%BD%91%E5%85%B3%E9%94%99%E8%AF%AF%E7%A0%81)
     */
    @ProtoNumber(1) val code: Int = 0,
    /**
     * 错误信息
     */
    @ProtoNumber(2) val msg: String? = null,
    /**
     * 服务端时间戳，精确到ms
     */
    @ProtoNumber(3) @SerialName("server_timestamp") val serverTimestamp: ULong,
    /**
     * 唯一连接ID
     */
    @ProtoNumber(4) @SerialName("conn_id") val connId: ULong,
)
