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

import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.MiyousheVillaPostEmptyResultApi
import love.forte.simbot.miyoushe.utils.serialization.ULongWriteStringSerializer
import kotlin.jvm.JvmStatic


/**
 * [踢出大别野用户](https://webstatic.mihoyo.com/vila/bot/doc/member_api/delete_villa_member.html)
 *
 * 踢出大别野用户
 *
 * `POST /vila/api/bot/platform/deleteVillaMember`
 *
 * @author ForteScarlet
 */
public class DeleteVillaMemberApi private constructor(override val body: Body) : MiyousheVillaPostEmptyResultApi() {
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/deleteVillaMember"

        /**
         * 提供 `用户 id` 并构建 [DeleteVillaMemberApi].
         *
         * @param uid 用户 id
         */
        public fun create(uid: ULong): DeleteVillaMemberApi = DeleteVillaMemberApi(Body(uid))

        /**
         * 提供 `用户 id` 并构建 [DeleteVillaMemberApi]。
         * 如果 [uidString] 无法被转化为 [ULong] 则会抛出 [NumberFormatException]。
         *
         * @param uidString 用户 id 的字符串值
         *
         * @throws NumberFormatException 当 [uidString] 无法转化为 [ULong]
         */
        @JvmStatic
        public fun create(uidString: String): DeleteVillaMemberApi = create(uidString.toULong())
    }

    override val path: String
        get() = PATH



    /**
     * Request body for [DeleteVillaMemberApi]
     *
     */
    @Serializable
    public data class Body(@Serializable(ULongWriteStringSerializer::class) val uid: ULong)

    override fun toString(): String {
        return "DeleteVillaMemberApi(body=$body)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DeleteVillaMemberApi) return false

        if (body != other.body) return false

        return true
    }

    override fun hashCode(): Int {
        return body.hashCode()
    }

}
