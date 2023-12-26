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

package love.forte.simbot.miyoushe.api.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

/**
 * 与房间相关的基础信息。
 *
 * @see Room
 * @see ListRoom
 */
public interface RoomBasic {
    /**
     * 房间 id
     */
    @get:JvmSynthetic
    public val roomId: ULong

    /**
     * See [RoomType]: [房间名称](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E7%B1%BB%E5%9E%8B).
     */
    public val roomName: String

    /**
     * 房间类型
     */
    public val roomTypeValue: String

    /**
     * 分组 id
     */
    @get:JvmSynthetic
    public val groupId: ULong

    /**
     * String value of [roomId]
     */
    public val roomIdStrValue: String get() = roomId.toString()

    /**
     * String value of [groupIdStrValue]
     */
    public val groupIdStrValue: String get() = groupId.toString()

    /**
     * 通过 [roomTypeValue] 转化为 [RoomType]。
     * @throws NoSuchElementException 如果没有匹配结果
     */
    public val roomType: RoomType
        get() = RoomType.valueOf(roomTypeValue)
}

/**
 * [房间 api](https://webstatic.mihoyo.com/vila/bot/doc/room_api/)
 *
 * Room 房间信息
 *
 * @property roomId 房间 id
 * @property roomName See [RoomType]: [房间名称](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E7%B1%BB%E5%9E%8B).
 * @property roomTypeValue 房间类型
 * @property groupId 分组 id
 * @property roomDefaultNotifyTypeValue See [RoomDefaultNotifyType]: [房间默认通知类型](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E9%BB%98%E8%AE%A4%E9%80%9A%E7%9F%A5%E7%B1%BB%E5%9E%8B).
 * @property sendMsgAuthRange 房间消息发送权限范围设置
 *
 * @author ForteScarlet
 */
@Serializable
public data class Room(
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    override val roomId: ULong,
    @SerialName("room_name")
    override val roomName: String,
    @SerialName("room_type")
    override val roomTypeValue: String,
    @SerialName("group_id")
    @get:JvmName("getGroupId")
    override val groupId: ULong,
    @SerialName("room_default_notify_type")
    val roomDefaultNotifyTypeValue: String,
    @SerialName("send_msg_auth_range")
    val sendMsgAuthRange: SendMsgAuthRange,
) : RoomBasic {

    /**
     * 通过 [roomDefaultNotifyTypeValue] 转化为 [RoomDefaultNotifyType]。
     * @throws NoSuchElementException 如果没有匹配结果
     */
    val roomDefaultNotifyType: RoomDefaultNotifyType
        get() = RoomDefaultNotifyType.valueOf(roomDefaultNotifyTypeValue)
}

/**
 * [房间类型](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E7%B1%BB%E5%9E%8B)
 *
 * @see Room.roomType
 */
public enum class RoomType {
    /** 聊天房间 */
    BOT_PLATFORM_ROOM_TYPE_CHAT_ROOM,

    /** 帖子房间 */
    BOT_PLATFORM_ROOM_TYPE_POST_ROOM,

    /** 场景房间 */
    BOT_PLATFORM_ROOM_TYPE_SCENE_ROOM,

    /** 无效 */
    BOT_PLATFORM_ROOM_TYPE_INVALID
}

/**
 * [房间默认通知类型](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E9%BB%98%E8%AE%A4%E9%80%9A%E7%9F%A5%E7%B1%BB%E5%9E%8B)
 */
public enum class RoomDefaultNotifyType {
    /** 默认通知 */
    BOT_PLATFORM_DEFAULT_NOTIFY_TYPE_NOTIFY,

    /** 默认免打扰 */
    BOT_PLATFORM_DEFAULT_NOTIFY_TYPE_IGNORE,

    /** 无效 */
    BOT_PLATFORM_DEFAULT_NOTIFY_TYPE_INVALID,
}

/**
 * 房间消息发送权限范围
 *
 * @property isAllSendMsg 是否全局可发送
 * @property roles 可发消息的身份组 id
 */
@Serializable
public data class SendMsgAuthRange(
    @SerialName("is_all_send_msg")
    val isAllSendMsg: Boolean = false,
    @get:JvmName("getRolesUnsignedSource")
    val roles: List<ULong> = emptyList()
) {
    /**
     * Friendly property for Java.
     *
     * @see roles
     */
    public val rolesAsLong: List<Long>
        @JvmName("getRoles")
        get() = with(roles) {
            if (isEmpty()) emptyList() else roles.map { it.toLong() }
        }
}


/**
 * [分组房间信息](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#sendmsgauthrange#grouproom)
 *
 * @property groupId 分组 id
 * @property groupName 分组名称
 * @property roomList 房间信息
 *
 */
@Serializable
public data class GroupRoom(
    @SerialName("group_id")
    @get:JvmName("getGroupId")
    val groupId: ULong,
    @SerialName("group_name")
    val groupName: String,
    @SerialName("room_list")
    val roomList: List<ListRoom>
) {
    val groupIdStrValue: String get() = groupId.toString()
}


/**
 * [房间列表信息](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#sendmsgauthrange#listroom)
 *
 * @property roomId 房间 id
 * @property roomName 房间名称
 * @property roomTypeValue See [RoomType]: [房间类型](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#%E6%88%BF%E9%97%B4%E7%B1%BB%E5%9E%8B).
 * @property groupId 分组 id
 */
@Serializable
public data class ListRoom(
    @SerialName("room_id")
    @get:JvmName("getRoomId")
    override val roomId: ULong,
    @SerialName("room_name")
    override val roomName: String,
    @SerialName("room_type")
    override val roomTypeValue: String,
    @SerialName("group_id")
    @get:JvmName("getGroupId")
    override val groupId: ULong,
) : RoomBasic

/**
 * [分组信息](https://webstatic.mihoyo.com/vila/bot/doc/room_api/#sendmsgauthrange#group)
 *
 * @property groupId 分组 id
 * @property groupName 分组名称
 */
@Serializable
public data class Group(
    @SerialName("group_id")
    @get:JvmName("getGroupId")
    val groupId: ULong,
    @SerialName("group_name")
    val groupName: String
) {
    val groupIdStrValue: String get() = groupId.toString()
}

