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

package love.forte.simbot.component.miyoushe.internal.message

import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.component.miyoushe.message.VillaSendSingleMessageReceipt
import love.forte.simbot.component.miyoushe.requestResultBy
import love.forte.simbot.miyoushe.api.msg.RecallMessageApi
import love.forte.simbot.miyoushe.api.msg.SendMessageResult


internal class VillaSendSingleMessageReceiptImpl(
    private val bot: VillaBotImpl,
    private val villaId: String,
    private val roomId: ULong,
    private val msgTime: Long?,
    override val result: SendMessageResult
) : VillaSendSingleMessageReceipt() {
    override suspend fun delete(): Boolean {
        val result = RecallMessageApi.create(result.botMsgId, roomId, msgTime).requestResultBy(bot, villaId)
        return result.isSuccess
    }
}
