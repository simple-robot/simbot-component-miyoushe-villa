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

package love.forte.simbot.miyoushe.api.role

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [获取身份组](https://webstatic.mihoyo.com/vila/bot/doc/role_api/get_member_role_info.html)
 *
 * 获取身份组信息
 *
 * `GET /vila/api/bot/platform/getMemberRoleInfo`
 *
 * @author ForteScarlet
 */
public class GetMemberRoleApi private constructor(private val roleId: ULong) :
    MiyousheVillaGetApi<GetMemberRoleInfoResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getMemberRoleInfo"
        private val RESULT_RES = ApiResult.serializer(GetMemberRoleInfoResult.serializer())

        /**
         * Create an instance of [GetMemberRoleApi]
         * @param roleId 身份组 id
         */
        @JvmStatic
        @JvmName("create")
        public fun create(roleId: ULong): GetMemberRoleApi = GetMemberRoleApi(roleId)

        /**
         * Create an instance of [GetMemberRoleApi]
         * @param roleIdString 身份组 id
         *
         * @throws NumberFormatException 如果 [roleIdString] 不能转化为 [ULong]
         */
        @JvmStatic
        public fun create(roleIdString: String): GetMemberRoleApi = create(roleIdString.toULong())
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetMemberRoleInfoResult>
        get() = GetMemberRoleInfoResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetMemberRoleInfoResult>>
        get() = RESULT_RES

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        with(parameters) {
            append("role_id", roleId.toString())
        }
    }

    override fun toString(): String {
        return "GetMemberRoleApi(roleId=$roleId)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GetMemberRoleApi) return false

        if (roleId != other.roleId) return false

        return true
    }

    override fun hashCode(): Int {
        return roleId.hashCode()
    }

}


/**
 * Result of [GetMemberRoleApi]
 * @property role [MemberRole]
 *
 */
@Serializable
public data class GetMemberRoleInfoResult(val role: VillaRole)
