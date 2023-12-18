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

@file:JvmName("MiyousheVillaOSS")

package love.forte.simbot.miyoushe.api.image

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.util.cio.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.nio.*
import kotlinx.coroutines.CoroutineScope
import love.forte.simbot.Api4J
import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.utils.runInAsync
import love.forte.simbot.utils.runInNoScopeBlocking
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import kotlin.io.path.isReadable
import kotlin.io.path.isRegularFile


//region ex
/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @param file 想要上传的文件
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, file: File): HttpResponse {
    check(file.isFile) { "File [$file] is not a file (isFile = false)" }
    check(file.canRead()) { "File [$file] can not be read (canRead = false)" }

    return upload(client, ChannelProvider { file.readChannel() })
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @param path 想要上传的文件
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, path: Path): HttpResponse {
    check(path.isRegularFile()) { "Path [$path] is not a regular file (isRegularFile = false)" }
    check(path.isReadable()) { "Path [$path] is not readable (isReadable = false)" }

    return upload(client, InputProvider { Files.newByteChannel(path).asInput() })
}
//endregion

//region Blocking
/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @param file 想要上传的文件
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, file: File): HttpResponse =
    runInNoScopeBlocking {
        upload(client, file)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @param path 想要上传的文件
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, path: Path): HttpResponse =
    runInNoScopeBlocking {
        upload(client, path)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, fileInputProvider: InputProvider): HttpResponse =
    runInNoScopeBlocking {
        upload(client, fileInputProvider)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, fileData: ByteArray): HttpResponse = runInNoScopeBlocking {
    upload(client, fileData)
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, fileData: ByteReadPacket): HttpResponse = runInNoScopeBlocking {
    upload(client, fileData)
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@Api4J
public fun OSSParams.uploadBlocking(client: HttpClient, fileChannelProvider: ChannelProvider): HttpResponse =
    runInNoScopeBlocking {
        upload(client, fileChannelProvider)
    }

//endregion
//region Async
/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    fileInputProvider: InputProvider,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, fileInputProvider)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 *
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    fileData: ByteArray,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, fileData)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 *
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    fileData: ByteReadPacket,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, fileData)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 *
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    fileChannelProvider: ChannelProvider,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, fileChannelProvider)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 *
 * @param file 想要上传的文件
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    file: File,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, file)
    }

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * 如果上传出现错误则会向 future 中传播 [OSSUploadException] 异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 *
 * @param path 想要上传的文件
 */
@OptIn(InternalSimbotApi::class)
@Api4J
@JvmOverloads
public fun OSSParams.uploadAsync(
    client: HttpClient,
    path: Path,
    scope: CoroutineScope? = null
): CompletableFuture<HttpResponse> =
    runInAsync(scope ?: client) {
        upload(client, path)
    }
//endregion


