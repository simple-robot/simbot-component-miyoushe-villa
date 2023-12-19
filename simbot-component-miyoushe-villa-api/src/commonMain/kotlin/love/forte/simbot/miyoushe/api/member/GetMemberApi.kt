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

package love.forte.simbot.miyoushe.api.member

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [获取用户信息](https://webstatic.mihoyo.com/vila/bot/doc/member_api/get_member.html)
 *
 * 获取用户信息
 *
 * `GET /vila/api/bot/platform/getMember`
 *
 *
 * @author ForteScarlet
 */
public class GetMemberApi private constructor(private val uid: ULong) : MiyousheVillaGetApi<GetMemberResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getMember"
        private val RESULT_SER = ApiResult.serializer(GetMemberResult.serializer())

        /**
         * 通过用户 id 构建 [GetMemberApi].
         *
         * @param uid 用户 id, 应当是一个 [Unsigned Long][ULong] (Java 中参考 `Long` 的无符号操作相关API)
         */
        @JvmStatic
        @JvmName("create")
        public fun create(uid: ULong): GetMemberApi = GetMemberApi(uid)

        /**
         * 通过用户 id 构建 [GetMemberApi]。
         * 如果提供的 [uidString] 无法被转化为 [ULong] 则会抛出 [NumberFormatException]。
         *
         * @param uidString 用户 id 的字面值字符串。
         *
         * @throws NumberFormatException
         *
         */
        @JvmStatic
        public fun create(uidString: String): GetMemberApi = create(uidString.toULong())

    }

    override val path: String
        get() = PATH

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        parameters.append("uid", uid.toString())
    }


    override val resultSerializer: DeserializationStrategy<GetMemberResult>
        get() = GetMemberResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetMemberResult>>
        get() = RESULT_SER

    override fun toString(): String {
        return "GetMemberApi(uid=$uid)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GetMemberApi) return false

        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }


}

/**
 * Result of [GetMemberApi]
 *
 */
@Serializable
public data class GetMemberResult(val member: Member)

