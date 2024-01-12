/*
 * Copyright (c) 2023-2024. ForteScarlet.
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

@file:JvmName("BotRequests")
@file:JvmMultifileClass

package love.forte.simbot.miyoushe.stdlib.bot

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.ApiResultNotSuccessException
import love.forte.simbot.miyoushe.api.HttpStatusException
import love.forte.simbot.miyoushe.api.MiyousheVillaApi
import love.forte.simbot.utils.runInAsync
import love.forte.simbot.utils.runInNoScopeBlocking
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

// blocking

/**
 * 使用 API 通过 [Bot] 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
@JvmOverloads
public fun MiyousheVillaApi<*>.requestByBlocking(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): HttpResponse = runInNoScopeBlocking {
    requestBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}

/**
 * 使用 [Bot] 请求目标API并将结果解析为 [ApiResult] 类型。
 * 如果响应结果代表了失败（[HttpStatusCode.isSuccess] == false），
 * 则会抛出 [HttpStatusException]。
 *
 * 不会校验 [ApiResult] 的成功与否。
 *
 * @receiver 需要请求的 API
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 */
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestResultByBlocking(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): ApiResult<R> = runInNoScopeBlocking {
    requestResultBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}

/**
 * 使用 [Bot] 请求目标API并将结果解析为预期类型。
 * 如果响应结果代表了失败，则会抛出 [ApiResultNotSuccessException]。
 *
 * @receiver 需要请求的 API
 * @see requestResultBy
 * @see ApiResult.dataIfSuccess
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 * @throws ApiResultNotSuccessException 如果结果不是成功 (see [ApiResult.dataIfSuccess])
 */
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestDataByBlocking(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): R = runInNoScopeBlocking {
    requestDataBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}

// async

/**
 * 使用 API 通过 [Bot] 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
@OptIn(InternalSimbotApi::class)
@JvmOverloads
public fun MiyousheVillaApi<*>.requestByAsync(
    bot: Bot,
    villaId: String?,
    scope: CoroutineScope? = null,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): CompletableFuture<HttpResponse> = runInAsync(scope ?: bot) {
    requestBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}

/**
 * 使用 [Bot] 请求目标API并将结果解析为 [ApiResult] 类型。
 * 如果响应结果代表了失败（[HttpStatusCode.isSuccess] == false），
 * 则会抛出 [HttpStatusException]。
 *
 * 不会校验 [ApiResult] 的成功与否。
 *
 * @receiver 需要请求的 API
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 */
@OptIn(InternalSimbotApi::class)
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestResultByAsync(
    bot: Bot,
    villaId: String?,
    scope: CoroutineScope? = null,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): CompletableFuture<ApiResult<R>> = runInAsync(scope ?: bot) {
    requestResultBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}

/**
 * 使用 [Bot] 请求目标API并将结果解析为预期类型。
 * 如果响应结果代表了失败，则会抛出 [ApiResultNotSuccessException]。
 *
 * @receiver 需要请求的 API
 * @see requestResultBy
 * @see ApiResult.dataIfSuccess
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 * @throws ApiResultNotSuccessException 如果结果不是成功 (see [ApiResult.dataIfSuccess])
 */
@OptIn(InternalSimbotApi::class)
@JvmOverloads
public fun <R : Any> MiyousheVillaApi<R>.requestDataByAsync(
    bot: Bot,
    villaId: String?,
    scope: CoroutineScope? = null,
    postRequestBuilder: Consumer<HttpRequestBuilder>? = null
): CompletableFuture<R> = runInAsync(scope ?: bot) {
    requestDataBy(bot, villaId) {
        postRequestBuilder?.accept(this)
    }
}
