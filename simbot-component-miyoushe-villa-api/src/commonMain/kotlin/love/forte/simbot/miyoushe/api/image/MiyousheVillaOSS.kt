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

package love.forte.simbot.miyoushe.api.image

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.ApiResultNotSuccessException
import love.forte.simbot.miyoushe.api.MiyousheVillaApiToken
import love.forte.simbot.miyoushe.api.requestData
import love.forte.simbot.miyoushe.utils.toHex
import org.kotlincrypto.hash.md.MD5
import kotlin.jvm.JvmSynthetic


/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileInputProvider: InputProvider): HttpResponse {
    return doUpload(client) {
        append(OssFormParams.FILE, fileInputProvider, it)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileData: ByteArray): HttpResponse {
    return doUpload(client) {
        append(OssFormParams.FILE, fileData, it)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileData: ByteReadPacket): HttpResponse {
    return doUpload(client) {
        append(OssFormParams.FILE, fileData, it)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 *
 * @throws OSSUploadException [client] 进行上传请求时产生了异常。具体异常会被包装在 [OSSUploadException.cause] 中。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileChannelProvider: ChannelProvider): HttpResponse {
    return doUpload(client) {
        append(OssFormParams.FILE, fileChannelProvider, it)
    }
}

/**
 * @throws OSSUploadException
 */
private suspend inline fun OSSParams.doUpload(
    client: HttpClient,
    crossinline inForm: FormBuilder.(Headers) -> Unit
): HttpResponse {
    return kotlin.runCatching {
        client.post(host) {
            setBody(MultiPartFormDataContent(formData {
                appendOSSParams(this@doUpload)
                inForm(Headers.Empty)
            }))
        }
    }.getOrElse { e -> throw OSSUploadException(e.message, e) }
}

private object OssFormParams {
    const val X_EXTRA = "\"x:extra\""
    const val OSSACCESS_KEY_ID = "\"OSSAccessKeyId\""
    const val SIGNATURE = "\"signature\""
    const val SUCCESS_ACTION_STATUS = "\"success_action_status\""
    const val NAME = "\"name\""
    const val CALLBACK = "\"callback\""
    const val X_OSS_CONTENT_TYPE = "\"x-oss-content-type\""
    const val KEY = "\"key\""
    const val POLICY = "\"policy\""
    const val CONTENT_DISPOSITION = "\"Content-Disposition\""

    const val FILE = "\"file\""

}

private fun FormBuilder.appendOSSParams(params: OSSParams) {
    append(OssFormParams.X_EXTRA, params.callbackVar.xExtra)
    append(OssFormParams.OSSACCESS_KEY_ID, params.accessid)
    append(OssFormParams.SIGNATURE, params.signature)
    append(OssFormParams.SUCCESS_ACTION_STATUS, params.successActionStatus)
    append(OssFormParams.NAME, params.name)
    append(OssFormParams.CALLBACK, params.callback)
    append(OssFormParams.X_OSS_CONTENT_TYPE, params.xOssContentType)
    append(OssFormParams.KEY, params.key)
    append(OssFormParams.POLICY, params.policy)
    append(OssFormParams.CONTENT_DISPOSITION, params.contentDisposition)
}


public suspend fun uploadToOss(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    data: ByteArray,
    ext: String
): HttpResponse {
    val md5 = MD5().digest(data).toHex()
    val result = GetUploadImageParamsApi.create(md5, ext).requestData(client, token)
    return result.params.upload(client, data)
}

public suspend fun uploadToOssResult(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    data: ByteArray,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): ApiResult<OSSResponse> {
    val resp = uploadToOss(client, token, data, ext)

    val body = resp.bodyAsText()

    return decoder.decodeFromString(OSSResponse.apiResultDeserializationStrategy, body)
}

/**
 * @throws ApiResultNotSuccessException
 */
public suspend fun uploadToOssData(
    client: HttpClient,
    token: MiyousheVillaApiToken,
    data: ByteArray,
    ext: String,
    decoder: Json = MiyousheVilla.DefaultJson
): OSSResponse {
    return uploadToOssResult(client, token, data, ext).dataIfSuccess
}
