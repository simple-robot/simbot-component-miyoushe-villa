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

@file:OptIn(ExperimentalSerializationApi::class)

package love.forte.simbot.miyoushe.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import kotlin.jvm.JvmName

/**
 * 事件结构体实现的统一父类。
 *
 */
public sealed class EventExtendData

/**
 * 由 [EventExtendData] 实现类的伴生对象实现，提供一些元数据信息。
 */
public abstract class EventExtendDataMeta {
    /**
     * 事件 `type` 值。
     */
    public abstract val type: Int

    /**
     * 事件名称
     */
    public abstract val name: String
}

/**
 * 有新用户加入大别野
 *
 * json
 *
 * ```json
 * {
 *   "join_uid": 0,            // uint64, 用户 id
 *   "join_user_nickname": 0,  // string, 用户昵称
 *   "join_at":  0,            // int64, 用户加入时间的时间戳
 *   "villa_id": 0             // 大别野 id
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 * message JoinVillaInfo {
 *     uint64 join_uid = 1; // 加入用户 uid
 *     string join_user_nickname = 2; // 加入用户昵称
 *     int64 join_at = 3; // 加入时间 [待废弃, 可以改用 RobotEvent.created_at]
 *     uint64 villa_id = 4;    // 大别野 id
 * }
 * ```
 *
 */
@Serializable
public data class JoinVilla(
    /**
     * `uint64`, 用户 id
     */
    @SerialName("join_uid")
    @get:JvmName("getJoinUid")
    @ProtoNumber(1)
    val joinUid: ULong,
    /**
     * `string`, 用户昵称
     */
    @SerialName("join_user_nickname")
    @ProtoNumber(2)
    val joinUserNickname: String,
    /**
     * `int64`, 用户加入时间的时间戳
     *
     * @see Event.createdAt
     */
    @SerialName("join_at")
    @ProtoNumber(3)
    val joinAt: Long = 0L,
    /**
     * `uint64`, 大别野 id
     */
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    @ProtoNumber(4)
    val villaId: ULong,
) : EventExtendData() {
    public val joinUidStrValue: String get() = joinUid.toString()
    public val villaIdStrValue: String get() = villaId.toString()

    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 1
        internal const val NAME: String = "JoinVilla"

        override val type: Int
            get() = TYPE

        override val name: String
            get() = NAME
    }
}

/**
 * 用户@机器人发送消息
 *
 * json
 *
 * ```json
 * {
 *   "content": "",      // 消息内容
 *   "from_user_id": 0,  // 发送者 id
 *   "send_at": 0,       // 发送时间的时间戳
 *   "room_id": 0,       // 房间 id
 *   "object_name": 1,   // 目前只支持文本类型消息
 *   "nickname": "",     // 用户昵称
 *   "msg_uid": "",      // 消息 id
 *   "bot_msg_id": "",   // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *   "villa_id": 0,      // 大别野 id
 *   "quote_msg": {      // 回调消息引用消息的基础信息
 *     "content": "",            // 消息摘要，如果是文本消息，则返回消息的文本内容。如果是图片消息，则返回"[图片]"
 *     "msg_uid": "",            // 消息 id
 *     "bot_msg_id": "",         // 如果消息从属于机器人，则该字段不为空字符串
 *     "send_at": 0,             // 发送时间的时间戳
 *     "msg_type": "",           // 消息类型，包括"文本"，"图片"，"帖子卡片"等
 *     "from_user_id": 0,        // 发送者 id（整型）
 *     "from_user_nickname": "", // 发送者昵称
 *     "from_user_id_str": "",   // 发送者 id（字符串）可携带机器人发送者的id
 *   }
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 *  message SendMessageInfo {
 *     string content = 1; // 消息内容
 *     uint64 from_user_id = 2; // 发送人 uid
 *     int64 send_at = 3; // 发送时间 [待废弃, 可以改用 RobotEvent.created_at]
 *     ObjectName object_name = 4; // 消息类型
 *     uint64 room_id = 5; // 房间 id
 *     string nickname = 6; // 昵称
 *     string msg_uid = 7; // 消息 id
 *     string bot_msg_id = 8;  // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *     uint64 villa_id = 9;    // 大别野 id
 *     QuoteMessageInfo quote_msg = 10; // 引用消息的内容
 * }
 * ```
 *
 */
