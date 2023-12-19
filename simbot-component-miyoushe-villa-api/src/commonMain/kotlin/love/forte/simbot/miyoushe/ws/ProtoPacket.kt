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

package love.forte.simbot.miyoushe.ws

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import love.forte.simbot.miyoushe.ws.ProtoPacketDefinition.Companion.MAGIC_VALUE
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * [WS 协议包](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html) 数据定义接口。
 *
 * @see ProtoPacket
 */
public interface ProtoPacketDefinition {
    /*
     * 定长部分格式
     * 定长头决定了一个消息包在一串byte流中的起点和终点。
     *
     * 接收端从读取到Magic字段开始，再读取4个字节获得变长部分长度，再读取DataLen个byte就能获得变长部分的完整数据。
     *
     * 字段	字段类型	字段value在byte
     * 数组中的index区间	说明	示例value
     * Magic	uint32	[0,4)	用于标识报文的开始
     * 目前的协议的magic值是十六进制的【0xBABEFACE】	0xBABEFACE
     * DataLen	uint32	[4,8)	变长部分总长度=变长头长度+变长消息体长度	1024
     */

    //region 定长部分
    /**
     * uint32 [0,4) 用于标识报文的开始
     *
     * 默认为 [MAGIC_VALUE]
     */
    public val magic: UInt

    /**
     * uint32 [4,8) 变长部分总长度=变长头长度+变长消息体长度	1024
     */
    public val dataLen: UInt
    //endregion


    /*
     * 变长部分格式
     * 变长部分包括变长头和消息体。
     *
     * 变长消息体最大长度可能达到1MB，通过读取前HeaderLen个byte的变长头，接收端不需要读完所有的变长数据就能提前做一些逻辑处理，如去重、识别消息体类型。
     *
     * 数据类型	字段	字段类型	字段value
     * 在byte数组中
     * 的index区间	说明	示例value
     * 变长头	HeaderLen	uint32	[8,12)	变长头总长度，变长头部分所有字段（包括HeaderLen本身）的总长度。	28
     * 变长头	ID	uint64	[12,20)	协议包序列ID，同一条连接上的发出的协议包应该单调递增，
     * 相同序列ID且Flag字段相同的包应该被认为是同一个包	123
     * 变长头	Flag	uint32	[20,24)	配合bizType使用，用于标识同一个bizType协议的方向。
     * 用 1 代表主动发到服务端的request包
     * 用 2 代表针对某个request包回应的response包	1
     * 变长头	BizType	uint32	[24,28)	消息体的业务类型，用于标识Body字段中的消息所属业务类型
     * 参见附录 bizType 参数表	1000
     * 变长头	AppId	int32	[28,32)	应用标识。固定为 104	104
     * 消息体	BodyData	byte数组	[32,1024K+8)	协议数据。数据采用protobuf Marshal之后的二进制数组。数据结构详见 protobuf 文件地址。
     */

    //region 变长头
    /**
     * HeaderLen	uint32	[8,12)	变长头总长度，变长头部分所有字段（包括HeaderLen本身）的总长度。
     *
     * 一般来讲值是 `20` 或 `24`, 取决于 [appId] 是否为 0。
     */
    public val headerLen: UInt

    /**
     * ID	uint64	[12,20)	协议包序列ID，同一条连接上的发出的协议包应该单调递增，
     */
    public val id: ULong

    /**
     * Flag	uint32	[20,24)	配合bizType使用，用于标识同一个bizType协议的方向。
     * - 用 1 代表主动发到服务端的request包
     * - 用 2 代表针对某个request包回应的response包
     *
     * @see ProtoPacketFlag
     */
    public val flag: UInt

    /**
     * BizType	uint32	[24,28)	消息体的业务类型，用于标识Body字段中的消息所属业务类型
     */
    public val bizType: UInt

    /**
     * AppId	int32	[28,32)	应用标识。
     */
    public val appId: UInt
    //endregion

    //region 变长体
    /**
     * BodyData	byte数组	[32,1024K+8)	协议数据。数据采用protobuf Marshal之后的二进制数组。数据结构详见 protobuf 文件地址。
     */
    public val bodyData: ByteArray
    //endregion


    @JsExport.Ignore // TODO provide create fun to JS?
    public companion object {
        public const val MAGIC_VALUE: UInt = 0xBABEFACEu
    }

}

/**
 * 配合 [ProtoPacketDefinition.bizType] 使用，用于标识同一个 `bizType` 协议的方向。
 * - 用 `1` 代表主动发到服务端的request包
 * - 用 `2` 代表针对某个request包回应的response包
 */
public enum class ProtoPacketFlag(@get:JvmName("getValue") public val value: UInt) {
    /**
     * 代表主动发到服务端的request包
     */
    REQUEST(1u),

    /**
     * 代表针对某个request包回应的response包
     */
    RESPONSE(2u)
}

/**
 * [WS 协议包](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html)
 *
 * @author ForteScarlet
 */
