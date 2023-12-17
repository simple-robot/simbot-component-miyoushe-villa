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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [创建身份组](https://webstatic.mihoyo.com/vila/bot/doc/role_api/create_member_role.html)
 *
 * 为大别野创建新的身份组，可以设置该身份组拥有的权限，并为身份组添加用户。
 *
 * `POST /vila/api/bot/platform/createMemberRole`
 *
 * @author ForteScarlet
 */
public class CreateMemberRoleApi private constructor(override val body: Body) :
    MiyousheVillaPostApi<CreateMemberRoleResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/createMemberRole"
        private val RESULT_SER = ApiResult.serializer(CreateMemberRoleResult.serializer())

        /**
         * Create an instance of [CreateMemberRoleApi]
         *
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        public fun create(name: String, color: String, permissions: Collection<String>): CreateMemberRoleApi =
            CreateMemberRoleApi(Body(name, color, permissions))

        /**
         * Create an instance of [CreateMemberRoleApi]
         *
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        public fun create(name: String, color: String, vararg permissions: String): CreateMemberRoleApi =
            create(name, color, permissions.asList())

        /**
         * Create an instance of [CreateMemberRoleApi]
         *
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        public fun createByPermissions(name: String, color: String, permissions: Collection<RolePermission>): CreateMemberRoleApi =
            create(name, color, permissions.map { it.name.lowercase() })

        /**
         * Create an instance of [CreateMemberRoleApi]
         *
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        public fun createByPermissions(name: String, color: String, vararg permissions: RolePermission): CreateMemberRoleApi =
            create(name, color, permissions.map { it.name.lowercase() })
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<CreateMemberRoleResult>
        get() = CreateMemberRoleResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<CreateMemberRoleResult>>
        get() = RESULT_SER

    /**
     * Body of [CreateMemberRoleApi]
     * @property name 身份组名称
     * @property color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
     * @property permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
     */
    @Serializable
    public data class Body(val name: String, val color: String, val permissions: Collection<String>)

    override fun toString(): String {
        return "CreateMemberRoleApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CreateMemberRoleApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }

}

/**
 * Result for [CreateMemberRoleApi]
 *
 * @property id 身份组 id
 */
@Serializable
public data class CreateMemberRoleResult(@get:JvmName("getId") val id: ULong) {
    val idStrValue: String get() = id.toString()
}
