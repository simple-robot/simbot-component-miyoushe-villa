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

package love.forte.simbot.component.miyoushe.message

import love.forte.simbot.ID
import love.forte.simbot.message.AggregatedMessageReceipt
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.message.SingleMessageReceipt
import love.forte.simbot.miyoushe.api.msg.SendMessageResult


/**
 *
 * @author ForteScarlet
 */
public interface VillaSendMessageReceipt : MessageReceipt {
    /**
     * 是否发送成功。
     * 能得到此类型即说明消息已发送成功，始终为 `true`。
     */
    override val isSuccess: Boolean
        get() = true
}

/**
 *
 * @author ForteScarlet
 */
public abstract class VillaSendSingleMessageReceipt : VillaSendMessageReceipt, SingleMessageReceipt() {
    public abstract val result: SendMessageResult
    override val id: ID
        get() = result.botMsgId.ID
}

/**
 *
 * @author ForteScarlet
 */
public abstract class VillaSendAggregatedMessageReceipt : VillaSendMessageReceipt, AggregatedMessageReceipt()