public class ProtoPacket : ProtoPacketDefinition {
    public companion object {
        private const val HEADER_LEN: UInt = 24u
        private const val HEADER_LEN_NO_APPID: UInt = 20u
        private val EMPTY_BODY = byteArrayOf()

        /**
         * 根据参数构建 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("create")
        public fun create(bizType: UInt, id: ULong, appid: UInt, flag: UInt, body: ByteArray): ProtoPacket {
            return ProtoPacket().apply {
                this.appId = appid
                this.headerLen = if (appid == 0u) HEADER_LEN_NO_APPID else HEADER_LEN
                this.bizType = bizType
                this.id = id
                this.flag = flag

                bodyData = body
                dataLen = headerLen + body.size.toUInt()
            }
        }

        /**
         * 根据参数构建 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("create")
        @JsName("createByBizType")
        public fun create(
            bizType: BizType,
            id: ULong,
            appid: UInt,
            flag: ProtoPacketFlag,
            body: ByteArray
        ): ProtoPacket {
            return ProtoPacket().apply {
                this.appId = appid
                this.headerLen = if (appid == 0u) HEADER_LEN_NO_APPID else HEADER_LEN
                this.bizType = bizType.value
                this.id = id
                this.flag = flag.value

                bodyData = body
                dataLen = headerLen + body.size.toUInt()
            }
        }

        /**
         * 根据参数构建代表针对某个 request 包回应的 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("response")
        public fun response(bizType: UInt, id: ULong, appid: UInt, body: ByteArray): ProtoPacket {
            return create(bizType, id, appid, ProtoPacketFlag.RESPONSE.value, body)
        }

        /**
         * 根据参数构建代表主动发送到服务器的 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("request")
        public fun request(bizType: UInt, id: ULong, appid: UInt, body: ByteArray): ProtoPacket {
            return create(bizType, id, appid, ProtoPacketFlag.REQUEST.value, body)
        }

        /**
         * 根据参数构建代表针对某个 request 包回应的 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("response")
        @JsName("responseByBizType")
        public fun response(bizType: BizType, id: ULong, appid: UInt, body: ByteArray): ProtoPacket {
            return create(bizType, id, appid, ProtoPacketFlag.RESPONSE, body)
        }

        /**
         * 根据参数构建代表主动发送到服务器的 [ProtoPacket]。
         */
        @JvmStatic
        @JvmName("request")
        @JsName("requestByBizType")
        public fun request(bizType: BizType, id: ULong, appid: UInt, body: ByteArray): ProtoPacket {
            return create(bizType, id, appid, ProtoPacketFlag.REQUEST, body)
        }
    }

    //region 定长部分
    override var magic: UInt = MAGIC_VALUE
    override var dataLen: UInt = 0u
    //endregion

    //region 变长头
    /**
     * HeaderLen	uint32	[8,12)	变长头总长度，变长头部分所有字段（包括HeaderLen本身）的总长度。
     */
    override var headerLen: UInt = 0u
    override var id: ULong = 0u
    override var flag: UInt = 0u
    override var bizType: UInt = 0u
    override var appId: UInt = 104u
    //endregion

    //region 变长体
    override var bodyData: ByteArray = EMPTY_BODY
    //endregion

    override fun toString(): String {
        return "ProtoPacket(magic=$magic, dataLen=$dataLen, headerLen=$headerLen, id=$id, flag=$flag, bizType=$bizType, appId=$appId, bodyData.len=${bodyData.size})"
    }
}

/**
 * Read [ProtoPacket] from given [ByteReadChannel].
 */
@JsExport.Ignore
public suspend fun ByteReadChannel.readToProtoPacket(): ProtoPacket {
    return ProtoPacket().apply {
        suspend fun readUIntLittleEndian(): UInt = readIntLittleEndian().toUInt()
        // 定长部分
        magic = readUIntLittleEndian()
        dataLen = readUIntLittleEndian()

        // 变长头
        headerLen = readUIntLittleEndian()
        id = readLongLittleEndian().toULong()
        flag = readUIntLittleEndian()
        bizType = readUIntLittleEndian()
        appId = readUIntLittleEndian()

        // max: 变长消息体最大长度可能达到1MB，通过读取前HeaderLen个byte的变长头，接收端不需要读完所有的变长数据就能提前做一些逻辑处理，如去重、识别消息体类型。
        // DataLen = 变长部分总长度=变长头长度+变长消息体长度
        val bodyLen = (dataLen - headerLen).toLong()
        if (bodyLen > 0) {
            bodyData = readRemaining(limit = bodyLen).readBytes()
        }
    }
}

/**
 * Read [ProtoPacket] from given [ByteReadPacket].
 */
public fun ByteReadPacket.readToProtoPacket(): ProtoPacket {
    return ProtoPacket().apply {
        fun readUIntLittleEndian(): UInt = readIntLittleEndian().toUInt()
        // 定长部分
        magic = readUIntLittleEndian()
        dataLen = readUIntLittleEndian()

        // 变长头
        headerLen = readUIntLittleEndian()
        id = readLongLittleEndian().toULong()
        flag = readUIntLittleEndian()
        bizType = readUIntLittleEndian()
        appId = readUIntLittleEndian()

        // max: 变长消息体最大长度可能达到1MB，通过读取前HeaderLen个byte的变长头，接收端不需要读完所有的变长数据就能提前做一些逻辑处理，如去重、识别消息体类型。
        // DataLen = 变长部分总长度=变长头长度+变长消息体长度
        val bodyLen = (dataLen - headerLen).toInt()
        if (bodyLen > 0) {
            bodyData = readBytes(bodyLen)
        }
    }
}


public fun ProtoPacketDefinition.toPacket(): ByteReadPacket {
    return buildPacket {
        fun writeUIntLittleEndian(value: UInt) {
            writeIntLittleEndian(value.toInt())
        }

        // 定长
        writeUIntLittleEndian(magic)
        writeUIntLittleEndian(dataLen)

        // 变长
        writeUIntLittleEndian(headerLen)
        writeLongLittleEndian(id.toLong())
        writeUIntLittleEndian(flag)
        writeUIntLittleEndian(bizType)
        writeUIntLittleEndian(appId)
        writeFully(bodyData)
    }
}


