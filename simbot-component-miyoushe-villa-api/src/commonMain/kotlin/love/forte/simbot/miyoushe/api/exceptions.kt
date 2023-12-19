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

import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.js.JsExport

/**
 * @author forte
 */
@JsExport
@Suppress("MemberVisibilityCanBePrivate", "NON_EXPORTABLE_TYPE")
public open class HttpStatusException(
    public val response: HttpResponse,
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message ?: "status: ${response.status}", cause) {
    public val status: HttpStatusCode
        get() = response.status
}

private fun ApiResult<*>.toReasonString() = "retcode: $retcode, message: $message"

/**
 * @see ApiResult
 * @author forte
 */
@Suppress("CanBeParameter", "MemberVisibilityCanBePrivate")
@JsExport
public open class ApiResultNotSuccessException(
    public val apiResult: ApiResult<*>,
    message: String? = null,
    cause: Throwable? = null
) : IllegalStateException(message ?: apiResult.toReasonString(), cause)
