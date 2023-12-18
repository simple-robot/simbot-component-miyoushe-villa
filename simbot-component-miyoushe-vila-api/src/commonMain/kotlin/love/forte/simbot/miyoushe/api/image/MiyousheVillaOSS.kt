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
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlin.jvm.JvmSynthetic


/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileInputProvider: InputProvider): HttpResponse {
    return doUpload(client) {
        append("file", fileInputProvider)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileData: ByteArray): HttpResponse {
    return doUpload(client) {
        append("file", fileData)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileData: ByteReadPacket): HttpResponse {
    return doUpload(client) {
        append("file", fileData)
    }
}

/**
 * 使用 [OSSParams] 上传文件到米游社的阿里云 OSS。
 */
@JvmSynthetic
public suspend fun OSSParams.upload(client: HttpClient, fileChannelProvider: ChannelProvider): HttpResponse {
    return doUpload(client) {
        append("file", fileChannelProvider)
    }
}


private suspend inline fun OSSParams.doUpload(client: HttpClient, crossinline inForm: FormBuilder.() -> Unit): HttpResponse {
    return client.submitFormWithBinaryData(
        url = host,
        formData = formData {
            appendOSSParams(this@doUpload)
            inForm()
        }
    )
}

private fun FormBuilder.appendOSSParams(params: OSSParams) {
    append("x:extra", params.callbackVar.xExtra)
    append("OSSAccessKeyId", params.accessid)
    append("signature", params.signature)
    append("success_action_status", params.successActionStatus)
    append("name", params.name)
    append("callback", params.callback)
    append("x-oss-content-type", params.xOssContentType)
    append("key", params.key)
    append("policy", params.policy)
}

