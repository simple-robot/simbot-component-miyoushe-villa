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

package love.forte.simbot.miyoushe.api.villa

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

/**
 * [大别野 Villa](https://webstatic.mihoyo.com/vila/bot/doc/villa_api/)
 *
 * @property villaId 大别野 id
 * @property name 名称
 * @property villaAvatarUrl 别野头像链接
 * @property ownerUid 别野主人 id
 * @property isOfficial 是否是官方别野
 * @property introduce 介绍
 * @property categoryId 分类ID
 * @property tags 标签
 *
 * @author ForteScarlet
 */
@Serializable
public data class Villa(
    @get:JvmName("getVillaId")
    @SerialName("villa_id")
    val villaId: ULong,
    val name: String,
    @SerialName("villa_avatar_url")
    val villaAvatarUrl: String,
    @get:JvmName("getOwnerUid")
    @SerialName("owner_uid")
    val ownerUid: ULong,
    @SerialName("is_official")
    val isOfficial: Boolean = false,
    val introduce: String,
    @get:JvmName("getCategoryId")
    @SerialName("category_id")
    val categoryId: UInt,
    val tags: List<String> = emptyList(),
) {
    val villaIdStrValue: String get() = villaId.toString()
    val ownerUidStrValue: String get() = ownerUid.toString()
    val categoryIdStrValue: String get() = categoryId.toString()
}

