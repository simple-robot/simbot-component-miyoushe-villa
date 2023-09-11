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
 * 更多参考：[http 事件回调](https://webstatic.mihoyo.com/vila/bot/doc/callback.html)
 *
 * @author ForteScarlet
 */
public class Event {
    // TODO event
}
