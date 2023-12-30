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

@file:JvmMultifileClass
@file:JvmName("APIRequests")

package love.forte.simbot.miyoushe.api

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import love.forte.simbot.Api4J
import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.utils.runInAsync
import love.forte.simbot.utils.runInNoScopeBlocking
import java.util.concurrent.CompletableFuture

// blocking

/**
 * 使用 API 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 *
 * @see runInNoScopeBlocking
 */
@Api4J
public fun <R : Any> MiyousheVillaApi<R>.requestBlocking(
    client: HttpClient,
    token: MiyousheVillaApiToken
): HttpResponse = runInNoScopeBlocking {
    request(client, token)
}

/**
 * @see runInNoScopeBlocking
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 */
@Api4J
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestResultBlocking(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson
): ApiResult<R> = runInNoScopeBlocking {
    requestResult(client, token, decoder)
}

/**
 * @see runInNoScopeBlocking
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 * @throws ApiResultNotSuccessException 如果结果不是成功 (see [ApiResult.dataIfSuccess])
 */
@Api4J
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestDataBlocking(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson
): R = runInNoScopeBlocking {
    requestData(client, token, decoder)
}

// async

@Api4J
@JvmOverloads
@OptIn(InternalSimbotApi::class)
public fun <R : Any> MiyousheVillaApi<R>.requestAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> = runInAsync(scope ?: client) {
    request(client, token)
}

@Api4J
@JvmOverloads
@OptIn(InternalSimbotApi::class)
public fun <R : Any> MiyousheVillaApi<R>.requestResultAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    scope: CoroutineScope? = null
): CompletableFuture<ApiResult<R>> = runInAsync(scope ?: client) {
    requestResult(client, token, decoder)
}

@Api4J
@JvmOverloads
@OptIn(InternalSimbotApi::class)
public fun <R : Any> MiyousheVillaApi<R>.requestDataAsync(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    scope: CoroutineScope? = null
): CompletableFuture<out R> = runInAsync(scope ?: client) {
    requestData(client, token, decoder)
}
