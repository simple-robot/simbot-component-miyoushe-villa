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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 与 [http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html) 中描述的事件结构完全一致的结构体。
 */
@Serializable
public data class RawEvent(

    /**
     * 事件 id
     */
    val id: String,
    /**
     * 机器人相关信息
     */
    val robot: Robot,
    /**
     * 事件类型
     *
     * 参考: [文档](https://webstatic.mihoyo.com/vila/bot/doc/callback.html##%E4%BA%8B%E4%BB%B6%E7%B1%BB%E5%9E%8B)
     */
    val type: Int,

    /**
     * 扩展数据，保存事件的具体信息。
     */
    @SerialName("extend_data")
    val extendData: ExtendDataContainer,

    /**
     * 	事件创建时间的时间戳
     */
    val createdAt: Long,

    /**
     * 事件回调时间的时间戳
     */
    val sendAt: Long
)

@Serializable
public data class ExtendDataContainer(@SerialName("EventData") val eventData: Data) {

    @Serializable
    public data class Data(
        @SerialName(JoinVilla.NAME) val joinVilla: JoinVilla? = null,
        @SerialName(SendMessage.NAME) val sendMessage: SendMessage? = null,
        @SerialName(CreateRobot.NAME) val createRobot: CreateRobot? = null,
        @SerialName(DeleteRobot.NAME) val deleteRobot: DeleteRobot? = null,
        @SerialName(AddQuickEmoticon.NAME) val addQuickEmoticon: AddQuickEmoticon? = null,
        @SerialName(AuditCallback.NAME) val auditCallback: AuditCallback? = null,
    )
}
