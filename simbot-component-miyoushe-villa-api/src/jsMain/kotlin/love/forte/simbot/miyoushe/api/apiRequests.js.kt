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

@file:Suppress("NON_EXPORTABLE_TYPE")
@file:JsExport

package love.forte.simbot.miyoushe.api

import io.ktor.client.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise
import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.MiyousheVilla
import kotlin.js.Promise

/**
 * 使用当前API发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
public fun <R : Any> MiyousheVillaApi<R>.requestAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    scope: CoroutineScope = client
): Promise<HttpResponse> =
    scope.promise { request(client, token) }


public fun <R : Any> MiyousheVillaApi<R>.requestResultAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    scope: CoroutineScope = client
): Promise<ApiResult<R>> =
    scope.promise { requestResult(client, token, decoder) }


public fun <R : Any> MiyousheVillaApi<R>.requestDataAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    scope: CoroutineScope = client
): Promise<R> =
    scope.promise { requestData(client, token, decoder) }
