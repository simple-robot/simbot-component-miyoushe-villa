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

package love.forte.simbot.miyoushe.api.role

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmName

/**
 * [身份组信息](https://webstatic.mihoyo.com/vila/bot/doc/role_api/)
 *
 * @property id 身份组 id
 * @property name 身份组名称
 * @property villaId 大别野 id
 * @property color 身份组颜色，可选项见[颜色](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E9%80%89%E9%A2%9C%E8%89%B2)
 * @property roleTypeValue 身份组类型(字符串)。可通过 [roleType] 得到对应的 [RoleType] 枚举值。
 * @property isAllRoom 是否选择全部房间
 * @property roomIds 指定的房间列表
 *
 */
@Serializable
public data class MemberRole(
    @get:JvmName("getId")
    val id: ULong,
    val name: String,

    @get:JvmName("getVillaId")
    @SerialName("villa_id")
    val villaId: ULong,
    val color: String,

    @SerialName("role_type")
    val roleTypeValue: String,

    @SerialName("is_all_room")
    val isAllRoom: Boolean = false,

    @get:JvmName("getRoomIdsUnsignedSource")
    @SerialName("room_ids")
    val roomIds: List<ULong> = emptyList(),
) {
    /**
     * Friendly property provided to Java
     */
    val roomIdsAsLong: List<Long>
        @JvmName("getRoomId")
        get() = with(roomIds) {
            if (isEmpty()) emptyList() else map { it.toLong() }
        }

    /**
     * 通过 [roleTypeValue] 转化为 [RoleType]
     *
     * @throws NoSuchElementException 如果没有对应的名称
     */
    @Suppress("MemberVisibilityCanBePrivate")
    val roleType: RoleType
        get() = RoleType.valueOf(roleTypeValue)
}

/**
 * 大别野内的某身份组
 *
 * ```json
 * {
 *     "retcode": 0,
 *     "message": "OK",
 *     "data": {
 *         "role": {
 *             "id": "368630049",  // 身份组 id
 *             "name": "管理员",    // 身份组名称
 *             "color": "#59A1EA", // 颜色
 *             "villa_id": "90002344", // 所属大别野 id
 *             "role_type": "MEMBER_ROLE_TYPE_ADMIN", // 身份组类型
 *             "member_num": "1",  // 身份组下成员数量
 *             "permissions": [    // 身份组拥有的权限列表
 *                 {
 *                     "key": "black_out",   // 权限 key 字符串
 *                     "name": "拉黑",        // 权限名称
 *                     "describe": "允许成员能够拉黑和将其他人移出大别野" // 权限描述
 *                 },
 *                 {
 *                     "key": "manage_chat_room",
 *                     "name": "聊天房间管理",
 *                     "describe": "允许成员开启聊天辩论、编辑房间权限、房间信息"
 *                 }
 *             ]
 *         }
 *     }
 * }
 * ```
 *
 * ```json
 * {
 *     "retcode": 0,
 *     "message": "OK",
 *     "data": {
 *         "list": [
 *             {
 *                 "id": "3684",   // 身份组 id
 *                 "name": "所有人", // 身份组名称
 *                 "color": "#8F9BBF", // 颜色
 *                 "role_type": "MEMBER_ROLE_TYPE_ALL_MEMBER", // 身份组类型
 *                 "villa_id": "900001", // 所属大别野 id
 *                 "member_num": "2" // 身份组下的成员数量
 *             },
 *             {
 *                 "id": "3686",
 *                 "name": "管理员",
 *                 "color": "#59A1EA",
 *                 "role_type": "MEMBER_ROLE_TYPE_ADMIN",
 *                 "villa_id": "900001",
 *                 "member_num": "0"
 *             }
 *         ]
 *     }
 * }
 * ```
 *
 */
