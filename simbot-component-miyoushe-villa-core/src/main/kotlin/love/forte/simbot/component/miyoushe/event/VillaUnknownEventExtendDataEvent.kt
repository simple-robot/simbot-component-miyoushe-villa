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

import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.UnknownEventExtendData


/**
 * 当出现了尚未支持或未知的事件类型 [UnknownEventExtendData] 时使用的事件。
 * 与 [VillaUnsupportedYetEvent] 不同，[VillaUnknownEventExtendDataEvent]
 * 是具有明确事件类型 [UnknownEventExtendData] 的。
 * 与 [VillaUnsupportedYetEvent] 之间的区别请参阅其文档说明了解更多。
 *
 *
 * @see VillaUnsupportedYetEvent
 *
 * @author ForteScarlet
 */
public abstract class VillaUnknownEventExtendDataEvent : VillaEvent<UnknownEventExtendData>() {

    override val key: Event.Key<VillaUnknownEventExtendDataEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaUnknownEventExtendDataEvent>(
        "villa.unknown", VillaEvent
    ) {
        override fun safeCast(value: Any): VillaUnknownEventExtendDataEvent? = doSafeCast(value)
    }
}
