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

package love.forte.simbot.component.miyoushe.internal

import love.forte.simbot.ID
import love.forte.simbot.component.miyoushe.VillaGuild
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.VillaRoom
import love.forte.simbot.component.miyoushe.internal.bot.VillaBotImpl
import love.forte.simbot.component.miyoushe.utils.toULong
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.ApiResultNotSuccessException
import love.forte.simbot.miyoushe.api.villa.Villa
import love.forte.simbot.utils.item.Items
import love.forte.simbot.utils.item.effectedItemsByFlow


/**
 *
 * @author ForteScarlet
 */
internal class VillaGuildImpl(
    override val bot: VillaBotImpl,
    override val source: Villa,
) : VillaGuild {

    override suspend fun owner(): VillaMember =
        bot.memberInternal(source.ownerUid, source.villaIdStrValue, this)

    override val channels: Items<VillaRoom>
        get() = bot.effectedItemsByFlow { bot.roomFlowInternal(source.villaIdStrValue, this) }

    override suspend fun channel(id: ID): VillaRoom? {
        try {
            return bot.roomInternal(id.toULong(), source.villaIdStrValue, this)
        } catch (ae: ApiResultNotSuccessException) {
            if (ae.apiResult.retcode == ApiResult.RETCODE_ROOM_NOT_EXISTS) {
                return null
            }

            throw ae
        }
    }

    override suspend fun member(id: ID): VillaMember? {
        try {
            return bot.memberInternal(id.toULong(), source.villaIdStrValue, this)
        } catch (ae: ApiResultNotSuccessException) {
            if (ae.apiResult.retcode == ApiResult.RETCODE_ROOM_NOT_EXISTS) {
                return null
            }

            throw ae
        }
    }

    override val members: Items<VillaMember>
        get() = bot.effectedItemsByFlow { bot.memberFlowInternal(source.villaIdStrValue) }

    override fun toString(): String = "VillaGuild(source=$source)"
}
