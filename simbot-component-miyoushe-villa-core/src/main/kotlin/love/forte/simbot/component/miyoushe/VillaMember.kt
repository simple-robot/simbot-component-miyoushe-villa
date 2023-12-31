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

package love.forte.simbot.component.miyoushe

import love.forte.simbot.ID
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.Timestamp
import love.forte.simbot.action.DeleteSupport
import love.forte.simbot.action.UnsupportedActionException
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Role
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.MessageReceipt
import love.forte.simbot.miyoushe.api.member.Member
import love.forte.simbot.utils.item.Items
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


/**
 * 米游社大别野成员
 *
 * @author ForteScarlet
 */
public interface VillaMember : GuildMember, DeleteSupport {
    override val bot: VillaBot

    /**
     * 用户基础信息
     */
    public val source: Member

    override val id: ID
        get() = source.basic.uid.ID

    override val username: String
        get() = source.basic.nickname

    /**
     * 始终为 `""`
     * @see username
     */
    override val nickname: String
        get() = ""

    override val avatar: String
        get() = source.basic.avatarUrl

    override val joinTime: Timestamp
        get() = Timestamp.byMillisecond(source.joinedAt)

    @JSTP
    override suspend fun guild(): VillaGuild

    @JSTP
    override suspend fun organization(): VillaGuild = guild()

    // TODO member.roles
    override val roles: Items<Role>
        get() = Items.emptyItems()

    /**
     * 踢出成员
     */
    @JvmSynthetic
    override suspend fun delete(): Boolean

    // unsupported

    /**
     * @throws UnsupportedActionException 米游社大别野成员不支持对其 send
     */
    @JST
    @Deprecated(SEND_OPERATOR_UNSUPPORTED)
    override suspend fun send(message: Message): MessageReceipt {
        throw UnsupportedActionException("VillaMember.send")
    }

    /**
     * @throws UnsupportedActionException 米游社大别野成员不支持对其 send
     */
    @JST
    @Deprecated(SEND_OPERATOR_UNSUPPORTED)
    override suspend fun send(message: MessageContent): MessageReceipt {
        throw UnsupportedActionException("VillaMember.send")
    }

    /**
     * @throws UnsupportedActionException 米游社大别野成员不支持对其 send
     */
    @JST
    @Deprecated(SEND_OPERATOR_UNSUPPORTED)
    override suspend fun send(text: String): MessageReceipt {
        throw UnsupportedActionException("VillaMember.send")
    }

    @JvmSynthetic
    @Deprecated(MUTE_OPERATOR_UNSUPPORTED, ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    @JST
    @Deprecated(MUTE_OPERATOR_UNSUPPORTED, ReplaceWith("false"))
    override suspend fun mute(time: Long, timeUnit: TimeUnit): Boolean = false

    @JST
    @Deprecated(MUTE_OPERATOR_UNSUPPORTED, ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
}

internal const val MUTE_OPERATOR_UNSUPPORTED = "`mute/unmute` operator is unsupported in a member of miyoushe villa"

internal const val SEND_OPERATOR_UNSUPPORTED = "`send` is unsupported in a member of miyoushe villa"
