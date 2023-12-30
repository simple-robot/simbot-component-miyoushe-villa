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
import love.forte.simbot.miyoushe.event.CreateRobot
import love.forte.simbot.miyoushe.event.DeleteRobot
import love.forte.simbot.miyoushe.event.EventExtendData


/**
 * 大别野 bot 相关事件。
 *
 * @see CreateRobot
 * @see DeleteRobot
 *
 * @author ForteScarlet
 */
@JSTP
public abstract class VillaBotEvent<E : EventExtendData> : VillaEvent<E>(), GuildEvent {
    abstract override val bot: VillaBot

    /**
     * 大别野ID
     */
    public abstract val villaId: ID

    /**
     * 发生事件的大别野
     */
    abstract override suspend fun guild(): VillaGuild

    /**
     * 发生事件的大别野
     *
     * @see guild
     */
    override suspend fun organization(): VillaGuild = guild()

    abstract override val key: Event.Key<out VillaBotEvent<*>>

    public companion object Key : BaseEventKey<VillaBotEvent<*>>(
        "villa.bot", VillaEvent, GuildEvent
    ) {
        override fun safeCast(value: Any): VillaBotEvent<*>? = doSafeCast(value)
    }
}

/**
 * 大别野添加机器人实例事件
 *
 * @see CreateRobot
 * @see VillaBotEvent
 */
public abstract class VillaCreateRobotEvent : VillaBotEvent<CreateRobot>() {
    override val villaId: ID
        get() = sourceEventExtend.villaId.ID

    override val key: Event.Key<out VillaCreateRobotEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaCreateRobotEvent>(
        "villa.bot.create", VillaBotEvent
    ) {
        override fun safeCast(value: Any): VillaCreateRobotEvent? = doSafeCast(value)
    }
}

/**
 * 大别野删除机器人实例
 *
 * @see DeleteRobot
 * @see VillaBotEvent
 */
public abstract class VillaDeleteRobotEvent : VillaBotEvent<DeleteRobot>() {
    override val villaId: ID
        get() = sourceEventExtend.villaId.ID

    override val key: Event.Key<out VillaDeleteRobotEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaDeleteRobotEvent>(
        "villa.bot.delete", VillaBotEvent
    ) {
        override fun safeCast(value: Any): VillaDeleteRobotEvent? = doSafeCast(value)
    }
}
