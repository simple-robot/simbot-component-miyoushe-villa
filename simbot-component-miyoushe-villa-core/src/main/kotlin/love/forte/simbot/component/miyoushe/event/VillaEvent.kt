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
import love.forte.simbot.Timestamp
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.EventExtendData


/**
 *
 * 米游社大别野的组件事件。
 *
 * @author ForteScarlet
 */
public abstract class VillaEvent<E : EventExtendData> : Event {
    abstract override val bot: VillaBot
    public abstract val sourceEvent: love.forte.simbot.miyoushe.event.Event<E>

    public val sourceEventExtend: E
        get() = sourceEvent.extendData

    override val id: ID
        get() = sourceEvent.id.ID

    override val timestamp: Timestamp
        get() = Timestamp.byMillisecond(sourceEvent.sendAt)

    abstract override val key: Event.Key<out VillaEvent<*>>

    public companion object Key : BaseEventKey<VillaEvent<*>>(
        "villa.event", Event
    ) {
        override fun safeCast(value: Any): VillaEvent<*>? = doSafeCast(value)
    }
}