@Serializable
public data class SendMessage(
    /**
     * 消息内容
     */
    @ProtoNumber(1)
    val content: String,
    /**
     * 发送者 id
     */
    @ProtoNumber(2)
    @SerialName("from_user_id")
    @get:JvmName("getFromUserId")
    val fromUserId: ULong,
    /**
     * 发送时间的时间戳
     *
     * @see Event.createdAt
     */
    @ProtoNumber(3)
    @SerialName("send_at")
    @get:JvmName("getSendAt")
    val sendAt: Long = 0L,
    /**
     * ```proto
     * enum ObjectName {
     *     UnknowObjectName = 0 ;
     *     Text = 1; // 文本消息
     *     Post = 2; // 帖子消息
     * }
     * ```
     *
     */
    @ProtoNumber(4)
    @SerialName("object_name")
    val objectName: Int = 0, // TODO enum?
    /**
     * 房间 id
     */
    @ProtoNumber(5)
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 用户昵称
     */
    @ProtoNumber(6)
    val nickname: String,
    /**
     * 消息 id
     */
    @ProtoNumber(7)
    @SerialName("msg_uid")
    val msgUid: String,
    /**
     * 如果被回复的消息从属于机器人，则该字段不为空字符串
     */
    @ProtoNumber(8)
    @SerialName("bot_msg_id")
    val botMsgId: String = "",
    /**
     * 大别野 id
     */
    @ProtoNumber(9)
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,

    /**
     * 引用消息的内容
     */
    @ProtoNumber(10)
    val quoteMsg: QuoteMessage? = null
) : EventExtendData() {
    public val fromUserIdStrValue: String get() = fromUserId.toString()
    public val roomIdStrValue: String get() = roomId.toString()
    public val villaIdStrValue: String get() = villaId.toString()


    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 2
        internal const val NAME: String = "SendMessage"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}


/**
 * 大别野添加机器人实例
 *
 * json
 *
 * ```json
 * {
 *   "villa_id": 0 // 大别野 id
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 * message CreateRobotInfo {
 *     uint64 villa_id = 1; // 创建机器人实例的大别野 id
 * }
 * ```
 *
 * @property villaId `uint64`, 大别野 id
 *
 */
@Serializable
public data class CreateRobot(
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    @ProtoNumber(1)
    val villaId: ULong
) : EventExtendData() {
    val villaIdStrValue: String get() = villaId.toString()

    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 3
        internal const val NAME: String = "CreateRobot"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}

/**
 * 大别野删除机器人实例
 *
 * json
 *
 * ```json
 * {
 *   "villa_id": 0 // 大别野 id
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 * message DeleteRobotInfo {
 *     uint64 villa_id = 1;
 * }
 * ```
 *
 * @property villaId `uint64`, 大别野 id
 *
 */
@Serializable
public data class DeleteRobot(
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    @ProtoNumber(1)
    val villaId: ULong
) : EventExtendData() {
    val villaIdStrValue: String get() = villaId.toString()

    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 4
        internal const val NAME: String = "DeleteRobot"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}

/**
 * 大别野用户对机器人消息做出快捷表情表态
 *
 * json
 *
 * ```json
 * {
 *   "villa_id": 0,    // 大别野 id
 *   "room_id": 0,     // 房间 id
 *   "uid": 0,         // 发送表情的用户 id
 *   "emoticon_id": 4, // 表情 id
 *   "emoticon": "支持",   // 表情内容
 *   "msg_uid": "C7ED-P5DJ-C4P8-MS0I",     // 被回复的消息 id
 *   "bot_msg_id": "85b4c7c7-9277-5d4c-0b29-5f5161e3862e",  // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *   "is_cancel": true // 是否是取消表情
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 * message AddQuickEmoticonInfo {
 *     uint64 villa_id = 1;    // 大别野 id
 *     uint64 room_id = 2;     // 房间 id
 *     uint64 uid = 3;         // 回复用户 id
 *     uint32 emoticon_id = 4; // 表情 id
 *     string emoticon = 5;    // 表情
 *     string msg_uid = 6;     // 消息 id
 *     bool is_cancel = 7;     // 是否是取消表情
 *     string bot_msg_id = 8;  // 如果被回复的消息从属于机器人，则该字段不为空字符串
 *     uint32 emoticon_type = 9; // 表情类型
 * }
 * ```
 *
 */
