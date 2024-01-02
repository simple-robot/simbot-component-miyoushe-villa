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
import love.forte.simbot.JSTP
import love.forte.simbot.component.miyoushe.VillaRoom
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.ChannelEvent
import love.forte.simbot.event.Event
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.AddQuickEmoticon


/**
 * 大别野用户对机器人消息做出快捷表情表态事件。
 *
 * @see AddQuickEmoticon
 *
 * @author ForteScarlet
 */
public abstract class VillaAddQuickEmoticonEvent : VillaEvent<AddQuickEmoticon>(), ChannelEvent {
    /**
     * 事件发生的大别野房间
     */
    @JSTP
    abstract override suspend fun channel(): VillaRoom

    /**
     * 事件发生的大别野房间
     *
     * @see channel
     */
    @JSTP
    override suspend fun organization(): VillaRoom = channel()

    //region Delegates
    /**
     * 大别野 id
     *
     * @see AddQuickEmoticon.villaId
     */
    public val villaId: ID get() = sourceEventExtend.villaId.ID

    /**
     * 房间 id
     *
     * @see AddQuickEmoticon.roomId
     */
    public val roomId: ID get() = sourceEventExtend.roomId.ID

    /**
     * 发送表情的用户 id
     *
     * @see AddQuickEmoticon.uid
     */
    public val uid: ID get() = sourceEventExtend.uid.ID

    /**
     * 表情 id
     *
     * @see AddQuickEmoticon.emoticonId
     */
    public val emoticonId: ID get() = sourceEventExtend.emoticonId.ID

    /**
     * 表情内容
     *
     * @see AddQuickEmoticon.emoticon
     */
    public val emoticon: String get() = sourceEventExtend.emoticon

    /**
     * 被回复的消息 id
     *
     * @see AddQuickEmoticon.msgUid
     */
    public val msgUid: ID get() = sourceEventExtend.msgUid.ID

    /**
     * 是否是取消表情
     *
     * @see AddQuickEmoticon.isCancel
     */
    public val isCancel: Boolean get() = sourceEventExtend.isCancel

    /**
     * 如果被回复的消息从属于机器人，则该字段不为空字符串
     *
     * @see AddQuickEmoticon.botMsgId
     */
    public val botMsgId: ID get() = sourceEventExtend.botMsgId.ID

    /**
     * 表情类型
     *
     * @see AddQuickEmoticon.emoticonType
     */
    @get:JvmName("getEmoticonType")
    public val emoticonType: UInt get() = sourceEventExtend.emoticonType
    //endregion

    override val key: Event.Key<VillaAddQuickEmoticonEvent>
        get() = Key

    override fun toString(): String = "VillaAddQuickEmoticonEvent(sourceEvent=$sourceEvent)"

    public companion object Key : BaseEventKey<VillaAddQuickEmoticonEvent>(
        "villa.add_quick_emoticon", VillaEvent, ChannelEvent
    ) {
        override fun safeCast(value: Any): VillaAddQuickEmoticonEvent? = doSafeCast(value)
    }

}
