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
import kotlin.js.JsExport
import kotlin.jvm.JvmName

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
 * proto 定义：
 *
 * ```proto3
 *
 * message RobotEvent {
 *     enum EventType {
 *         UnknowRobotEventType = 0;
 *         JoinVilla = 1; // 加入大别野
 *         SendMessage = 2; // 发送消息
 *         CreateRobot = 3; // 大别野添加机器人实例
 *         DeleteRobot = 4; // 大别野删除机器人实例
 *         AddQuickEmoticon = 5; // 表情表态
 *         AuditCallback = 6; // 审核结果回调
 *         ClickMsgComponent = 7; // 点击消息组件
 *     }
 *
 *     message ExtendData {
 *         message JoinVillaInfo {
 *             uint64 join_uid = 1; // 加入用户 uid
 *             string join_user_nickname = 2; // 加入用户昵称
 *             int64 join_at = 3; // 加入时间 [待废弃, 可以改用 RobotEvent.created_at]
 *             uint64 villa_id = 4;    // 大别野 id
 *         }
 *
 *         message SendMessageInfo {
 *             string content = 1; // 消息内容
 *             uint64 from_user_id = 2; // 发送人 uid
 *             int64 send_at = 3; // 发送时间 [待废弃, 可以改用 RobotEvent.created_at]
 *             ObjectName object_name = 4; // 消息类型
 *             uint64 room_id = 5; // 房间 id
 *             string nickname = 6; // 昵称
 *             string msg_uid = 7; // 消息 id
 *             string bot_msg_id = 8;  // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *             uint64 villa_id = 9;    // 大别野 id
 *             QuoteMessageInfo quote_msg = 10; // 引用消息的内容
 *         }
 *
 *         message CreateRobotInfo {
 *             uint64 villa_id = 1; // 创建机器人实例的大别野 id
 *         }
 *
 *
 *         message DeleteRobotInfo {
 *             uint64 villa_id = 1;
 *         }
 *
 *         message AddQuickEmoticonInfo {
 *             uint64 villa_id = 1;    // 大别野 id
 *             uint64 room_id = 2;     // 房间 id
 *             uint64 uid = 3;         // 回复用户 id
 *             uint32 emoticon_id = 4; // 表情 id
 *             string emoticon = 5;    // 表情
 *             string msg_uid = 6;     // 消息 id
 *             bool is_cancel = 7;     // 是否是取消表情
 *             string bot_msg_id = 8;  // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *             uint32 emoticon_type = 9; // 表情类型
 *         }
 *
 *         message AuditCallbackInfo {
 *             enum AuditResult {
 *                 None = 0;
 *                 Pass = 1;
 *                 Reject = 2;
 *             }
 *
 *             string audit_id = 1;   // 机器人平台审核事件 id
 *             string bot_tpl_id = 2; // 机器人 id
 *             uint64 villa_id = 3;   // 大别野 id
 *             uint64 room_id = 4;    // 房间 id
 *             uint64 user_id = 5;    // 用户 id
 *             string pass_through = 6; // 透传字段
 *             AuditResult audit_result = 7; // 审核结果
 *         }
 *
 *         message ClickMsgComponentInfo {
 *             uint64 villa_id = 1;
 *             uint64 room_id = 2;
 *             string component_id = 3;    // 自定义组件id
 *             string msg_uid = 4;
 *             uint64 uid = 5;
 *             string bot_msg_id = 6;
 *             uint64 template_id = 7; // 模板id
 *             string extra = 8;  // 机器人自定义透传数据
 *         }
 *
 *         oneof event_data {
 *             JoinVillaInfo join_villa = 1; // 加入大别野扩展信息
 *             SendMessageInfo send_message = 2; // 发送消息扩展信息
 *             CreateRobotInfo create_robot = 3; // 添加机器人扩展信息
 *             DeleteRobotInfo delete_robot = 4; // 删除机器人扩展信息
 *             AddQuickEmoticonInfo add_quick_emoticon = 5;   // 表情表态扩展信息
 *             AuditCallbackInfo audit_callback = 6; // 审核回调信息
 *             ClickMsgComponentInfo click_msg_component = 7;  // 点击消息组件回传
 *         }
 *     }
 *
 *     Robot robot = 1; // 事件相关机器人
 *     EventType type = 2; // 事件类型
 *     ExtendData extend_data = 3; // 事件拓展信息
 *     int64 created_at = 4; // 事件发生时间
 *     string id = 5; // 事件 id
 *     int64 send_at = 6; // 事件消息投递时间
 * }
 * ```
 *
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
     *
     */
    @SerialName("extend_data")
    val extendData: E,

    /**
     * 	事件创建时间的时间戳
     */
    @SerialName("created_at")
    val createdAt: Long,

    /**
     * 事件 id
     */
    val id: String,

    /**
     * 事件回调时间的时间戳
     */
    @SerialName("send_at")
    val sendAt: Long
)

