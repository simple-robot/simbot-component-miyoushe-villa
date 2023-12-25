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

package love.forte.simbot.component.miyoushe.bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.InternalSimbotApi
import love.forte.simbot.miyoushe.stdlib.bot.Bot
import love.forte.simbot.miyoushe.stdlib.bot.BotConfiguration
import love.forte.simbot.miyoushe.ws.PLogin


/**
 * 大别野机器人在通过配置文件读取数据时使用的反序列化实例类型。
 * 仅由内部作为反序列化类型使用，对外不稳定。
 *
 * @author ForteScarlet
 */
@Serializable
@InternalSimbotApi
public data class VillaSerializableBotConfiguration(
    /**
     * 票据信息。
     */
    val ticket: Ticket,

    /**
     * 其他的额外配置。
     */
    val config: Config? = null
) {

    /**
     *
     * @see Bot.Ticket
     */
    @Serializable
    public data class Ticket(val botId: String, val botSecret: String) {
        public fun toTicket(): Bot.Ticket = Bot.Ticket(botId, botSecret)
    }


    @Serializable
    public data class Config(
        /**
         * 创建连接（获取 ws 连接信息）时，提供给 API 的 token 中的`villa_id` 属性。
         * @see BotConfiguration.loginVillaId
         */
        val loginVillaId: String? = null,

        /**
         * 应用在 [PLogin.meta] 上的属性。
         * @see BotConfiguration.loginMeta
         */
        val loginMeta: Map<String, String>? = null,

        /**
         * 应用在 [PLogin.meta] 上的属性。如果为 `null` 则会使用一个随机值。
         * @see BotConfiguration.loginRegion
         */
        val loginRegion: String? = null,

        /**
         * 与部分超时相关的配置信息。
         *
         * ```json
         * {
         *   "config": {
         *     "timeout": {
         *       "apiHttpRequestTimeoutMillis": ...,
         *       "apiHttpConnectTimeoutMillis": ...,
         *       "apiHttpSocketTimeoutMillis": ...
         *     }
         *   }
         * }
         * ```
         *
         * @see TimeoutConfig
         *
         */
        @SerialName("timeout")
        public val timeoutConfig: TimeoutConfig? = null,
    )

    internal fun toConfiguration(): VillaBotConfiguration {
        val configuration = VillaBotConfiguration().apply {
            config?.also { config ->
                botConfiguration = BotConfiguration().apply {
                    config.loginVillaId?.also { loginVillaId = it }
                    config.loginMeta?.also { loginMeta = it }
                    config.loginRegion?.also { loginRegion = it }
                    config.timeoutConfig?.also {
                        apiHttpRequestTimeoutMillis = it.apiHttpRequestTimeoutMillis
                        apiHttpConnectTimeoutMillis = it.apiHttpConnectTimeoutMillis
                        apiHttpSocketTimeoutMillis = it.apiHttpSocketTimeoutMillis
                    }
                }
            }
        }

        return configuration
    }

}

/**
 * 与部分超时相关的配置信息。
 * ```json
 * {
 *   "config": {
 *     "timeout": {
 *       "apiHttpRequestTimeoutMillis": ...,
 *       "apiHttpConnectTimeoutMillis": ...,
 *       "apiHttpSocketTimeoutMillis": ...
 *     }
 *   }
 * }
 * ```
 *
 * @see BotConfiguration.apiHttpRequestTimeoutMillis
 * @see BotConfiguration.apiHttpConnectTimeoutMillis
 * @see BotConfiguration.apiHttpSocketTimeoutMillis
 */
@Serializable
@InternalSimbotApi
public data class TimeoutConfig(
    /** @see BotConfiguration.apiHttpRequestTimeoutMillis */
    val apiHttpRequestTimeoutMillis: Long? = null,
    /** @see BotConfiguration.apiHttpConnectTimeoutMillis */
    val apiHttpConnectTimeoutMillis: Long? = null,
    /** @see BotConfiguration.apiHttpSocketTimeoutMillis */
    val apiHttpSocketTimeoutMillis: Long? = null,
)
