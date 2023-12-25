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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.role.MemberRole
import kotlin.jvm.JvmName

/**
 * [用户大别野信息](https://webstatic.mihoyo.com/vila/bot/doc/member_api/)
 *
 * @property basic 用户基本信息
 * @property roleIdList 用户加入的身份组 id 列表
 * @property joinedAt ISO8601 timestamp 用户加入时间 (实际上好像是时间戳字符串)
 * @property roleList 用户已加入的身份组列表
 *
 * @author ForteScarlet
 */
@Serializable
public data class Member(
    val basic: MemberBasic,
    @get:JvmName("getRoleIdListUnsignedSource")
    @SerialName("role_id_list") val roleIdList: List<ULong> = emptyList(),
    @SerialName("joined_at") val joinedAt: Long,
    @SerialName("role_list") val roleList: List<MemberRole> = emptyList(),
) {
    /**
     * Friendly property provided to Java
     */
    public val roleIdListAsLong: List<Long>
        @JvmName("getRoleIdList")
        get() = with(roleIdList) {
            if (isEmpty()) emptyList() else map { it.toLong() }
        }
}

/**
 * [用户基础信息](https://webstatic.mihoyo.com/vila/bot/doc/member_api/)
 *
 * @property uid 用户 uid
 * @property nickname 昵称
 * @property introduce 个性签名
 * @property avatarUrl 头像链接
 *
 * @author ForteScarlet
 */
@Serializable
public data class MemberBasic(
    @get:JvmName("getUid")
    val uid: ULong,
    val nickname: String,
    val introduce: String = "",
    @SerialName("avatar_url") val avatarUrl: String,
) {
    val uidStrValue: String get() = uid.toString()
}
