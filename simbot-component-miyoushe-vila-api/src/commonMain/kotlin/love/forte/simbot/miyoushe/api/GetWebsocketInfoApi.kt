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

package love.forte.simbot.miyoushe.api

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.emoticons.GetAllEmoticonsApi
import kotlin.jvm.JvmStatic


/**
 * [获取 Websocket 接入信息接口](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_api.html)
 *
 * `GET /vila/api/bot/platform/getWebsocketInfo`
 *
 * @author ForteScarlet
 */
public class GetWebsocketInfoApi private constructor() : MiyousheVillaGetApi<WebsocketInfo>() {
    public companion object Factory {
        private val INSTANCE = GetWebsocketInfoApi()
        private const val API_PATH = "/vila/api/bot/platform/getWebsocketInfo"

        private val apiResultSer = ApiResult.serializer(WebsocketInfo.serializer())

        /**
         * 构建 [GetAllEmoticonsApi] 实例。
         */
        @JvmStatic
        public fun create(): GetWebsocketInfoApi = INSTANCE
    }

    override val path: String
        get() = API_PATH

    override val resultSerializer: KSerializer<WebsocketInfo>
        get() = WebsocketInfo.serializer()

    override val apiResultSerializer: DeserializationStrategy<ApiResult<WebsocketInfo>>
        get() = apiResultSer

    override fun toString(): String {
        return "GetWebsocketInfoApi()"
    }


}

/**
 * Result of [GetWebsocketInfoApi]
 */
@Serializable
public data class WebsocketInfo(
    /**
     * Websocket 接入地址
     */
    @SerialName("websocket_url") val websocketUrl: String,
    /**
     * Websocket 连接使用的 `uid` 参数
     */
    val uid: Long,
    /**
     * Websocket 接入使用的 `app_id` 参数
     */
    @SerialName("app_id") val appId: Int,
    /**
     * Websocket 接入使用的 `platform` 参数
     */
    val platform: Int,
    /**
     * Websocket 接入使用的 `device_id` 参数
     */
    @SerialName("device_id") val deviceId: String,
)

// {
// "retcode":0,
// "message":"OK",
// "data":{
//     "websocket_url":"wss://ws-mysbot-hulk.mihoyo.com:443/ws",
//     "uid":"59",
//     "app_id":104,
//     "platform":3,
//     "device_id":"bot_uyoiEQHXtqFgBLHdoBJg"
//   }
// }