@Serializable
public data class AddQuickEmoticon(
    /**
     * 大别野 id
     */
    @ProtoNumber(1)
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
    /**
     * 房间 id
     */
    @ProtoNumber(2)
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 发送表情的用户 id
     */
    @ProtoNumber(3)
    @SerialName("uid")
    @get:JvmName("getUid")
    val uid: ULong,
    /**
     * 表情 id
     */
    @ProtoNumber(4)
    @SerialName("emoticon_id")
    @get:JvmName("getEmoticonId")
    val emoticonId: ULong,
    /**
     * 表情内容
     */
    @ProtoNumber(5)
    @SerialName("emoticon")
    val emoticon: String,
    /**
     * 被回复的消息 id
     */
    @ProtoNumber(6)
    @SerialName("msg_uid")
    val msgUid: String,
    /**
     * 是否是取消表情
     */
    @ProtoNumber(7)
    @SerialName("is_cancel")
    val isCancel: Boolean = false,
    /**
     * 如果被回复的消息从属于机器人，则该字段不为空字符串
     */
    @ProtoNumber(8)
    @SerialName("bot_msg_id")
    val botMsgId: String = "",

    /**
     * 表情类型
     */
    @ProtoNumber(9)
    @SerialName("emoticon_type")
    @get:JvmName("getEmoticonType")
    val emoticonType: UInt,
) : EventExtendData() {
    public val villaIdStrValue: String get() = villaId.toString()
    public val roomIdStrValue: String get() = roomId.toString()
    public val uidStrValue: String get() = uid.toString()
    public val emoticonIdStrValue: String get() = emoticonId.toString()
    public val emoticonTypeStrValue: String get() = emoticonType.toString()

    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 5
        internal const val NAME: String = "AddQuickEmoticon"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}

/**
 * 大别野用户对机器人消息做出快捷表情表态
 *
 * json
 *
 * ```json
 * {
 *   "audit_id": "111",    // 审核事件 id
 *   "bot_tpl_id": "bot_ea9d3981djcnryxxxxxx", // 机器人 id
 *   "villa_id":100001,    // 大别野 id
 *   "room_id":950003,     // 房间 id（和审核接口调用方传入的值一致）
 *   "user_id": 0,         // 用户 id（和审核接口调用方传入的值一致）
 *   "pass_through": "",   // 透传数据（和审核接口调用方传入的值一致）
 *   "audit_result": 1     // 审核结果，0作兼容，1审核通过，2审核驳回
 * }
 * ```
 *
 * proto
 *
 * ```proto3
 * message AuditCallbackInfo {
 *     enum AuditResult {
 *         None = 0;
 *         Pass = 1;
 *         Reject = 2;
 *     }
 *
 *     string audit_id = 1;   // 机器人平台审核事件 id
 *     string bot_tpl_id = 2; // 机器人 id
 *     uint64 villa_id = 3;   // 大别野 id
 *     uint64 room_id = 4;    // 房间 id
 *     uint64 user_id = 5;    // 用户 id
 *     string pass_through = 6; // 透传字段
 *     AuditResult audit_result = 7; // 审核结果
 * }
 * ```
 *
 */
