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

package love.forte.simbot.miyoushe.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

/**
 * 与 [http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html) 中描述的事件结构完全一致的结构体。
 *
 * @see Event
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class RawEvent(
    /**
     * 机器人相关信息
     */
    @ProtoNumber(1)
    val robot: Robot,
    /**
     * 事件类型
     *
     * 参考: [文档](https://webstatic.mihoyo.com/vila/bot/doc/callback.html##%E4%BA%8B%E4%BB%B6%E7%B1%BB%E5%9E%8B)
     */
    @ProtoNumber(2)
    val type: Int = 0,
    /**
     * 扩展数据，保存事件的具体信息。
     */
    @SerialName("extend_data")
    @ProtoNumber(3)
    val extendData: ExtendDataContainer,
    /**
     * 	事件创建时间的时间戳
     */
    @ProtoNumber(4)
    val createdAt: Long,
    /**
     * 事件 id
     */
    @ProtoNumber(5)
    val id: String,
    /**
     * 事件回调时间的时间戳
     */
    @ProtoNumber(6)
    val sendAt: Long
)

/**
 * EventData of [RawEvent.extendData]
 *
 * @see RawEvent
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
public data class ExtendDataContainer(
    @ProtoNumber(1) @SerialName("EventData") val eventData: Data
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
