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
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [删除身份组](https://webstatic.mihoyo.com/vila/bot/doc/role_api/delete_member_role.html)
 *
 * 删除身份组
 *
 * `POST /vila/api/bot/platform/deleteMemberRole`
 *
 * @author ForteScarlet
 */
public class DeleteMemberRoleApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/deleteMemberRole"

        /**
         * Create an instance of [DeleteMemberRoleApi]
         *
         * @param id 身份组 id
         */
        @JvmStatic
        @JvmName("create")
        public fun create(id: ULong): DeleteMemberRoleApi = DeleteMemberRoleApi(Body(id))

        /**
         * Create an instance of [DeleteMemberRoleApi]
         *
         * @param idString 身份组 id
         *
         * @throws NumberFormatException 如果 [idString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(idString: String): DeleteMemberRoleApi = create(idString.toULong())
    }

    override val path: String
        get() = PATH

    /**
     * Body of [DeleteMemberRoleApi]
     * @property id 身份组 id
     */
    @Serializable
    public data class Body(@Serializable(ULongWriteStringSerializer::class) val id: ULong)

    override fun toString(): String {
        return "DeleteMemberRoleApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeleteMemberRoleApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }


}
