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

package love.forte.simbot.miyoushe

/**
 *
 * 一些与米游社大别野机器人相关的信息或常量等。
 *
 * 参考: [米游社大别野机器人](https://webstatic.mihoyo.com/vila/bot/doc/)。
 *
 */
public object Miyoushe {

    /**
     * 机器人调用接口的开放接口访问域名。
     */
    public const val API_PATH: String = "https://bbs-api.miyoushe.com"

    /**
     * 机器人接口调用时使用的相关请求头。
     *
     * > 机器人调用大别野接口的时候，需要在 http header 中带上机器人凭证
     *
     * 更多参考：[开发说明 - 机器人调用接口](https://webstatic.mihoyo.com/vila/bot/doc/#%E6%9C%BA%E5%99%A8%E4%BA%BA%E8%B0%83%E7%94%A8%E6%8E%A5%E5%8F%A3)
     */
    public object Headers {
        /**
         * `bot_id`，平台下发的机器人唯一标志
         */
        public const val BOT_ID_KEY: String = "x-rpc-bot_id"

        /**
         * `bot_secret` ，平台下发的机器人鉴权标志
         */
        public const val BOT_SECRET_KEY: String = "x-rpc-bot_secret"

        /**
         * `villa_id`，大别野 id
         */
        public const val BOT_VILLA_ID_KEY: String = "x-rpc-bot_villa_id"
    }

}
