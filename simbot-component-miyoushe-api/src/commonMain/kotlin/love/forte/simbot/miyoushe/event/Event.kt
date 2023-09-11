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
 *
 * 机器人事件结构体。
 *
 * 请求数据结构示例:
 *
 * ```json
 * {
 *     "event": {
 *         "robot": {  // 机器人相关信息
 *             "template": {   // 机器人模板信息
 *                 "id": "bot_ea9d3xxxxxx9",
 *                 "name": "测试",
 *                 "desc": "测试机器人",
 *                 "icon": "",
 *                 "commands": [
 *                     {
 *                         "name": "/重置",
 *                         "desc": "重置会话"
 *                     }
 *                 ]
 *             },
 *             "villa_id": 100001 // 事件所属的大别野 id
 *         },
 *         "type": 2,  // 事件类型
 *         "extend_data": {  // 包含事件的具体数据
 *             "EventData": {
 *                 "SendMessage": {  // 不同类型事件有不同的回调数据
 *                     "content": "",
 *                     "from_user_id": 3300034,
 *                     "send_at": 1683275781450,
 *                     "object_name": 1,
 *                     "room_id": 95333,
 *                     "nickname": "kdodjcoclss",
 *                     "msg_uid": "C7TC-71AI-KDN8-MS0I"
 *                 }
 *             }
 *         },
 *         "created_at": 1683275781450,  // 事件创建事件
 *         "id": "8ee4c10d-8354-18d7-84df-7e02f034cfd1",
 *         "send_at": 1683275782   // 回调事件
 *     }
 * }
 * ```
 *
 * 然而 [Event] 类型的类型结构与上述结构有些许不同：`extend_data` （[extendData]）会跳过其下的 `"EventData"` 和 `"SendMessage"` (或其他对应的事件Key)
 * 而直接使用一个具体的 [EventExtendData] 类型结果。
 *
 * 因此 [Event] 类型并不能**直接**作为事件的反序列化目标（实际上从它有一个泛型开始就不可以了）。
 * 如果你希望使用一个与示例JSON的结构完全匹配的类型，参考 [RawEvent]。
 *
 * 更多参考：[http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html)
 *
 * @author ForteScarlet
 */
public data class Event<out E : EventExtendData>(
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
     *
     * [Event.extendData] 与示例中的JSON结构有所不同：[extendData] 数据结构跳过了 `"EventData"` 和其中的子事件类型Key（比如说 `"SendMessage"`），
     * 取而代之的则是直接的事件体。
     */
    @SerialName("extend_data")
    val extendData: E,

    /**
     * 	事件创建时间的时间戳
     */
    @SerialName("created_at")
    val createdAt: Long,

    /**
     * 事件回调时间的时间戳
     */
    @SerialName("send_at")
    val sendAt: Long
)

/**
 * 机器人相关信息
 */
@Serializable
public data class Robot(
    /**
     * 机器人模板信息
     */
    val template: Template,

    /**
     * 事件所属的大别野 id
     */
    @SerialName("villa_id")
    val villaId: ULong
) {
    /**
     * 机器人模板信息
     */
    @Serializable
    public data class Template(
        val id: String,
        val name: String,
        val desc: String,
        val icon: String,
        val commands: List<TemplateCommand>
    )

    /**
     * 机器人模板指令
     */
    @Serializable
    public data class TemplateCommand(val name: String, val desc: String)
}
