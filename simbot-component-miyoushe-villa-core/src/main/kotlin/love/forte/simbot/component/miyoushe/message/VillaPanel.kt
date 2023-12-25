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

package love.forte.simbot.component.miyoushe.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.ID
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.api.msg.Component
import love.forte.simbot.miyoushe.api.msg.Panel


/**
 * 消息组件
 *
 * @see Panel
 *
 * @author ForteScarlet
 */
@Serializable
@SerialName("villa.panel")
public data class VillaPanel(val source: Panel) : VillaMessageElement<VillaPanel>() {

    val templateId: ID? get() = source.templateId?.ID
    val smallComponentGroupList: List<Component>? get() = source.smallComponentGroupList
    val midComponentGroupList: List<Component>? get() = source.smallComponentGroupList
    val bigComponentGroupList: List<Component>? get() = source.smallComponentGroupList

    override val key: Message.Key<VillaPanel>
        get() = Key

    public companion object Key : Message.Key<VillaPanel> {
        override fun safeCast(value: Any): VillaPanel? = doSafeCast(value)
    }
}
