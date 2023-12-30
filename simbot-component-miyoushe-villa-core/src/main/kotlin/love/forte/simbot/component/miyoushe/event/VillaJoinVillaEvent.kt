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

package love.forte.simbot.component.miyoushe.event

import love.forte.simbot.ID
import love.forte.simbot.JSTP
import love.forte.simbot.Timestamp
import love.forte.simbot.action.ActionType
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.definition.MemberInfo
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.event.GuildMemberIncreaseEvent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.JoinVilla


/**
 * 有新用户加入大别野事件
 *
 * @see JoinVilla
 *
 * @author ForteScarlet
 */
public abstract class VillaJoinVillaEvent : VillaEvent<JoinVilla>(), GuildMemberIncreaseEvent {
    abstract override val bot: VillaBot

    //region delegates
    /**
     * `uint64`, 用户 id
     *
     * @see JoinVilla.joinUid
     */
    public val joinUid: ID get() = sourceEventExtend.joinUid.ID

    /**
     * `string`, 用户昵称
     *
     * @see JoinVilla.joinUserNickname
     */
    public val joinUserNickname: String get() = sourceEventExtend.joinUserNickname

    /**
     * `uint64`, 大别野 id
     *
     * @see JoinVilla.villaId
     */
    public val villaId: ID get() = sourceEventExtend.villaId.ID
    //endregion

    override val timestamp: Timestamp
        get() = super<VillaEvent>.timestamp

    override val changedTime: Timestamp
        get() = timestamp

    /**
     * 无法得知操作者，始终得到 `null`。
     */
    @JvmSynthetic
    override suspend fun operator(): MemberInfo? = null

    /**
     * 新添加的用户
     */
    @JSTP
    abstract override suspend fun member(): VillaMember

    /**
     * 新添加的用户
     *
     * @see member
     */
    @JSTP
    override suspend fun user(): VillaMember = member()

    /**
     * 新添加的用户
     *
     * @see member
     */
    @JSTP
    override suspend fun after(): VillaMember = member()

    /**
     * 无法判断，始终得到 [ActionType.PROACTIVE]
     */
    override val actionType: ActionType
        get() = ActionType.PROACTIVE

    /**
     * 事件发生的大别野
     */
    @JSTP
    abstract override suspend fun source(): VillaGuild

    /**
     * 事件发生的大别野
     *
     * @see source
     */
    @JSTP
    override suspend fun organization(): VillaGuild = source()

    /**
     * 事件发生的大别野
     *
     * @see source
     */
    @JSTP
    override suspend fun guild(): VillaGuild = source()

    override val key: Event.Key<VillaJoinVillaEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaJoinVillaEvent>(
        "villa.join", VillaEvent, GuildMemberIncreaseEvent
    ) {
        override fun safeCast(value: Any): VillaJoinVillaEvent? = doSafeCast(value)
    }
}
