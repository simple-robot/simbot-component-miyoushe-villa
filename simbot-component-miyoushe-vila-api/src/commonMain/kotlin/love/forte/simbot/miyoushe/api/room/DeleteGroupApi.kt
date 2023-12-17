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

package love.forte.simbot.miyoushe.api.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [删除分组](https://webstatic.mihoyo.com/vila/bot/doc/room_api/delete_group.html)
 *
 * 删除分组
 *
 * `POST /vila/api/bot/platform/deleteGroup`
 *
 * @author ForteScarlet
 */
public class DeleteGroupApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/deleteGroup"

        /**
         * Create an instance of [DeleteGroupApi].
         *
         * @param groupId 分组 id
         */
        @JvmStatic
        @JvmName("create")
        public fun create(groupId: ULong): DeleteGroupApi = DeleteGroupApi(Body(groupId))

        /**
         * Create an instance of [DeleteGroupApi].
         *
         * @param groupIdString 分组 id 字符串值
         *
         * @throws NumberFormatException 如果 [groupIdString] 不能转化为 [ULong]
         */
        @JvmStatic
        public fun create(groupIdString: String): DeleteGroupApi = create(groupIdString.toULong())
    }

    override val path: String
        get() = PATH


    /**
     * Body of [DeleteGroupApi]
     * @property groupId 分组 id
     */
    @Serializable
    public data class Body(@SerialName("group_id") val groupId: ULong)

    override fun toString(): String {
        return "DeleteGroupApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeleteGroupApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }


}
