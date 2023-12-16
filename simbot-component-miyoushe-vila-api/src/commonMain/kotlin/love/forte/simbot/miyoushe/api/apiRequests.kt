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

import io.ktor.client.*
import io.ktor.client.statement.*
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString


public suspend fun <R> MiyousheApi<R>.requestResult(client: HttpClient, token: MiyousheApiToken, decoder: StringFormat): ApiResult<R> {
    val response = request(client, token)
    val text = response.bodyAsText()
    return decoder.decodeFromString(apiResultSerializer, text)
}

@Suppress("UNCHECKED_CAST")
public suspend fun <R> MiyousheApi<R>.requestData(client: HttpClient, token: MiyousheApiToken, decoder: StringFormat): R {
    val apiResult = requestResult(client, token, decoder)

    // TODO check result?

    return apiResult.data as R
}
