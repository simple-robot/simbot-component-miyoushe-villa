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
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.component.miyoushe.message.VillaSendMessageReceipt
import love.forte.simbot.definition.Channel
import love.forte.simbot.definition.GuildMember
import love.forte.simbot.definition.Organization
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.miyoushe.api.room.RoomBasic
import love.forte.simbot.utils.item.Items
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


/**
 * 米游社大别野中的子频道。
 * 也就是 `房间 (Room)`。
 *
 * [VillaRoom] 作为协程作用域与 bot 所指作用域相同。
 *
 * @author ForteScarlet
 */
public interface VillaRoom : Channel {
    override val bot: VillaBot

    /**
     * 原始的房间信息。
     */
    public val room: RoomBasic

    override val id: ID
        get() = room.roomId.ID

    override val name: String
        get() = room.roomName

    /**
     * 始终为空字符串 `""`
     */
    override val description: String
        get() = ""

    /**
     * 始终为空字符串 `""`
     */
    override val icon: String
        get() = ""

    /**
     * 不支持，始终得到 [Timestamp.notSupport]
     */
    override val createTime: Timestamp
        get() = Timestamp.notSupport()

    @JSTP
    override suspend fun guild(): VillaGuild

    @JSTP
    override suspend fun previous(): Organization? = guild()

    @JSTP
    override suspend fun owner(): VillaMember = guild().owner()

    /**
     * 等同于 [VillaGuild.member]
     */
    @JST(blockingBaseName = "getMember", blockingSuffix = "", asyncBaseName = "getMember")
    override suspend fun member(id: ID): VillaMember? = guild().member(id)

    /**
     * 等同于 [VillaGuild.members]
     */
    override val members: Items<GuildMember>


    /**
     * 无法获取，始终得到 `-1`
     */
    override val currentMember: Int get() = -1

    /**
     * 无法获取，始终得到 `-1`
     */
    override val maximumMember: Int get() = -1

    /**
     * 向此房间发送消息
     */
    @JST
    override suspend fun send(message: Message): VillaSendMessageReceipt

    /**
     * 向此房间发送消息
     */
    @JST
    override suspend fun send(message: MessageContent): VillaSendMessageReceipt

    /**
     * 向此房间发送消息
     */
    @JST
    override suspend fun send(text: String): VillaSendMessageReceipt

    @Deprecated(ROOM_MUTE_OP_UNSUPPORTED, ReplaceWith("false"))
    @JvmSynthetic
    override suspend fun mute(duration: Duration): Boolean = false

    @Deprecated(ROOM_MUTE_OP_UNSUPPORTED, ReplaceWith("false"))
    @JvmSynthetic
    override suspend fun mute(time: Long, timeUnit: TimeUnit): Boolean = false

    @Deprecated(ROOM_MUTE_OP_UNSUPPORTED, ReplaceWith("false"))
    @JvmSynthetic
    override suspend fun unmute(): Boolean = false


}

internal const val ROOM_MUTE_OP_UNSUPPORTED = "`mute/unmute` operators is not supported in villa room"
