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

package love.forte.simbot.miyoushe.api.audit

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [审核](https://webstatic.mihoyo.com/vila/bot/doc/audit_api/audit.html)
 *
 * 审核用户配置内容是否合规，调用成功后会返回审核事件id(audit_id)。审核结果会通过回调接口异步通知。
 *
 * `POST /vila/api/bot/platform/audit`
 *
 * @author ForteScarlet
 */
public class AuditApi private constructor(override val body: Body) : MiyousheVillaPostApi<AuditResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/audit"
        private val RESULT_SER = ApiResult.serializer(AuditResult.serializer())

        /**
         * 得到一个用于构建的 builder。
         */
        @JvmStatic
        public fun builder(): Builder = Builder()

    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<AuditResult>
        get() = AuditResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<AuditResult>>
        get() = RESULT_SER

    /**
     * Body of [AuditApi]
     *
     * @property auditContent 待审核内容，必填
     * @property passThrough 透传信息，该字段会在审核结果回调时携带给开发者，选填
     * @property roomId 房间 id，选填
     * @property uid 用户 id, 必填
     * @property contentType 送审内容的类型. 支持类型：文本(`AuditContentTypeText`)、图片(`AuditContentTypeImage`)
     *
     */
    @Serializable
    public data class Body(
        @SerialName("audit_content") val auditContent: String,
        @SerialName("pass_through") val passThrough: String?,
        @SerialName("room_id") val roomId: ULong?,
        val uid: ULong,

        /**
         * @see AuditContentType
         */
        @SerialName("content_type") val contentType: String,
    )


    /**
     * A builder for [AuditApi]
     *
     * @property auditContent 待审核内容，必填
     * @property passThrough 透传信息，该字段会在审核结果回调时携带给开发者，选填
     * @property roomId 房间 id，选填
     * @property uid 用户 id, 必填
     * @property contentType 送审内容的类型. 支持类型：文本(`AuditContentTypeText`)、图片(`AuditContentTypeImage`)
     */
    public class Builder {
        @JsName("auditContentValue")
        public var auditContent: String? = null

        @JsName("passThroughValue")
        public var passThrough: String? = null

        @JsName("roomIdValue")
        public var roomId: ULong? = null

        @JsName("uidValue")
        public var uid: ULong? = null

        @JsName("contentTypeValue")
        public var contentType: String? = null

        public fun auditContent(value: String): Builder = apply {
            this.auditContent = value
        }

        public fun passThrough(value: String): Builder = apply {
            this.passThrough = value
        }

        @JvmName("roomId")
        public fun roomId(value: ULong): Builder = apply {
            this.roomId = value
        }

        @JvmName("uid")
        public fun uid(value: ULong): Builder = apply {
            this.uid = value
        }

        /**
         * @throws NumberFormatException 如果 [valueString] 无法转化为 [UInt]
         */
        @JvmName("roomId")
        public fun roomId(valueString: String): Builder = apply {
            this.roomId = valueString.toULong()
        }

        /**
         * @throws NumberFormatException 如果 [valueString] 无法转化为 [ULong]
         */
        @JvmName("uid")
        public fun uid(valueString: String): Builder = apply {
            this.uid = valueString.toULong()
        }

        public fun contentType(value: String): Builder = apply {
            this.contentType = value
        }

        public fun contentType(value: AuditContentType): Builder = apply {
            this.contentType = value.value
        }


        /**
         * 构建结果。
         *
         * @throws IllegalArgumentException 如果必选属性未被设置
         */
        public fun build(): AuditApi = AuditApi(
            Body(
                auditContent = auditContent ?: throw IllegalArgumentException("required 'auditContent' was null"),
                passThrough = passThrough,
                roomId = roomId,
                uid = uid ?: throw IllegalArgumentException("required 'uid' was null"),
                contentType = contentType ?: throw IllegalArgumentException("required 'uid' was null"),
            )
        )
    }

    override fun toString(): String {
        return "AuditApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AuditApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}

/**
 * Result of [AuditApi]
 *
 * @property auditId 审核事件 id
 */
@Serializable
public data class AuditResult(val auditId: String)

/**
 * 送审内容的类型
 *
 * 支持类型：文本、图片
 * 如果送审内容为文本，则 `content_type=AuditContentTypeText`, 将送审内容填充到 `audit_content` 字段中。
 *
 * 如果送审内容是图片，则 `content_type=AuditContentTypeImage`，将图片url填充到 `audit_content` 字段中。
 *
 * 注：
 * - 如果content_type传入值为空，默认当作文本类型处理
 * - 如果送审图片，需先调用[转存接口](https://webstatic.mihoyo.com/vila/bot/doc/img_api/transfer.html)，将转存后的URL填充到audit_content中
 * @see AuditApi.Body.contentType
 */
public enum class AuditContentType(public val value: String) {
    TEXT("AuditContentTypeText"),
    IMAGE("AuditContentTypeImage")
}
