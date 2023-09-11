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

import io.ktor.client.*

/**
 * 一个由平台决定内容的 [MiyousheApi] 父级抽象类，
 * 用于向 [MiyousheApi] 中提供更多平台API。
 *
 * 不要直接使用 [PlatformMiyousheApi] 类型本身，你应当使用 [MiyousheApi] 类型。
 *
 * @see MiyousheApi
 *
 */
public actual abstract class PlatformMiyousheApi<out R> {
    /**
     * 使用当前API发起一个请求，并得到一个[HTTP响应][HttpResponse].
     */
    public actual abstract suspend fun request(
        client: HttpClient,
        token: MiyousheApiToken
    ): io.ktor.client.statement.HttpResponse

}
