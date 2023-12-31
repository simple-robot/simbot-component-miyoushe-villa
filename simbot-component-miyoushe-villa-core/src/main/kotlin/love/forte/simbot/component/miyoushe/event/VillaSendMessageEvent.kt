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
import love.forte.simbot.JST
import love.forte.simbot.JSTP
import love.forte.simbot.component.miyoushe.VillaMember
import love.forte.simbot.component.miyoushe.VillaRoom
import love.forte.simbot.component.miyoushe.message.VillaReceivedMessageContent
import love.forte.simbot.component.miyoushe.message.VillaSendMessageReceipt
import love.forte.simbot.event.BaseEventKey
import love.forte.simbot.event.ChannelMessageEvent
import love.forte.simbot.event.Event
import love.forte.simbot.event.MessageEvent
import love.forte.simbot.message.Message
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.miyoushe.event.SendMessage


/**
 * 用户@机器人发送消息事件
 *
 * @see SendMessage
 *
 * @author ForteScarlet
 */
public abstract class VillaSendMessageEvent : VillaEvent<SendMessage>(), ChannelMessageEvent {

    //region delegates
    /**
     * 发送者 id
     *
     * @see SendMessage.fromUserId
     */
    public val fromUserId: ID get() = sourceEventExtend.fromUserId.ID

    /**
     * 房间 id
     *
     * @see SendMessage.roomId
     */
    public val roomId: ID get() = sourceEventExtend.roomId.ID

    /**
     * 用户昵称
     *
     * @see SendMessage.nickname
     */
    public val nickname: String get() = sourceEventExtend.nickname

    /**
     * 消息 id
     *
     * @see SendMessage.msgUid
     */
    public val msgUid: ID get() = sourceEventExtend.msgUid.ID

    /**
     * 如果被回复的消息从属于机器人，则该字段不为 `null`。
     *
     * @see SendMessage.botMsgId
     */
    public val botMsgId: ID? get() = sourceEventExtend.botMsgId.takeIf { it.isNotEmpty() }?.ID

    /**
     * 大别野 id
     *
     * @see SendMessage.villaId
     */
    public val villaId: ID get() = sourceEventExtend.villaId.ID

    //endregion

    /**
     * 消息内容本体
     */
    abstract override val messageContent: VillaReceivedMessageContent

    /**
     * 消息的发送者
     */
    @JSTP
    abstract override suspend fun author(): VillaMember

    /**
     * 回复此消息。会自动附加针对当前消息的引用回复
     */
    @JST
    abstract override suspend fun reply(message: Message): VillaSendMessageReceipt

    /**
     * 回复此消息。会自动附加针对当前消息的引用回复
     */
    @JST
    abstract override suspend fun reply(message: MessageContent): VillaSendMessageReceipt

    /**
     * 回复此消息。会自动附加针对当前消息的引用回复
     */
    @JST
    abstract override suspend fun reply(text: String): VillaSendMessageReceipt

    /**
     * 事件发生的子频道（大别野房间）
     */
    @JSTP
    abstract override suspend fun channel(): VillaRoom

    /**
     * 事件发生的子频道（大别野房间）
     */
    @JSTP
    override suspend fun source(): VillaRoom = channel()

    /**
     * 事件发生的子频道（大别野房间）
     */
    @JSTP
    override suspend fun organization(): VillaRoom = channel()

    override val key: Event.Key<VillaSendMessageEvent>
        get() = Key

    public companion object Key : BaseEventKey<VillaSendMessageEvent>(
        "villa.send_message", VillaEvent, MessageEvent
    ) {
        override fun safeCast(value: Any): VillaSendMessageEvent? = doSafeCast(value)
    }

}
