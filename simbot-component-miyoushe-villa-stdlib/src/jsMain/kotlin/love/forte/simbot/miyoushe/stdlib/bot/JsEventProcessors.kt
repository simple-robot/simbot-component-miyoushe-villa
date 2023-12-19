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

@file:JsFileName("EventProcessors")

package love.forte.simbot.miyoushe.stdlib.bot

import love.forte.simbot.miyoushe.event.Event
import love.forte.simbot.miyoushe.event.EventExtendData
import love.forte.simbot.miyoushe.event.EventSource
import kotlin.js.Promise


/**
 * 以异步的方式构建 [EventProcessor] 函数。
 *
 */
public fun  async(
    function: (Event<EventExtendData>, EventSource) -> Promise<Unit?>
): EventProcessor {
    return EventProcessor { source ->
        function(this, source)
    }
}
