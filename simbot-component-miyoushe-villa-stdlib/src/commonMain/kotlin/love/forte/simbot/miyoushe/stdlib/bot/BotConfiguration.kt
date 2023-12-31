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

package love.forte.simbot.miyoushe.stdlib.bot

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
import love.forte.simbot.miyoushe.MiyousheVilla
import love.forte.simbot.miyoushe.ws.PLogin
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * [Bot] 的配置信息。
 *
 * @author ForteScarlet
 */
public class BotConfiguration {
    /**
     * 协程上下文。
     * 构建时会根据 [coroutineContext] 生成一个 [Job],
     * 并将 [coroutineContext] 中现有的 [Job] （如果有的话）作为 `parent`。
     */
    public var coroutineContext: CoroutineContext = EmptyCoroutineContext

    /**
     * 创建连接（获取 ws 连接信息）时，提供给 API 的 token 中的
     * `villa_id` 属性。默认为 `0`。
     *
     * > 机器人未上线时，villa_id 使用测试别野，上线后可传 0
     *
     * 参考：[消息体相关协议 PLogin](https://webstatic.mihoyo.com/vila/bot/doc/websocket/websocket_package.html#plogin)
     *
     * 因为是作为请求头和拼接 token 使用的值，因此为字符串格式。
     */
    public var loginVillaId: String = "0"

    /**
     * 应用在 [PLogin.meta] 上的属性。
     */
    public var loginMeta: Map<String, String> = emptyMap()

    /**
     * 应用在 [PLogin.region] 上的属性。如果为 `null` 则会使用一个随机值。
     */
    public var loginRegion: String? = null

    /**
     * 用于API请求的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html)。
     *
     * 如果 [apiClientEngine] 和 [apiClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var apiClientEngine: HttpClientEngine? = null

    /**
     * 用于API请求的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html) 工厂。
     *
     * 如果 [apiClientEngine] 和 [apiClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var apiClientEngineFactory: HttpClientEngineFactory<*>? = null

    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpRequestTimeoutMillis]、[apiHttpRequestTimeoutMillis]、[apiHttpRequestTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.requestTimeoutMillis
     */
    public var apiHttpRequestTimeoutMillis: Long? = null

    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpConnectTimeoutMillis]、[apiHttpConnectTimeoutMillis]、[apiHttpConnectTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.connectTimeoutMillis
     */
    public var apiHttpConnectTimeoutMillis: Long? = null

    /**
     * API请求中的超时请求配置。
     *
     * 如果 [apiHttpSocketTimeoutMillis]、[apiHttpSocketTimeoutMillis]、[apiHttpSocketTimeoutMillis] 都为null，则不会配置timeout。
     *
     * @see HttpTimeout.HttpTimeoutCapabilityConfiguration.socketTimeoutMillis
     */
    public var apiHttpSocketTimeoutMillis: Long? = null

    /**
     * 用于ws的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html)。
     *
     * 如果 [wsClientEngine] 和 [wsClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var wsClientEngine: HttpClientEngine? = null

    /**
     * 用于连接ws的 [HttpClient] 所使用的[引擎](https://ktor.io/docs/http-client-engines.html) 工厂。
     *
     * 如果 [wsClientEngine] 和 [wsClientEngineFactory] 都为null，则会尝试使用一个默认引擎。
     *
     */
    public var wsClientEngineFactory: HttpClientEngineFactory<*>? = null

    /**
     * 用于API请求结果反序列化的 [Json].
     *
     * 默认为 [MiyousheVilla.DefaultJson]
     *
     */
    public var apiDecoder: Json = MiyousheVilla.DefaultJson

    public companion object {
        public const val DEFAULT_EVENT_BUFFER_CAPACITY: Int = 64
    }
}
