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

@file:JsFileName("MiyousheVillaOSS")
@file:Suppress("NON_EXPORTABLE_TYPE")
@file:JsExport

package love.forte.simbot.miyoushe.api.image

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.promise
import kotlin.js.Promise

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 promise 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JsName("uploadByInputProvider")
public fun OSSParams.upload(
    client: HttpClient,
    fileInputProvider: InputProvider,
    scope: CoroutineScope = client
): Promise<HttpResponse> = scope.promise {
    upload(client, fileInputProvider)
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 promise 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
public fun OSSParams.upload(
    client: HttpClient,
    fileData: ByteArray,
    scope: CoroutineScope = client
): Promise<HttpResponse> = scope.promise {
    upload(client, fileData)
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 promise 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JsName("uploadByByteReadPacket")
public fun OSSParams.upload(
    client: HttpClient,
    fileData: ByteReadPacket,
    scope: CoroutineScope = client
): Promise<HttpResponse> = scope.promise {
    upload(client, fileData)
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 promise 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JsName("uploadByChannelProvider")
public fun OSSParams.upload(
    client: HttpClient,
    fileChannelProvider: ChannelProvider,
    scope: CoroutineScope = client
): Promise<HttpResponse> = scope.promise {
    upload(client, fileChannelProvider)
}
