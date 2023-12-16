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

/**
 * Shutdown
 *
 * `bizType = 52`
 *
 * 无字段，客户端收到该 bizType 的命令时需要登出，断开连接并重连。
 *
 * 参考：[平台 Websocket 数据协议#Shutdown](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#Shutdown)
 *
 * @see BizType.SHUTDOWN
 *
 * @author ForteScarlet
 */
@Serializable
public object Shutdown
