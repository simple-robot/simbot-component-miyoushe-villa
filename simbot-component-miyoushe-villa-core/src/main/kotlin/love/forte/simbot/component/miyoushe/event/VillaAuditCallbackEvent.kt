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
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.event.GuildEvent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.AuditCallback


/**
 * 审核 API 结果回调事件
 *
 * @see AuditCallback
 *
 * @author ForteScarlet
 */
public abstract class VillaAuditCallbackEvent : VillaEvent<AuditCallback>(), GuildEvent {
    abstract override val bot: VillaBot

    //region delegates
    /**
     * 审核事件 id
     *
     * @see AuditCallback.auditId
     */
    public val auditId: String get() = sourceEventExtend.auditId

    /**
     * 机器人 id
     *
     * @see AuditCallback.botTplId
     */
    public val botTplId: String get() = sourceEventExtend.botTplId

    /**
     * 大别野 id
     *
     * @see AuditCallback.villaId
     */
    public val villaId: ID get() = sourceEventExtend.villaId.ID

    /**
     * 房间 id（和审核接口调用方传入的值一致）
     *
     * @see AuditCallback.roomId
     */
    public val roomId: ID? get() = sourceEventExtend.roomId?.ID

    /**
     * 用户 id（和审核接口调用方传入的值一致）
     *
     * @see AuditCallback.userId
     */
    public val userId: ID get() = sourceEventExtend.userId.ID

    /**
     * 透传数据（和审核接口调用方传入的值一致）
     *
     * @see AuditCallback.passThrough
     */
    public val passThrough: String? get() = sourceEventExtend.passThrough

    /**
     * 审核结果，0作兼容，1审核通过，2审核驳回
     *
     * @see AuditCallback.auditResult
     */
    public val auditResult: Int
        get() = sourceEventExtend.auditResult

    /**
     * @see AuditCallback.isAuditResultPass
     */
    public val isAuditResultPass: Boolean
        get() = sourceEventExtend.isAuditResultPass

    /**
     * @see AuditCallback.isAuditResultReject
     */
    public val isAuditResultReject: Boolean
        get() = sourceEventExtend.isAuditResultReject


    //endregion

    /**
     * 发生事件的大别野
     */
    @JSTP
    abstract override suspend fun guild(): VillaGuild

    /**
     * 发生事件的大别野
     */
    @JSTP
    override suspend fun organization(): VillaGuild = guild()

    override val key: Event.Key<VillaAuditCallbackEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaAuditCallbackEvent>(
        "villa.audit.callback", VillaEvent, GuildEvent
    ) {
        override fun safeCast(value: Any): VillaAuditCallbackEvent? = doSafeCast(value)
    }
}
