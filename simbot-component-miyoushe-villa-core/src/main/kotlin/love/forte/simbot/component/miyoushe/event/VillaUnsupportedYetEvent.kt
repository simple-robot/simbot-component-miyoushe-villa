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

import love.forte.simbot.DiscreetSimbotApi
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.EventExtendData
import love.forte.simbot.miyoushe.event.UnknownEventExtendData


/**
 * 所有 **组件(core)模块** 尚未支持的事件类型最终的包装类型。
 *
 * 与 [VillaUnknownEventExtendDataEvent] 不同，
 * [VillaUnknownEventExtendDataEvent] 是明确知道事件类型为 [UnknownEventExtendData] 的，
 * 它的作用是兼容未来米游社更新而 API 模块尚未跟进时接收到暂时无法解析的事件时的情况。
 * 而 [VillaUnknownEventExtendDataEvent] 则是 API 模块已经支持、但是当前组件模块尚未有对应的包装事件时候的一种兼容。
 * 它们也许都是一种“兼容”事件，但是兼容的目标与模块均不相同。
 *
 * [VillaUnsupportedYetEvent] 是一个 [具有特殊规则的API][DiscreetSimbotApi]，
 * 如果未来某个 API 模块支持、但是当前组件模块不支持的事件在某次更新后被支持了，
 * [VillaUnsupportedYetEvent] 将不会有任何警告或提示。
 *
 * @see VillaUnknownEventExtendDataEvent
 *
 * @author ForteScarlet
 */
@DiscreetSimbotApi
public abstract class VillaUnsupportedYetEvent : VillaEvent<EventExtendData>() {

    override val key: Event.Key<VillaUnsupportedYetEvent>
        get() = Key

    override fun toString(): String = "VillaUnsupportedYetEvent(sourceEvent=$sourceEvent)"

    public companion object Key : BaseEventKey<VillaUnsupportedYetEvent>("villa.unsupported_yet") {
        override fun safeCast(value: Any): VillaUnsupportedYetEvent? = doSafeCast(value)
    }
}

