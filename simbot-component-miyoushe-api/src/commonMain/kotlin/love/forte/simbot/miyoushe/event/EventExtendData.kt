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
import kotlin.jvm.JvmName


/**
 * 包含事件的具体数据
 * @author ForteScarlet
 */
public abstract class EventExtendData

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
 */
@Serializable
public data class JoinVilla(
    /**
     * `uint64`, 用户 id
     */
    @SerialName("join_uid")
    @get:JvmName("getJoinUid")
    val joinUid: ULong,
    /**
     * `string`, 用户昵称
     */
    @SerialName("join_user_nickname")
    val joinUserNickname: String,
    /**
     * `int64`, 用户加入时间的时间戳
     */
    @SerialName("join_at")
    val joinAt: Long,
    /**
     * `uint64`, 大别野 id
     */
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
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
 */
@Serializable
public data class SendMessage(
    /**
     * 消息内容
     */
    val content: String,
    /**
     * 发送者 id
     */
    @SerialName("from_user_id")
    @get:JvmName("getFromUserId")
    val fromUserId: ULong,
    /**
     * 发送时间的时间戳
     */
    @SerialName("send_at")
    @get:JvmName("getSendAt")
    val sendAt: Long,
    /**
     * 房间 id
     */
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 目前只支持文本类型消息
     */
    @SerialName("object_name")
    val objectName: Int, // TODO ?
    /**
     * 用户昵称
     */
    val nickname: String,
    /**
     * 消息 id
     */
    @SerialName("msg_uid")
    val msgUid: String,
    /**
     * 如果被回复的消息从属于机器人，则该字段不为空字符串
     */
    @SerialName("bot_msg_id")
    val botMsgId: String,
    /**
     * 大别野 id
     */
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
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
 */
@Serializable
public data class CreateRobot(@SerialName("villa_id") @get:JvmName("getVillaId") val villaId: ULong) :
    EventExtendData() {
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
 */
@Serializable
public data class DeleteRobot(@SerialName("villa_id") @get:JvmName("getVillaId") val villaId: ULong) :
    EventExtendData() {
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
 */
@Serializable
public data class AddQuickEmoticon(
    /**
     * 大别野 id
     */
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
    /**
     * 房间 id
     */
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 发送表情的用户 id
     */
    @SerialName("uid")
    @get:JvmName("getUid")
    val uid: ULong,
    /**
     * 表情 id
     */
    @SerialName("emoticon_id")
    @get:JvmName("getEmoticonId")
    val emoticonId: ULong,
    /**
     * 表情内容
     */
    @SerialName("emoticon")
    val emoticon: String,
    /**
     * 被回复的消息 id
     */
    @SerialName("msg_uid")
    val msgUid: String,
    /**
     * 如果被回复的消息从属于机器人，则该字段不为空字符串
     */
    @SerialName("bot_msg_id")
    val botMsgId: String,
    /**
     * 是否是取消表情
     */
    @SerialName("is_cancel")
    val isCancel: Boolean,
) : EventExtendData() {
    public val villaIdStrValue: String get() = villaId.toString()
    public val roomIdStrValue: String get() = roomId.toString()
    public val uidStrValue: String get() = uid.toString()
    public val emoticonIdStrValue: String get() = emoticonId.toString()

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
 */
@Serializable
public data class AuditCallback(
    /**
     * 审核事件 id
     */
    @SerialName("audit_id")
    val auditId: String,
    /**
     * 机器人 id
     */
    @SerialName("bot_tpl_id")
    val botTplId: String,
    /**
     * 大别野 id
     */
    @SerialName("villa_id")
    @get:JvmName("getVillaId")
    val villaId: ULong,
    /**
     * 房间 id（和审核接口调用方传入的值一致）
     */
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    val roomId: UInt,
    /**
     * 用户 id（和审核接口调用方传入的值一致）
     */
    @SerialName("user_id")
    @get:JvmName("getUserId")
    val userId: ULong,
    /**
     * 透传数据（和审核接口调用方传入的值一致）
     */
    @SerialName("pass_through")
    val passThrough: String,
    /**
     * 审核结果，0作兼容，1审核通过，2审核驳回
     */
    @SerialName("audit_result")
    val auditResult: Int,
) : EventExtendData() {
    public val villaIdStrValue: String get() = villaId.toString()
    public val roomIdStrValue: String get() = roomId.toString()
    public val userIdStrValue: String get() = userId.toString()

    public companion object Meta : EventExtendDataMeta() {
        private const val TYPE: Int = 6
        internal const val NAME: String = "AuditCallback"

        override val type: Int
            get() = TYPE
        override val name: String
            get() = NAME
    }
}
