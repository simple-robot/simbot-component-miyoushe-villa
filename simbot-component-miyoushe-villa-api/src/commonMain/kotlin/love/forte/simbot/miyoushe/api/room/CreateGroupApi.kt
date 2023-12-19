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

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaPostApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [创建分组](https://webstatic.mihoyo.com/vila/bot/doc/room_api/create_group.html)
 *
 * `POST /vila/api/bot/platform/createGroup`
 *
 * @author ForteScarlet
 */
public class CreateGroupApi private constructor(override val body: Body) : MiyousheVillaPostApi<CreateGroupResult>() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/createGroup"
        private val RESULT_RES = ApiResult.serializer(CreateGroupResult.serializer())

        /**
         * Create an instance of [CreateGroupApi].
         *
         * @param groupName 分组名称
         */
        @JvmStatic
        public fun create(groupName: String): CreateGroupApi = CreateGroupApi(Body(groupName))
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<CreateGroupResult>
        get() = CreateGroupResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<CreateGroupResult>>
        get() = RESULT_RES

    @Serializable
    public data class Body(@SerialName("group_name") val groupName: String)

    override fun toString(): String {
        return "CreateGroupApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CreateGroupApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }
}

/**
 * Result of [CreateGroupApi]
 *
 * @property groupId    分组 id
 */
@Serializable
public data class CreateGroupResult(@SerialName("group_id") @get:JvmName("getGroupId") val groupId: ULong) {
    val groupIdStrValue: String get() = groupId.toString()
}
