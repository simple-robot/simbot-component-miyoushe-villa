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

package love.forte.simbot.miyoushe.api

import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
public open class HttpStatusException : RuntimeException {
    @JsName("create_0")
    public constructor() : super()

    @JsName("create_1")
    public constructor(message: String?) : super(message)

    @JsName("create_2")
    public constructor(message: String?, cause: Throwable?) : super(message, cause)

    @JsName("create_3")
    public constructor(cause: Throwable?) : super(cause)
}