@Serializable
public data class VillaRole(
    @get:JvmName("getId")
    val id: ULong,
    val name: String,
    val color: String,
    @SerialName("role_type")
    val roleTypeValue: String,
    @get:JvmName("getVillaId")
    @SerialName("villa_id")
    val villaId: ULong,
    @SerialName("member_num")
    val memberNum: Long = 0L,
    val permissions: List<Permission>? = null
) {
    public val idStrValue: String get() = id.toString()
    public val villaIdStrValue: String get() = villaId.toString()

    /**
     * 根据 [roleTypeValue] 转化为 [RoleType]
     * @throws NoSuchElementException 如果没有匹配结果
     */
    public val roleType: RoleType
        get() = RoleType.valueOf(roleTypeValue)

    /**
     * 身份组拥有的权限列表
     *
     * @property key 权限 key 字符串
     * @property name 权限名称
     * @property describe 权限描述
     *
     */
    @Serializable
    public data class Permission(@SerialName("key") val keyValue: String, val name: String, val describe: String) {

        /**
         * 根据 [keyValue] 转化为 [RolePermission]
         * @throws NoSuchElementException 如果没有匹配结果
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public val key: RolePermission
            get() = RolePermission.valueOf(keyValue.uppercase())
    }
}

/**
 * [身份组类型](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E7%B1%BB%E5%9E%8B)
 *
 * @see MemberRole.roleType
 */
public enum class RoleType {
    /** 所有人身份组 */
    MEMBER_ROLE_TYPE_ALL_MEMBER,

    /** 管理员身份组 */
    MEMBER_ROLE_TYPE_ADMIN,

    /** 大别野房主身份组 */
    MEMBER_ROLE_TYPE_OWNER,

    /** 其他自定义身份组 */
    MEMBER_ROLE_TYPE_CUSTOM,

    /** 未知 */
    MEMBER_ROLE_TYPE_UNKNOWN
}

/**
 * [身份组可添加权限](https://webstatic.mihoyo.com/vila/bot/doc/role_api/#%E8%BA%AB%E4%BB%BD%E7%BB%84%E5%8F%AF%E6%B7%BB%E5%8A%A0%E6%9D%83%E9%99%90)
 *
 * 需要注意在文档中的描述里，字段名称都是小写的。因此如果需要通过 [RolePermission.valueOf] 转化，
 * 需要注意大小写问题。
 *
 */
public enum class RolePermission {
    /** \@全体全员    允许成员能够 @全体成员 */
    MENTION_ALL,

    /** 撤回消息	允许成员能够在聊天房间中撤回任何人的消息 */
    RECALL_MESSAGE,

    /** 置顶消息	允许成员能够在聊天房间中置顶消息 */
    PIN_MESSAGE,

    /** 身份组管理	允许成员添加、删除身份组，管理身份组成员，修改身份组的权限 */
    MANAGE_MEMBER_ROLE,

    /** 编辑大别野详情	允许成员编辑大别野的简介、标签、设置大别野加入条件等 */
    EDIT_VILLA_INFO,

    /** 房间及分组管理	允许成员新建房间，新建/删除房间分组，调整房间及房间分组的排序 */
    MANAGE_GROUP_AND_ROOM,

    /** 禁言	允许成员能够在房间里禁言其他人 */
    VILLA_SILENCE,

    /** 拉黑	允许成员能够拉黑和将其他人移出大别野 */
    BLACK_OUT,

    /** 加入审核	允许成员审核大别野的加入申请 */
    HANDLE_APPLY,

    /** 聊天房间管理	允许成员编辑房间信息及设置可见、发言权限 */
    MANAGE_CHAT_ROOM,

    /** 查看大别野数据	允许成员查看大别野数据看板 */
    VIEW_DATA_BOARD,

    /** 组织活动	允许成员创建活动，编辑活动信息 */
    MANAGE_CUSTOM_EVENT,

    /** 点播房间节目	允许成员在直播房间中点播节目及控制节目播放 */
    LIVE_ROOM_ORDER,

    /** 设置精选消息	允许成员设置、移除精选消息 */
    MANAGE_SPOTLIGHT_COLLECTION,

}
