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

@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package love.forte.simbot.miyoushe.api.image

import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [图片上传参数接口](https://webstatic.mihoyo.com/vila/bot/doc/img_api/upload.html)
 *
 * 获取米游社阿里云 OSS 上传参数
 *
 * `GET /vila/api/bot/platform/getUploadImageParams`
 *
 * @author ForteScarlet
 */
public class GetUploadImageParamsApi private constructor(
    public val md5: String,
    public val ext: String,
) : MiyousheVillaGetApi<GetUploadImageParamsResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getUploadImageParams"
        private val RESULT_SER = ApiResult.serializer(GetUploadImageParamsResult.serializer())

        /** ext: `jpg` */
        public const val EXT_JPG: String = "jpg"

        /** ext: `jpeg` */
        public const val EXT_JPEG: String = "jpeg"

        /** ext: `png` */
        public const val EXT_PNG: String = "png"

        /** ext: `gif` */
        public const val EXT_GIF: String = "gif"

        /** ext: `bmp` */
        public const val EXT_BMP: String = "bmp"

        private const val EXT_JPG_B: UInt = 0xFFD8FFu
        //private const val EXT_JPEG_B: UInt = EXT_JPG_B
        private const val EXT_PNG_B: UInt = 0x89504E47u
        private const val EXT_GIF_B: UInt = 0x47494638u
        private const val EXT_BMP_B: UInt = 0x424Du

        public fun ext(filename: String): String? {
            val fileExt = filename.substringAfterLast('.', "").takeIf { it.isNotEmpty() } ?: return null

            return when (fileExt.lowercase()) {
                EXT_JPG -> EXT_JPG
                EXT_JPEG -> EXT_JPEG
                EXT_PNG -> EXT_PNG
                EXT_GIF -> EXT_GIF
                EXT_BMP -> EXT_BMP
                else -> null
            }
        }

        @JsName("extFromBytes")
        public fun ext(bytes: ByteArray): String? {
            require(bytes.size >= 4) { "bytes.length must >= 4 (for read int)" }
            val head = ByteReadPacket(bytes).readInt().toUInt()

            return ext(head)
        }

        @JsName("extFromHead")
        @JvmName("ext")
        public fun ext(head: UInt): String? {
            return when  {
                (head and 0xffffff00u) shr 8 == EXT_JPG_B -> EXT_JPG
//                EXT_JPEG_B -> EXT_JPEG
                head == EXT_PNG_B -> EXT_PNG
                head == EXT_GIF_B -> EXT_GIF
                (head and 0xffff0000u) shr 16 == EXT_BMP_B -> EXT_BMP
                else -> null
            }
        }

        /**
         * Create an instance of [GetUploadImageParamsApi]
         *
         * @param md5 图片的 md5，最后会被用于文件名
         * @param ext 图片扩展名 (支持 jpg,jpeg,png,gif,bmp), 可参考 [GetUploadImageParamsApi] 中以 `EXT_` 为前缀的常量，例如 [EXT_JPG]。
         */
        @JvmStatic
        public fun create(md5: String, ext: String): GetUploadImageParamsApi = GetUploadImageParamsApi(md5, ext)

    }

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        with(parameters) {
            append("md5", md5)
            append("ext", ext)
        }
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetUploadImageParamsResult>
        get() = GetUploadImageParamsResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetUploadImageParamsResult>>
        get() = RESULT_SER

    override fun toString(): String {
        return "GetUploadImageParamsApi(md5='$md5', ext='$ext')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GetUploadImageParamsApi) return false

        if (md5 != other.md5) return false
        if (ext != other.ext) return false

        return true
    }

    override fun hashCode(): Int {
        var result = md5.hashCode()
        result = 31 * result + ext.hashCode()
        return result
    }
}

/**
 * Result of [GetUploadImageParamsApi]
 *
 * @property type 上传类型，oss
 * @property fileName 上传后文件名
 * @property maxFileSize 上传文件大小限制，单位 KB
 * @property params 上传参数对象 oss 上传参数
 */
@Serializable
public data class GetUploadImageParamsResult(
    val type: String,
    @SerialName("file_name")
    val fileName: String,

    @SerialName("max_file_size")
    @get:JvmName("getMaxFileSize")
    val maxFileSize: UInt,
    val params: OSSParams,
) {
    public val maxFileSizeStrValue: String get() = maxFileSize.toString()
}

/**
 * 阿里云 oss 上传参数
 */
@Serializable
public data class OSSParams(
    val accessid: String,
    val callback: String,
    @SerialName("callback_var")
    val callbackVar: CallbackVar,
    val dir: String,
    val expire: String,
    val host: String,
    val name: String,
    val policy: String,
    val signature: String,
    @SerialName("x_oss_content_type")
    val xOssContentType: String,
    @SerialName("object_acl")
    val objectAcl: String,
    @SerialName("content_disposition")
    val contentDisposition: String,
    val key: String,
    @SerialName("success_action_status")
    val successActionStatus: String,
) {
    @Serializable
    public data class CallbackVar(@SerialName("x:extra") val xExtra: String)
}

/**
 * OSS 相关操作可能产生的异常。
 */
public open class OSSException : RuntimeException {
    @JsName("create_0")
    public constructor() : super()

    @JsName("create_1")
    public constructor(message: String?) : super(message)

    @JsName("create_2")
    public constructor(message: String?, cause: Throwable?) : super(message, cause)

    @JsName("create_3")
    public constructor(cause: Throwable?) : super(cause)
}

/**
 * OSS 上传操作可能产生的异常。
 */
@Suppress("MemberVisibilityCanBePrivate")
public class OSSUploadException : OSSException {

    @JsName("create_0")
    public constructor() : super()

    @JsName("create_1")
    public constructor(message: String?) : super(message)

    @JsName("create_2")
    public constructor(message: String?, cause: Throwable?) : super(message, cause)

    @JsName("create_3")
    public constructor(cause: Throwable?) : super(cause)
}

/*
{
	"retcode": 0,
	"msg": "success",
	"data": {
		"url": "https://upload-bbs.miyoushe.com/backend/vila/bot/platform/img/2023/12/19/bot_uyoiEQHXtqFgBLHdoBJg/7b4ee14a89b3da27e4a65538a118d211_825647531035405021.png",
		"secret_url": "",
		"object": "backend/vila/bot/platform/img/2023/12/19/bot_uyoiEQHXtqFgBLHdoBJg/7b4ee14a89b3da27e4a65538a118d211_825647531035405021.png"
	}
}
 */
/**
 * OSS 文件上传后的响应结果。
 *
 * > 通过接口测试得到，文档中暂无描述
 *
 * ```json
 * {
 * 	"retcode": 0,
 * 	"msg": "success",
 * 	"data": {
 * 		"url": "https://upload-bbs.miyoushe.com/xxx.png",
 * 		"secret_url": "",
 * 		"object": "xxx.png"
 * 	}
 * }
 * ```
 *
 */
@Serializable
public data class OSSResponse(
    val url: String,
    @SerialName("secret_url")
    val secretUrl: String = "",
    @SerialName("object")
    val objectValue: String,
) {
    public companion object {
        /**
         * 被 [ApiResult] 包裹的 [KSerializer].
         *
         */
        private val RESULT_SER: KSerializer<ApiResult<OSSResponse>> = ApiResult.serializer(serializer())

        /**
         * 获取被 [ApiResult] 包裹的 [KSerializer].
         */
        @get:JvmStatic
        public val apiResultDeserializationStrategy: DeserializationStrategy<ApiResult<OSSResponse>>
            get() = RESULT_SER
    }
}
