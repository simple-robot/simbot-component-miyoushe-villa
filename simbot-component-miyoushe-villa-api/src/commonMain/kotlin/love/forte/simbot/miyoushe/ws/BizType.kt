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

import kotlin.js.JsExport
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [bizType参数表](https://webstatic.mihoyo.com/vila/bot/doc/websocket/#biztype%E5%8F%82%E6%95%B0%E8%A1%A8)
 *
 * @author ForteScarlet
 */

public enum class BizType(@get:JvmName("getValue") public val value: UInt, public val protobuf: Boolean) {
    /** 上行	出现了必然是bug或数据异常 */
    UPLINK(0u, false),

    /** 长连接心跳保活协议(ProtoBuf) */
    LONG_CONN_HB_KEEPALIVE(6u, true),

    /** 长连接登录协议(ProtoBuf) */
    LONG_CONN_LOGIN(7u, true),

    /** 长连接登出协议(ProtoBuf) */
    LONG_CONN_LOGOUT(8u, true),

    /** 服务关闭协议（无字段） */
    SHUTDOWN(52u, false),

    /** 踢下线协议（ProtoBuf） */
    KICK_OFF(53u, false),

    /** 机器人开放平台事件（protoBuf） */
    BOT_OPEN(30001u, true);

    public companion object {
        /**
         * 通过 `bizType` 值反向寻找指定的 [BizType].
         *
         * @throws NoSuchElementException 如果没有匹配的目标
         */
        @JvmStatic
        @JvmName("fromValue")
        public fun fromValue(value: UInt): BizType = when (value) {
            0u -> UPLINK
            6u -> LONG_CONN_HB_KEEPALIVE
            7u -> LONG_CONN_LOGIN
            8u -> LONG_CONN_LOGOUT
            52u -> SHUTDOWN
            53u -> KICK_OFF
            30001u -> BOT_OPEN
            else -> throw NoSuchElementException("bizType=$value")
        }
    }
}
