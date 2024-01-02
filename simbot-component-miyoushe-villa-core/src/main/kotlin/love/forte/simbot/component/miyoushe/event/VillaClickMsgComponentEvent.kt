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

package love.forte.simbot.component.miyoushe.event

import love.forte.simbot.ID
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.Event
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.ClickMsgComponent


/**
 * 点击消息组件回传事件
 *
 * @see ClickMsgComponent
 *
 * @author ForteScarlet
 */
public abstract class VillaClickMsgComponentEvent : VillaEvent<ClickMsgComponent>() {
    //region delegates
    /**
     * @see ClickMsgComponent.villaId
     */
    public val villaId: ID get() = sourceEventExtend.villaId.ID

    /**
     * @see ClickMsgComponent.roomId
     */
    public val roomId: ID get() = sourceEventExtend.roomId.ID

    /**
     * @see ClickMsgComponent.componentId
     */
    public val componentId: ID get() = sourceEventExtend.componentId.ID

    /**
     * @see ClickMsgComponent.msgUid
     */
    public val msgUid: ID get() = sourceEventExtend.msgUid.ID

    /**
     * @see ClickMsgComponent.uid
     */
    public val uid: ID get() = sourceEventExtend.uid.ID

    /**
     * @see ClickMsgComponent.botMsgId
     */
    public val botMsgId: ID get() = sourceEventExtend.botMsgId.ID

    /**
     * @see ClickMsgComponent.templateId
     */
    public val templateId: ID get() = sourceEventExtend.templateId.ID

    /**
     * @see ClickMsgComponent.extra
     */
    public val extra: String get() = sourceEventExtend.extra
    //endregion

    override val key: Event.Key<VillaClickMsgComponentEvent>
        get() = Key

    override fun toString(): String = "VillaClickMsgComponentEvent(sourceEvent=$sourceEvent)"

    public companion object Key : BaseEventKey<VillaClickMsgComponentEvent>(
        "villa.click_msg_component", VillaEvent
    ) {
        override fun safeCast(value: Any): VillaClickMsgComponentEvent? = doSafeCast(value)
    }
}
