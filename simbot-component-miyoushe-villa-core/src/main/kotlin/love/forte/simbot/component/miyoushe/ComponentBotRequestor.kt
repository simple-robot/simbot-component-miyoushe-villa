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

package love.forte.simbot.component.miyoushe

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import love.forte.simbot.component.miyoushe.bot.VillaBot
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.ApiResultNotSuccessException
import love.forte.simbot.miyoushe.api.HttpStatusException
import love.forte.simbot.miyoushe.api.MiyousheVillaApi
import love.forte.simbot.miyoushe.api.image.GetUploadImageParamsApi
import love.forte.simbot.miyoushe.api.image.OSSResponse
import love.forte.simbot.miyoushe.api.image.upload
import love.forte.simbot.miyoushe.stdlib.bot.requestBy
import love.forte.simbot.miyoushe.stdlib.bot.requestDataBy
import love.forte.simbot.miyoushe.stdlib.bot.requestResultBy
import love.forte.simbot.miyoushe.utils.toHex
import org.kotlincrypto.hash.md.MD5
import java.io.File
import java.nio.file.Path


/**
 * 使用 API 通过 [VillaBot] 发起一个请求，并得到一个[HTTP响应][HttpResponse].
 */
public suspend inline fun MiyousheVillaApi<*>.requestBy(
    bot: VillaBot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = requestBy(bot.source, villaId, postRequestBuilder)

/**
 * 使用 [VillaBot] 请求目标API并将结果解析为 [ApiResult] 类型。
 * 如果响应结果代表了失败（[HttpStatusCode.isSuccess] == false），
 * 则会抛出 [HttpStatusException]。
 *
 * 不会校验 [ApiResult] 的成功与否。
 *
 * @receiver 需要请求的 API
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 */
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestResultBy(
    bot: VillaBot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): ApiResult<R> = requestResultBy(bot.source, villaId, postRequestBuilder)

/**
 * 使用 [VillaBot] 请求目标API并将结果解析为预期类型。
 * 如果响应结果代表了失败，则会抛出 [ApiResultNotSuccessException]。
 *
 * @receiver 需要请求的 API
 * @see requestResultBy
 * @see ApiResult.dataIfSuccess
 * @throws HttpStatusException 如果 http 结果不是成功 ([HttpStatusCode.isSuccess] == false)
 * @throws ApiResultNotSuccessException 如果结果不是成功 (see [ApiResult.dataIfSuccess])
 */
public suspend inline fun <R : Any> MiyousheVillaApi<R>.requestDataBy(
    bot: VillaBot,
    villaId: String?,
    postRequestBuilder: HttpRequestBuilder.() -> Unit = {}
): R = requestDataBy(bot.source, villaId, postRequestBuilder)


// OSS

public suspend fun uploadToOss(
    bot: VillaBot,
    villaId: String?,
    data: ByteArray,
    ext: String
): HttpResponse {
    val md5 = MD5().digest(data).toHex()
    val result = GetUploadImageParamsApi.create(md5, ext).requestDataBy(bot, villaId)
    return result.params.upload(bot.source.apiClient, data)
}

public suspend fun uploadToOssResult(
    bot: VillaBot,
    villaId: String?,
    data: ByteArray,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): ApiResult<OSSResponse> {
    val resp = uploadToOss(bot, villaId, data, ext)
    val text = resp.bodyAsText()
    return decoder.decodeFromString(OSSResponse.apiResultDeserializationStrategy, text)
}

public suspend fun uploadToOssData(
    bot: VillaBot,
    villaId: String?,
    data: ByteArray,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): OSSResponse {
    val result = uploadToOssResult(bot, villaId, data, ext, decoder)
    return result.dataIfSuccess
}

public suspend fun uploadToOss(
    bot: VillaBot,
    villaId: String?,
    data: File,
    ext: String
): HttpResponse {
    val md5 = with(MD5()) {
        data.readChannel().consumeEachBufferRange { buffer, _ ->
            update(buffer)
            true
        }

        digest().toHex()
    }

    val result = GetUploadImageParamsApi.create(md5, ext).requestDataBy(bot, villaId)
    return result.params.upload(bot.source.apiClient, ChannelProvider(data.length()) { data.readChannel() })
}

public suspend fun uploadToOssResult(
    bot: VillaBot,
    villaId: String?,
    data: File,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): ApiResult<OSSResponse> {
    val resp = uploadToOss(bot, villaId, data, ext)
    val text = resp.bodyAsText()
    return decoder.decodeFromString(OSSResponse.apiResultDeserializationStrategy, text)
}

public suspend fun uploadToOssData(
    bot: VillaBot,
    villaId: String?,
    data: File,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): OSSResponse {
    val result = uploadToOssResult(bot, villaId, data, ext, decoder)
    return result.dataIfSuccess
}

public suspend fun uploadToOss(
    bot: VillaBot,
    villaId: String?,
    data: Path,
    ext: String
): HttpResponse = uploadToOss(bot, villaId, data.toFile(), ext)

public suspend fun uploadToOssResult(
    bot: VillaBot,
    villaId: String?,
    data: Path,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): ApiResult<OSSResponse> {
    val resp = uploadToOss(bot, villaId, data, ext)
    val text = resp.bodyAsText()
    return decoder.decodeFromString(OSSResponse.apiResultDeserializationStrategy, text)
}

public suspend fun uploadToOssData(
    bot: VillaBot,
    villaId: String?,
    data: Path,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): OSSResponse {
    val result = uploadToOssResult(bot, villaId, data, ext, decoder)
    return result.dataIfSuccess
}
