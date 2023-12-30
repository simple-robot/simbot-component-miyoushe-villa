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
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [编辑分组](https://webstatic.mihoyo.com/vila/bot/doc/room_api/edit_group.html)
 *
 * 编辑分组，只允许编辑分组的名称
 *
 * `POST /vila/api/bot/platform/editGroup`
 *
 * @author ForteScarlet
 */
public class EditGroupApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/editGroup"

        /**
         * Create an instance of [EditGroupApi].
         *
         * @param groupId 分组 id
         * @param groupName 大别野 名称
         */
        @JvmStatic
        @JvmName("create")
        public fun create(groupId: ULong, groupName: String): EditGroupApi = EditGroupApi(Body(groupId, groupName))

        /**
         * Create an instance of [EditGroupApi].
         *
         * @param groupIdString 分组 id 字符串值
         * @param groupName 大别野 名称
         *
         * @throws NumberFormatException 如果 [groupIdString] 不能转化为 [ULong]
         */
        @JvmStatic
        public fun create(groupIdString: String, groupName: String): EditGroupApi =
            create(groupIdString.toULong(), groupName)
    }

    override val path: String
        get() = PATH

    /**
     * Body of [EditGroupApi]
     * @property groupId 分组 id
     * @property groupName 大别野 名称
     */
    @Serializable
    public data class Body(
        @SerialName("group_id")
        @Serializable(ULongWriteStringSerializer::class)
        val groupId: ULong,
        @SerialName("group_name") val groupName: String
    )

    override fun toString(): String {
        return "EditGroupApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EditGroupApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}
