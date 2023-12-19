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

import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [编辑身份组](https://webstatic.mihoyo.com/vila/bot/doc/role_api/edit_member_role.html)
 *
 * 编辑身份组，可以修改身份组的名称、颜色和权限。
 *
 * `POST /vila/api/bot/platform/editMemberRole`
 *
 * @author ForteScarlet
 */
public class EditMemberRoleApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/editMemberRole"


        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param id 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        @JvmName("create")
        public fun create(id: ULong, name: String, color: String, permissions: Collection<String>): EditMemberRoleApi =
            EditMemberRoleApi(Body(id, name, color, permissions))

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param id 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        @JvmName("create")
        public fun create(id: ULong, name: String, color: String, vararg permissions: String): EditMemberRoleApi =
            create(id, name, color, permissions.asList())

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param id 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        @JvmName("createByPermissions")
        public fun createByPermissions(
            id: ULong,
            name: String,
            color: String,
            permissions: Collection<RolePermission>
        ): EditMemberRoleApi =
            create(id, name, color, permissions.map { it.name.lowercase() })

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param id 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         */
        @JvmStatic
        @JvmName("createByPermissions")
        public fun createByPermissions(
            id: ULong,
            name: String,
            color: String,
            vararg permissions: RolePermission
        ): EditMemberRoleApi =
            create(id, name, color, permissions.map { it.name.lowercase() })

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param idString 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         * @throws NumberFormatException 如果 [idString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(
            idString: String,
            name: String,
            color: String,
            permissions: Collection<String>
        ): EditMemberRoleApi =
            create(idString.toULong(), name, color, permissions)

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param idString 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         * @throws NumberFormatException 如果 [idString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(
            idString: String,
            name: String,
            color: String,
            vararg permissions: String
        ): EditMemberRoleApi =
            create(idString.toULong(), name, color, permissions = permissions)

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param idString 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         * @throws NumberFormatException 如果 [idString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun createByPermissions(
            idString: String,
            name: String,
            color: String,
            permissions: Collection<RolePermission>
        ): EditMemberRoleApi =
            createByPermissions(idString.toULong(), name, color, permissions)

        /**
         * Create an instance of [EditMemberRoleApi]
         *
         * @param idString 身份组 id
         * @param name 身份组名称
         * @param color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
         * @param permissions 权限列表，可选项见[权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
         *
         * @throws NumberFormatException 如果 [idString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun createByPermissions(
            idString: String,
            name: String,
            color: String,
            vararg permissions: RolePermission
        ): EditMemberRoleApi =
            createByPermissions(idString.toULong(), name, color, permissions = permissions)
    }

    override val path: String
        get() = PATH

    /**
     * Body of [EditMemberRoleApi].
     *
     * @property id 身份组 id
     * @property name 身份组名称
     * @property color 身份组颜色，可选项见颜色
     * @property permissions 权限列表，可选项见权限
     *
     */
    @Serializable
    public data class Body(
        val id: ULong, val name: String, val color: String, val permissions: Collection<String>
    )

    override fun toString(): String {
        return "EditMemberRoleApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EditMemberRoleApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}
