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

package love.forte.simbot.miyoushe.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.MiyousheVilla
import kotlin.jvm.JvmSynthetic


/**
 * 使用 API 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
@JvmSynthetic
public suspend inline fun MiyousheVillaApi<*>.request(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val method = this.method
    val body = this.body
    val url = this.url
    val api = this

    return client.request {
        this.method = method
        this.url(url)
        // set Body
        when (body) {
            null -> {
                // ignore.
            }

            is OutgoingContent -> setBody(body)
            else -> {
                if (contentType() == null) {
                    contentType(ContentType.Application.Json)
                }

                if (client.pluginOrNull(ContentNegotiation) != null) {
                    setBody(body)
                } else {
                    try {
                        val ser = guessSerializer(body, MiyousheVilla.DefaultJson.serializersModule)
                        val bodyJson = MiyousheVilla.DefaultJson.encodeToString(ser, body)
                        setBody(bodyJson)
                    } catch (e: Throwable) {
                        try {
                            setBody(body)
                        } catch (e0: Throwable) {
                            e0.addSuppressed(e)
                            throw e0
                        }
                    }
                }
            }
        }

        headers[MiyousheVilla.Headers.BOT_ID_KEY] = token.botId
        headers[MiyousheVilla.Headers.BOT_SECRET_KEY] = token.botSecret
        token.botVillaId?.also { headers[MiyousheVilla.Headers.BOT_VILLA_ID_KEY] = it }

        if (api is StandardMiyousheVillaApi) {
            api.postRequestBuilder(this)
        }

        postRequestBuilder(this)
    }
}

/**
 * 请求目标API并将结果解析为 [ApiResult] 类型。
 * 如果响应结果代表了失败（[HttpStatusCode.isSuccess] == false），
 * 则会抛出 [HttpStatusException]。
 *
 * 不会校验 [ApiResult] 的成功与否。
 *
 * @receiver 需要请求的 API
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 */
@JvmSynthetic
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestResult(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R> {
    val response = request(client, token, postRequestBuilder)
    if (!response.status.isSuccess()) {
        throw HttpStatusException(response)
    }
    val text = response.bodyAsText()
    return decoder.decodeFromString(apiResultSerializer, text)
}

/**
 * 请求目标API并将结果解析为预期类型。
 * 如果响应结果代表了失败，则会抛出 [ApiResultNotSuccessException]。
 *
 * @receiver 需要请求的 API
 * @see requestResult
 * @see ApiResult.dataIfSuccess
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 * @throws ApiResultNotSuccessException 如果结果不是成功 (see [ApiResult.dataIfSuccess])
 */
@JvmSynthetic
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestData(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    decoder: Json = MiyousheVilla.DefaultJson,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): R {
    val apiResult = requestResult(client, token, decoder, postRequestBuilder)

    // check if success and return
    return apiResult.dataIfSuccess
}