@Serializable
public data class AuditCallback(
    /**
     * 审核事件 id
     */
    @ProtoNumber(1)
    @SerialName("audit_id")
    val auditId: String,
    /**
     * 机器人 id
     */
    @ProtoNumber(2)
    @SerialName("bot_tpl_id")
    val botTplId: String,
    /**
     * 大别野 id
     */
    @ProtoNumber(3)
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
    /**
     * 房间 id（和审核接口调用方传入的值一致）
     */
    @ProtoNumber(4)
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 用户 id（和审核接口调用方传入的值一致）
     */
    @ProtoNumber(5)
    @SerialName("user_id")
    @get:JvmName("getUserId")
    val userId: ULong,
    /**
     * 透传数据（和审核接口调用方传入的值一致）
     */
    @ProtoNumber(6)
    @SerialName("pass_through")
    val passThrough: String = "",
    /**
     * 审核结果，0作兼容，1审核通过，2审核驳回
     */
    @ProtoNumber(7)
    @SerialName("audit_result")
    val auditResult: Int = 0,
) : EventExtendData() {
    public val villaIdStrValue: String get() = villaId.toString()
    public val roomIdStrValue: String get() = roomId.toString()
    public val userIdStrValue: String get() = userId.toString()

    public val isAuditResultPass: Boolean get() = auditResult == AUDIT_RESULT_PASS
    public val isAuditResultReject: Boolean get() = auditResult == AUDIT_RESULT_REJECT

    public companion object Meta : EventExtendDataMeta() {

        public const val AUDIT_RESULT_NONE: Int = 0
        public const val AUDIT_RESULT_PASS: Int = 1
        public const val AUDIT_RESULT_REJECT: Int = 2

        private const val TYPE: Int = 6
        internal const val NAME: String = "AuditCallback"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}

/**
 * 点击消息组件回传
 *
 * ```json
 * {
 *   "villa_id": 0,    // 大别野 id
 *   "room_id": 0,     // 房间 id
 *   "uid": 0,         // 用户 id
 *   "msg_uid": "C7ED-P5DJ-C4P8-MS0I",     // 消息 id
 *   "bot_msg_id": "85b4c7c7-9277-5d4c-0b29-5f5161e3862e",  // 如果消息从属于机器人，则该字段不为空字符串
 *   "component_id": "",    // 机器人自定义的组件id
 *   "template_id": "",  // 如果该组件模板为已创建模板，则template_id不为0
 *   "extra": ""    // 机器人自定义透传信息
 * }
 * ```
 *
 * ```proto3
 * message ClickMsgComponentInfo {
 *             uint64 villa_id = 1;
 *             uint64 room_id = 2;
 *             string component_id = 3;    // 自定义组件id
 *             string msg_uid = 4;
 *             uint64 uid = 5;
 *             string bot_msg_id = 6;
 *             uint64 template_id = 7; // 模板id
 *             string extra = 8;  // 机器人自定义透传数据
 *         }
 * ```
 *
 * @property villaId 大别野 id
 * @property roomId 房间 id
 * @property uid 用户 id
 * @property msgUid 消息 id
 * @property botMsgId 如果消息从属于机器人，则该字段不为空字符串
 * @property componentId 机器人自定义的组件id
 * @property templateId 如果该组件模板为已创建模板，则template_id不为0
 * @property extra 机器人自定义透传信息
 *
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ClickMsgComponent(
    @ProtoNumber(1) @SerialName("villa_id") @get:JvmName("getVillaId") val villaId: UInt,
    @ProtoNumber(2) @SerialName("room_id") @get:JvmName("getRoomId") val roomId: UInt,
    @ProtoNumber(3) @SerialName("component_id") val componentId: String,
    @ProtoNumber(4) @SerialName("msg_uid") val msgUid: String,
    @ProtoNumber(5) @get:JvmName("getUid") val uid: ULong,
    @ProtoNumber(6) @SerialName("bot_msg_id") val botMsgId: String,
    @ProtoNumber(7) @SerialName("template_id") @get:JvmName("getTemplateId") val templateId: ULong,
    @ProtoNumber(8) @SerialName("extra") val extra: String,
) : EventExtendData() {
    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 7
        internal const val NAME: String = "ClickMsgComponent"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }

    val villaIdStrValue: String get() = villaId.toString()
    val roomIdStrValue: String get() = roomId.toString()
    val uidStrValue: String get() = uid.toString()
    val templateIdStrValue: String get() = templateId.toString()
}

