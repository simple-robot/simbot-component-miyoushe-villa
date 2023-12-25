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

package love.forte.simbot.component.miyoushe.bot

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 *
 * @author ForteScarlet
 */
public class VillaBotManagerConfiguration {
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext

    /**
     * 是否要求相同 id 的bot同时只能存在一个。
     *
     * 如果为 `false` 则允许注册多个相同 `id` 的 [VillaBot]。
     * 此时如果通过 [VillaBotManager.get] 获取则会得到第一个匹配的结果（通常来讲是最早注册的那个）。
     *
     * 如果为 `true`（默认）则注册时如果已经存在相同 id 的 bot，抛出异常。
     */
    public var uniqueId: Boolean = true
}
