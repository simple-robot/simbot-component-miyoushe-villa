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

package love.forte.simbot.component.miyoushe.internal

import love.forte.simbot.Api4J
import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.VillaRoom
import love.forte.simbot.component.miyoushe.VillaRoomGroup
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.component.miyoushe.internal.message.VillaSendSingleMessageReceiptImpl
import love.forte.simbot.component.miyoushe.message.VillaSendMessageReceipt
import love.forte.simbot.component.miyoushe.requestDataBy
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Role
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.miyoushe.api.msg.MsgContentInfo
import love.forte.simbot.miyoushe.api.msg.SendMessageApi
import love.forte.simbot.miyoushe.api.msg.TextMsgContent
import love.forte.simbot.miyoushe.api.room.RoomBasic
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedFlowItems


/**
 *
 * @author ForteScarlet
 */
internal class VillaRoomImpl(
    override val bot: VillaBotImpl,
    override val room: RoomBasic,
    private val villaId: String,
    override val category: VillaRoomGroup = VillaRoomGroupImpl(room.groupId),
    private val villa: VillaGuild? = null,
) : VillaRoom {
    override val guildId: ID
        get() = villaId.ID

    @OptIn(Api4J::class)
    override val ownerId: ID
        get() = villa?.ownerId ?: guild.ownerId

    override val roles: Items<Role>
        get() = villa?.roles ?: bot.effectedFlowItems {
            guild().roles.collect { emit(it) }
        }

    override suspend fun guild(): VillaGuild {
        if (villa != null) return villa

        return bot.guildInternal(villaId)
    }

    override val members: Items<GuildMember>
        get() = villa?.members ?: bot.effectedFlowItems {
            guild().members.collect { emit(it) }
        }

    override suspend fun send(message: Message): VillaSendMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(message: MessageContent): VillaSendMessageReceipt {
        TODO("Not yet implemented")
    }

    override suspend fun send(text: String): VillaSendMessageReceipt {
        val result =
            SendMessageApi.create(room.roomId, MsgContentInfo(TextMsgContent(text = text))).requestDataBy(bot, villaId)
        return VillaSendSingleMessageReceiptImpl(bot, villaId, room.roomId, null, result)
    }
}
