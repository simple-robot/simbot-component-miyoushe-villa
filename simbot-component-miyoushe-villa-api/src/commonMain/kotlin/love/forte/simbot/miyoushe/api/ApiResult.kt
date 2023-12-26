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

package love.forte.simbot.miyoushe.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import love.forte.simbot.miyoushe.api.ApiResult.Companion.EMPTY_SUCCESS_RESULT
import love.forte.simbot.miyoushe.api.ApiResult.Companion.RETCODE_DEFAULT_SUCCESS
import kotlin.js.JsExport

/**
 * 基本的数据响应结果.
 *
 * [D] 为预期中的响应结果类型。
 * 不应为 nullable 类型，如果想要代表无数据，考虑使用 [Unit] 等 `object` 类型。
 * 当 [retcode] 不为 [RETCODE_DEFAULT_SUCCESS] 时，[data] 大概率为 `null`。
 *
 * @param D 预期中的响应结果。不应为 nullable 类型，
 * 如果想要代表无数据，考虑使用 [Unit] 等 `object` 类型。
 */
@Suppress("NON_EXPORTABLE_TYPE")
@Serializable
@JsExport
public data class ApiResult<out D : Any>(
    val retcode: Int = RETCODE_DEFAULT_SUCCESS,
    val message: String? = null,
    val data: D? = null
) {
    /**
     * [retcode] 是否等于 [RETCODE_DEFAULT_SUCCESS]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    public val isSuccess: Boolean get() = retcode == RETCODE_DEFAULT_SUCCESS

    /**
     * 如果 [retcode] 等于 [RETCODE_DEFAULT_SUCCESS] 则得到 [data] 的非 null 值，
     * 否则抛出 [ApiResultNotSuccessException]
     *
     * @throws ApiResultNotSuccessException 如果结果不是成功
     */
    public val dataIfSuccess: D
        get() = if (!isSuccess || data == null)
            throw ApiResultNotSuccessException(this) else data

    public companion object {
        internal val EMPTY_SUCCESS_RESULT = ApiResult(data = Unit)
        public const val RETCODE_DEFAULT_SUCCESS: Int = 0
        // 200 OK {"data":null,"message":"机器人没有数据访问权限，详见https://webstatic.mihoyo.com/vila/bot/doc/question_answer.html","retcode":10322003}
        //  👆 也就是大别野ID错了

        // 200 OK {"data":null,"message":"房间不存在","retcode":10315000}
        // 200 OK {"data":null,"message":"用户不在大别野内","retcode":-1}

        public const val RETCODE_BOT_ACCESS_DEFIED: Int = 10322003
        public const val RETCODE_ROOM_NOT_EXISTS: Int = 10315000
        public const val RETCODE_MEMBER_NOT_IN_VILLA: Int = -1


        public fun emptySerializer(): KSerializer<ApiResult<Unit>> = EmptyApiResultSerializer
    }
}

private object EmptyApiResultSerializer : KSerializer<ApiResult<Unit>> {
    private val EMPTY_RESULT = ApiResult.serializer(Unit.serializer())
    override val descriptor: SerialDescriptor = EMPTY_RESULT.descriptor

    override fun deserialize(decoder: Decoder): ApiResult<Unit> {
        val deserialized = EMPTY_RESULT.deserialize(decoder)
        if (deserialized.data == null && deserialized.isSuccess) {
            if (deserialized.message == null) {
                return EMPTY_SUCCESS_RESULT
            }

            return deserialized.copy(data = Unit)
        }

        return deserialized
    }

    override fun serialize(encoder: Encoder, value: ApiResult<Unit>) {
        EMPTY_RESULT.serialize(encoder, value)
    }
}
