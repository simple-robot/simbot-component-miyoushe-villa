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
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

/**
 * ## 踢下线
 * 新连接登录时，服务端如果发现相同机器人下已经有一个长连接时，就会向旧连接发送Kickoff协议，踢出旧连接。
 *
 * 客户端收到Kickoff协议表示当前设备已经被踢下线，需要断开连接并且不再重连
 *
 * `bizType = 53`
 *
 * 参考：[平台 Websocket 数据协议#踢下线](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#%E8%B8%A2%E4%B8%8B%E7%BA%BF)
 *
 * @see BizType.KICK_OFF
 *
 * @author ForteScarlet
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PKickOff(
    /**
     * 踢出原因状态码
     */
    @ProtoNumber(1) val code: Int = 0,

    /**
     * 状态码对应的文案
     */
    @ProtoNumber(2) val reason: String? = null
)
