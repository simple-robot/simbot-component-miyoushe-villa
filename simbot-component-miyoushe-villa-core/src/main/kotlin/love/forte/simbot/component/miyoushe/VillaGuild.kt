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
import love.forte.simbot.definition.Guild
import love.forte.simbot.definition.Organization
import love.forte.simbot.definition.Role
import love.forte.simbot.miyoushe.api.villa.Villa
import love.forte.simbot.utils.item.Items
import java.util.concurrent.TimeUnit
import kotlin.time.Duration


/**
 * 米游社大别野中的频道服务器。
 * 也就是 `大别野 (Villa)`。
 *
 * @author ForteScarlet
 */
public interface VillaGuild : Guild {
    override val bot: VillaBot

    /**
     * 原始的大别野对象。
     */
    public val source: Villa

    override val id: ID
        get() = source.villaId.ID

    override val ownerId: ID
        get() = source.ownerUid.ID

    override val icon: String
        get() = source.villaAvatarUrl

    override val name: String
        get() = source.name

    override val description: String
        get() = source.introduce

    /**
     * 无法获取，始终得到 [Timestamp.notSupport]
     */
    override val createTime: Timestamp
        get() = Timestamp.notSupport()

    /**
     * 频道的拥有者
     */
    @JSTP
    override suspend fun owner(): VillaMember

    /**
     * 获取大别野房间列表
     */
    override val channels: Items<VillaRoom>

    /**
     * 根据 [id] 寻找指定大别野房间
     */
    @JST(blockingBaseName = "getChannel", blockingSuffix = "", asyncBaseName = "getChannel")
    override suspend fun channel(id: ID): VillaRoom?

    /**
     * 无法获取，始终为 `-1`
     */
    override val currentChannel: Int
        get() = -1

    /**
     * 无法获取，始终为 `-1`
     */
    override val maximumChannel: Int
        get() = -1

    /**
     * 无法获取，始终为 `-1`
     */
    override val currentMember: Int
        get() = -1

    /**
     * 无法获取，始终为 `-1`
     */
    override val maximumMember: Int
        get() = -1

    /**
     * 根据 [id] 寻找指定成员
     */
    @JST(blockingBaseName = "getMember", blockingSuffix = "", asyncBaseName = "getMember")
    override suspend fun member(id: ID): VillaMember?

    /**
     * 获取成员列表
     */
    override val members: Items<VillaMember>


    /**
     * @see channels
     */
    override val children: Items<VillaRoom>
        get() = channels

    /**
     * @see channel
     */
    @JST(blockingBaseName = "getChild", blockingSuffix = "", asyncBaseName = "getChild")
    override suspend fun child(id: ID): VillaRoom? = channel(id)

    /**
     * 始终为 `null`
     */
    @JvmSynthetic
    override suspend fun previous(): Organization? = null

    // TODO guild roles
    override val roles: Items<Role>
        get() = Items.emptyItems()

    /**
     * 不支持，始终得到 `false`。
     */
    @JvmSynthetic
    @Deprecated(MUTE_OP_NOT_SUPPORTED, ReplaceWith("false"))
    override suspend fun mute(duration: Duration): Boolean = false

    /**
     * 不支持，始终得到 `false`。
     */
    @JvmSynthetic
    @Deprecated(MUTE_OP_NOT_SUPPORTED, ReplaceWith("false"))
    override suspend fun mute(time: Long, timeUnit: TimeUnit): Boolean = false

    /**
     * 不支持，始终得到 `false`。
     */
    @JvmSynthetic
    @Deprecated(MUTE_OP_NOT_SUPPORTED, ReplaceWith("false"))
    override suspend fun unmute(): Boolean = false
}

internal const val MUTE_OP_NOT_SUPPORTED = "`mute/unmute` operators in villa is not supported."
