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

package love.forte.simbot.miyoushe.stdlib.bot

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import love.forte.simbot.miyoushe.api.*

/**
 * 使用 API 通过 [Bot] 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
public suspend inline fun MiyousheVillaApi<*>.requestBy(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val (botId, secret) = bot.ticket
    val token = MiyousheVillaApiToken(botId, secret, villaId)

    return request(bot.apiClient, token, postRequestBuilder)
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
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestResultBy(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R> {
    val response = requestBy(bot, villaId, postRequestBuilder)
    if (!response.status.isSuccess()) {
        throw HttpStatusException(response)
    }

    val text = response.bodyAsText()
    return bot.apiDecoder.decodeFromString(apiResultSerializer, text)
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
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestDataBy(
    bot: Bot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): R {
    val apiResult = requestResultBy(bot, villaId, postRequestBuilder)

    // check if success and return
    return apiResult.dataIfSuccess
}
