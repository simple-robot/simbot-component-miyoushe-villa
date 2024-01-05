/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

package love.forte.simbot.miyoushe.stdlib.bot

import love.forte.simbot.miyoushe.ws.PLogin
import love.forte.simbot.miyoushe.ws.PLoginReply

/**
 * WS中使用 [PLogin] 登录失败
 */
@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
public class PLoginFailedException(
    public val reply: PLoginReply,
    message: String? = null, cause: Throwable? = null
) : RuntimeException(message ?: reply.toString(), cause)

/**
 * 当 Bot 已经被关闭
 */
public class BotWasCancelledException(message: String?, cause: Throwable?) : IllegalStateException(message, cause)
