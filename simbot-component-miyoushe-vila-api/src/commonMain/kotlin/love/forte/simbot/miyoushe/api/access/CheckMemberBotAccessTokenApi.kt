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

package love.forte.simbot.miyoushe.api.access

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.jvm.JvmStatic


/**
 * [校验用户机器人访问凭证](https://webstatic.mihoyo.com/vila/bot/doc/auth_api/check_member_bot_access_token.html)
 *
 * `POST /vila/api/bot/platform/checkMemberBotAccessToken`
 *
 * @author ForteScarlet
 */
public class CheckMemberBotAccessTokenApi private constructor(override val body: Body) :
    MiyousheVillaPostApi<CheckMemberBotAccessTokenResult>() {
    public companion object Factory {
        private val RESULT_SER = ApiResult.serializer(CheckMemberBotAccessTokenResult.serializer())
        private const val PATH = "/vila/api/bot/platform/checkMemberBotAccessToken"

        /**
         * Create a [CheckMemberBotAccessTokenApi] instance.
         */
        @JvmStatic
        public fun create(token: String): CheckMemberBotAccessTokenApi =
            CheckMemberBotAccessTokenApi(Body(token))
    }

    override val path: String
        get() = PATH

    override val resultSerializer: DeserializationStrategy<CheckMemberBotAccessTokenResult>
        get() = CheckMemberBotAccessTokenResult.serializer()

    override val apiResultSerializer: DeserializationStrategy<ApiResult<CheckMemberBotAccessTokenResult>>
        get() = RESULT_SER

    /**
     * Request Body for [CheckMemberBotAccessTokenApi].
     */
    @Serializable
    public data class Body(val token: String)

    override fun toString(): String {
        return "CheckMemberBotAccessTokenApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CheckMemberBotAccessTokenApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}


/**
 * Result of [CheckMemberBotAccessTokenApi]
 *
 * @property accessInfo token 解析的用户信息
 * @property member 用户详细信息
 *
 * @see CheckMemberBotAccessTokenApi
 */
@Serializable
public data class CheckMemberBotAccessTokenResult(
    val accessInfo: BotMemberAccessInfo,
    val member: String // TODO
)
