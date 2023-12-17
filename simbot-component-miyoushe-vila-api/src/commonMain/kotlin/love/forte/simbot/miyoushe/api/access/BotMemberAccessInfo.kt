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

package love.forte.simbot.miyoushe.api.access

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

/**
 * 用户机器人访问信息
 *
 * @property uid 用户 id
 * @property villaId 大别野 id
 * @property memberAccessToken 用户机器人访问凭证
 * @property botTplId 机器人模板 id
 *
 * @see CheckMemberBotAccessTokenApi
 */
@Serializable
public data class BotMemberAccessInfo(
    @get:JvmName("getUid")
    val uid: ULong,
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
    @SerialName("member_access_token")
    val memberAccessToken: String,
    @SerialName("bot_tpl_id")
    val botTplId: String
) {
    val uidStrValue: String get() = uid.toString()
    val villaIdStrValue: String get() = villaId.toString()
}
