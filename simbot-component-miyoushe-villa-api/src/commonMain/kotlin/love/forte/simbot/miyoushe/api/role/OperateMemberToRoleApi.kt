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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [向身份组操作用户](https://webstatic.mihoyo.com/vila/bot/doc/role_api/operate_member_to_role.html)
 *
 * 向身份组操作用户，包括把用户添加到身份组或者从身份组删除用户
 *
 * `POST /vila/api/bot/platform/operateMemberToRole`
 *
 * @author ForteScarlet
 */
public class OperateMemberToRoleApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/operateMemberToRole"

        /**
         * Create an instance of [OperateMemberToRoleApi]
         *
         * @param roleId 身份组 id
         * @param uid 用户 id
         * @param isAdd 是否是添加用户
         */
        @JvmStatic
        @JvmName("create")
        public fun create(roleId: ULong, uid: ULong, isAdd: Boolean): OperateMemberToRoleApi =
            OperateMemberToRoleApi(Body(roleId, uid, isAdd))

        /**
         * Create an instance of [OperateMemberToRoleApi]
         *
         * @param roleIdString 身份组 id
         * @param uidString 用户 id
         * @param isAdd 是否是添加用户
         *
         * @throws NumberFormatException 如果 [roleIdString] 或 [uidString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(roleIdString: String, uidString: String, isAdd: Boolean): OperateMemberToRoleApi =
            create(roleIdString.toULong(), uidString.toULong(), isAdd)

        /**
         * Create an instance of [OperateMemberToRoleApi] for add (`is_add = true`).
         *
         * @param roleId 身份组 id
         * @param uid 用户 id
         */
        @JvmStatic
        @JvmName("createForAdd")
        public fun createForAdd(roleId: ULong, uid: ULong): OperateMemberToRoleApi =
            create(roleId, uid, true)

        /**
         * Create an instance of [OperateMemberToRoleApi] for add (`is_add = true`).
         *
         * @param roleIdString 身份组 id
         * @param uidString 用户 id
         *
         * @throws NumberFormatException 如果 [roleIdString] 或 [uidString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun createForAdd(roleIdString: String, uidString: String): OperateMemberToRoleApi =
            createForAdd(roleIdString.toULong(), uidString.toULong())

        /**
         * Create an instance of [OperateMemberToRoleApi] for delete (`is_add = false`)
         *
         * @param roleId 身份组 id
         * @param uid 用户 id
         */
        @JvmStatic
        @JvmName("createForRemove")
        public fun createForRemove(roleId: ULong, uid: ULong): OperateMemberToRoleApi =
            create(roleId, uid, false)

        /**
         * Create an instance of [OperateMemberToRoleApi] for delete (`is_add = false`)
         *
         * @param roleIdString 身份组 id
         * @param uidString 用户 id
         *
         * @throws NumberFormatException 如果 [roleIdString] 或 [uidString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun createForRemove(roleIdString: String, uidString: String): OperateMemberToRoleApi =
            createForRemove(roleIdString.toULong(), uidString.toULong())
    }

    override val path: String
        get() = PATH

    /**
     * Body of [OperateMemberToRoleApi]
     * @property roleId 身份组 id
     * @property uid 用户 id
     * @property isAdd 是否是添加用户
     */
    @Serializable
    public data class Body(
        @SerialName("role_id")
        @Serializable(ULongWriteStringSerializer::class)
        val roleId: ULong,
        @Serializable(ULongWriteStringSerializer::class)
        val uid: ULong,

        @SerialName("is_add") val isAdd: Boolean
    )

    override fun toString(): String {
        return "OperateMemberToRoleApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OperateMemberToRoleApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}

/*
请求
字段名	类型	描述
role_id	uint64	身份组 id
uid	uint64	用户 id
is_add	bool	是否是添加用户
 */