/**
 * 机器人相关信息
 *
 * ```proto3
 * message Robot {
 *     RobotTemplate template = 1; // 所属机器人模板
 *     uint64 villa_id = 2; // 大别野 id
 * }
 * ```
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Robot(
    /**
     * 机器人模板信息
     */
    @ProtoNumber(1)
    val template: Template,

    /**
     * 事件所属的大别野 id
     */
    @SerialName("villa_id")
    @ProtoNumber(2)
    @get:JvmName("getVillaId")
    val villaId: ULong
) {
    public val villaIdStrValue: String get() = villaId.toString()


    /**
     * 机器人模板信息
     *
     * ```proto3
     * message RobotTemplate {
     *     message Param {
     *         string desc = 1; // 参数描述
     *     }
     *
     *     message Command {
     *         string name = 1; // 指令名称
     *         string desc = 2; // 指令描述
     *         repeated Param params = 3; // 指令参数
     *     }
     *     message CustomSetting {
     *         string name = 1; // 自定义设置项名称
     *         string url = 2; // 自定义设置项页面
     *     }
     *
     *     string id = 1; // 机器人模板 id
     *     string name = 2; // 机器人名称
     *     string desc = 3; // 机器人描述
     *     string icon = 4; // 机器人 icon
     *     repeated Command commands = 5; // 指令列表
     *     repeated CustomSetting custom_settings = 6; // 自定义设置项
     *     bool is_allowed_add_to_other_villa = 7; // 是否允许添加到其他大别野
     * }
     * ```
     */
    @Serializable
    public data class Template(
        /**
         * 机器人模板 id
         */
        @ProtoNumber(1)
        val id: String,
        /**
         * 机器人名称
         */
        @ProtoNumber(2)
        val name: String,
        /**
         * 机器人描述
         */
        @ProtoNumber(3)
        val desc: String,
        /**
         * 机器人 icon
         */
        @ProtoNumber(4)
        val icon: String,
        /**
         * 指令列表
         */
        @ProtoNumber(5)
        val commands: List<TemplateCommand> = emptyList(),
        /**
         * 自定义设置项
         */
        @ProtoNumber(6)
        val customSettings: List<CustomSetting> = emptyList(),
        /**
         * 是否允许添加到其他大别野
         */
        @ProtoNumber(7)
        @SerialName("is_allowed_add_to_other_villa")
        val isAllowedAddToOtherVilla: Boolean = false,
    )

    /**
     * 机器人模板指令
     *
     * @property name 指令名称
     * @property desc 指令描述
     * @property params 指令参数
     *
     */
    @Serializable
    public data class TemplateCommand(
        @ProtoNumber(1) val name: String,
        @ProtoNumber(2) val desc: String,
        @ProtoNumber(3) val params: List<TemplateCommandParam> = emptyList(),
    )

    /**
     * 机器人模板指令参数
     *
     * @property desc 参数描述
     */
    @Serializable
    public data class TemplateCommandParam(@ProtoNumber(1) val desc: String)

    /**
     * 自定义设置项
     *
     * @property name 自定义设置项名称
     * @property url 自定义设置项页面
     */
    @Serializable
    public data class CustomSetting(
        @ProtoNumber(1) val name: String,
        @ProtoNumber(2) val url: String
    )

}


/*

 */

/**
 * 引用消息内容
 *
 * proto
 *
 * ```proto3
 * message QuoteMessageInfo {
 *     string content = 1; // 消息内容
 *     string msg_uid = 2; // 消息msg_uid
 *     int64 send_at = 3;
 *     string msg_type = 4; // 消息类型
 *     string bot_msg_id = 5;  // 如果是机器人消息，则该字段不为空字符串
 *     uint64 from_user_id = 6;  // 发送者id
 *     string from_user_id_str = 7;  // 发送者id（string）
 *     string from_user_nickname = 8; // 发送者昵称
 * }
 * ```
 *
 * @property content 消息内容
 * @property msgUid 消息msg_uid
 * @property sendAt send at
 * @property msgType 消息类型
 * @property botMsgId 如果是机器人消息，则该字段不为空字符串
 * @property fromUserId 发送者id
 * @property fromUserIdStr 发送者id（string）
 * @property fromUserNickname 发送者昵称
 * @property images 消息中的图片url数组，支持图文消息、图片消息、自定义表情、avatar互动等消息类型
 * （2023.12.20 更新）
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class QuoteMessage(
    @ProtoNumber(1)
    val content: String,
    @ProtoNumber(2)
    @SerialName("msg_uid")
    val msgUid: String,
    @ProtoNumber(3)
    @SerialName("send_at")
    val sendAt: Long,
    @ProtoNumber(4)
    @SerialName("msg_type")
    val msgType: String,
    @ProtoNumber(5)
    @SerialName("bot_msg_id")
    val botMsgId: String,
    @ProtoNumber(6)
    @SerialName("from_user_id")
    @get:JvmName("getFromUserId")
    val fromUserId: ULong,
    @ProtoNumber(7)
    @SerialName("from_user_id_str")
    val fromUserIdStr: String,
    @ProtoNumber(8)
    @SerialName("from_user_nickname")
    val fromUserNickname: String,
    @ProtoNumber(9)
    val images: List<String> = emptyList()
) {
    val fromUserIdStrValue: String get() = fromUserId.toString()
}
