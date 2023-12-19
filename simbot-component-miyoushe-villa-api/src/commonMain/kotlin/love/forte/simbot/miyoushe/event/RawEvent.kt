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

@file:Suppress("NON_EXPORTABLE_TYPE")
@file:JsExport

package love.forte.simbot.miyoushe.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import love.forte.simbot.miyoushe.ws.ProtoPacketDefinition
import kotlin.js.JsExport


/**
 * [RawEvent] 与 [PRawEvent] 的共用父类，
 * 定义它们之间一致的字段，并将 `extendData` 泛型化处理。
 *
 * @param ED [ExtendDataContainer] 或 [ExtendDataContainer.Data].
 *
 * @see RawEvent
 * @see PRawEvent
 */
public sealed class SerializableRawEvent<ED> {
    /**
     * 机器人相关信息
     */
    public abstract val robot: Robot

    /**
     * 事件类型
     *
     * 参考: [文档](https://webstatic.mihoyo.com/vila/bot/doc/callback.html##%E4%BA%8B%E4%BB%B6%E7%B1%BB%E5%9E%8B)
     */
    public abstract val type: Int

    /**
     * 扩展数据，保存事件的具体信息。
     * 可能为 [ExtendDataContainer] 或 [ExtendDataContainer.Data]
     */
    public abstract val extendData: ED

    /**
     * 事件创建时间的时间戳
     */
    public abstract val createdAt: Long

    /**
     * 事件 id
     */
    public abstract val id: String

    /**
     * 事件回调时间的时间戳
     */
    public abstract val sendAt: Long
}

/**
 * 与 [http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html)
 * 中描述的事件结构完全一致的结构体，用于 JSON 反序列化。
 *
 * @see SerializableRawEvent
 * @see Event
 */
@Serializable
public data class RawEvent(
    override val robot: Robot,
    override val type: Int = 0,
    /**
     * 扩展数据，保存事件的具体信息。
     *
     * @see ExtendDataContainer
     */
    @SerialName("extend_data")
    override val extendData: ExtendDataContainer,
    override val createdAt: Long,
    override val id: String,
    override val sendAt: Long
) : SerializableRawEvent<ExtendDataContainer>()

/**
 * 与 [http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html)
 * 中描述的事件结构基本一致的结构体，用于 Protobuf 反序列化。
 *
 * @see SerializableRawEvent
 * @see Event
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class PRawEvent(
    @ProtoNumber(1)
    override val robot: Robot,
    @ProtoNumber(2)
    override val type: Int = 0,
    /**
     * 扩展数据，保存事件的具体信息。
     *
     * @see ExtendDataContainer.Data
     */
    @ProtoNumber(3)
    override val extendData: ExtendDataContainer.Data,
    @ProtoNumber(4)
    override val createdAt: Long,
    @ProtoNumber(5)
    override val id: String,
    @ProtoNumber(6)
    override val sendAt: Long
) : SerializableRawEvent<ExtendDataContainer.Data>()

/**
 * 使用 [ExtendDataContainer.Data.oneOfValue] 作为 [Event.extendData]，将 [PRawEvent] 转化为 [Event]。
 *
 * @throws IllegalStateException 如果 [ExtendDataContainer.Data.oneOfValue] 无法获取任意事件结果
 */
public fun SerializableRawEvent<*>.toEvent(): Event<*> {
    return when (this) {
        is RawEvent -> Event(robot, type, extendData.eventData.oneOfValue, createdAt, id, sendAt)
        is PRawEvent -> Event(robot, type, extendData.oneOfValue, createdAt, id, sendAt)
    }
}

/**
 * EventData of [RawEvent.extendData]
 *
 * @see RawEvent
 */
@Serializable
public data class ExtendDataContainer(
    @SerialName("EventData") val eventData: Data,
) {
    /**
     * [ExtendData.EventData][ExtendDataContainer.eventData]
     *
     * @property joinVilla 加入大别野扩展信息
     * @property sendMessage 发送消息扩展信息
     * @property createRobot 添加机器人扩展信息
     * @property deleteRobot 删除机器人扩展信息
     * @property addQuickEmoticon 表情表态扩展信息
     * @property auditCallback 审核回调信息
     * @property clickMsgComponent 点击消息组件回传
     */
    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    public data class Data(
        @ProtoNumber(1) @SerialName(JoinVilla.NAME) val joinVilla: JoinVilla? = null,
        @ProtoNumber(2) @SerialName(SendMessage.NAME) val sendMessage: SendMessage? = null,
        @ProtoNumber(3) @SerialName(CreateRobot.NAME) val createRobot: CreateRobot? = null,
        @ProtoNumber(4) @SerialName(DeleteRobot.NAME) val deleteRobot: DeleteRobot? = null,
        @ProtoNumber(5) @SerialName(AddQuickEmoticon.NAME) val addQuickEmoticon: AddQuickEmoticon? = null,
        @ProtoNumber(6) @SerialName(AuditCallback.NAME) val auditCallback: AuditCallback? = null,
        @ProtoNumber(7) @SerialName(ClickMsgComponent.NAME) val clickMsgComponent: ClickMsgComponent? = null,
    ) {
        val oneOfValue: EventExtendData
            get() {
                return joinVilla
                    ?: sendMessage
                    ?: createRobot
                    ?: deleteRobot
                    ?: addQuickEmoticon
                    ?: auditCallback
                    ?: clickMsgComponent
                    ?: error("Non-null not found")
            }

    }
}

/**
 * 用于描述事件传递进来时的原始状态的类型。
 * 它由分别代表字符串（JSON）和代表二进制包（Protobuf）的类型实现。
 *
 * @see StringEventSource
 * @see ProtoPacketEventSource
 *
 */
public sealed class EventSource {
    /**
     * 代表原始值的类型。不会为 `null`。
     */
    public abstract val source: Any
}

/**
 * 以JSON字符串作为事件源的 [EventSource] 实现。
 *
 */
public data class StringEventSource(override val source: String) : EventSource()

/**
 * 以二进制数据包（protobuf, [WS 协议包数据](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html)）
 * 作为事件源的 [EventSource] 实现。
 *
 * 其中，事件本体的二进制数据为 [ProtoPacketDefinition.bodyData].
 *
 * @see ProtoPacketDefinition
 */
public data class ProtoPacketEventSource(override val source: ProtoPacketDefinition) : EventSource()
