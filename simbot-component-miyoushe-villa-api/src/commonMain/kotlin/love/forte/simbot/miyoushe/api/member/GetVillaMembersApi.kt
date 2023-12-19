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

package love.forte.simbot.miyoushe.api.member

import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.miyoushe.api.ApiResult
import love.forte.simbot.miyoushe.api.MiyousheVillaGetApi
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * [获取大别野成员列表](https://webstatic.mihoyo.com/vila/bot/doc/member_api/get_villa_members.html)
 *
 * 获取大别野成员列表
 *
 * `GET /vila/api/bot/platform/getVillaMembers`
 *
 *
 * @author ForteScarlet
 */
public class GetVillaMembersApi private constructor(
    /** 起始位置偏移量 */
    private val offset: String,
    /** 分页大小 */
    private val size: ULong,
) : MiyousheVillaGetApi<GetVillaMembersResult>() {
    /*
    offset	uint64	偏移量（该字段已废弃）
offset_str	string	起始位置偏移量
size	uint64	分页大小
     */
    public companion object Factory {
        private const val PATH = "/vila/api/bot/platform/getVillaMembers"
        private val RESULT_SER = ApiResult.serializer(GetVillaMembersResult.serializer())
        public const val FIRST_OFFSET: String = ""

        /**
         * 构建 [GetVillaMembersApi].
         *
         * 当 [offset] 为空字符串时，系统会返回第一页的成员列表信息；
         * 当使用本次请求返回值中的 [next_offset_str][GetVillaMembersResult.nextOffset] 作为下次请求中的 [offset] 时，
         * 会返回下一页的成员列表。
         *
         * @param offset 起始位置偏移量
         * @param size 分页大小
         *
         */
        @JvmStatic
        @JvmName("create")
        public fun create(offset: String, size: ULong): GetVillaMembersApi = GetVillaMembersApi(offset, size)

        /**
         * 构建 [GetVillaMembersApi].
         *
         * 当 [offset] 为空字符串时，系统会返回第一页的成员列表信息；
         * 当使用本次请求返回值中的 [next_offset_str][GetVillaMembersResult.nextOffset] 作为下次请求中的 [offset] 时，
         * 会返回下一页的成员列表。
         *
         * @param size 分页大小
         */
        @JvmStatic
        @JvmName("create")
        public fun create(size: ULong): GetVillaMembersApi = GetVillaMembersApi(FIRST_OFFSET, size)

        /**
         * 提供一个每页的分页大小和对 [GetVillaMembersApi] 的具体请求方式，
         * 通过偏移量自动以分页的形式查询所有的数据，
         * 并作为 [Flow] 结果返回。
         */
        public inline fun getVillaMembersApiFlow(
            fixSize: ULong,
            crossinline doRequest: suspend GetVillaMembersApi.() -> GetVillaMembersResult
        ): Flow<Member> = flow {
            var result: GetVillaMembersResult
            var offset = FIRST_OFFSET
            do {
                result = create(offset = offset, size = fixSize).doRequest()
                if (offset == result.nextOffset) {
                    // 两次偏移量一样
                    break
                }

                offset = result.nextOffset
                for (member in result.list) {
                    emit(member)
                }
            } while (result.list.isNotEmpty())
        }
    }

    override fun URLBuilder.prepareUrl(): URLBuilder = apply {
        with(parameters) {
            append("offset", offset)
            append("size", size.toString())
        }
    }

    override val path: String
        get() = PATH
    override val resultSerializer: DeserializationStrategy<GetVillaMembersResult>
        get() = GetVillaMembersResult.serializer()
    override val apiResultSerializer: DeserializationStrategy<ApiResult<GetVillaMembersResult>>
        get() = RESULT_SER

    override fun toString(): String {
        return "GetVillaMembersApi(offset='$offset', size=$size)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GetVillaMembersApi) return false

        if (offset != other.offset) return false
        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offset.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }

}

/**
 * Result of [GetVillaMembersApi].
 *
 * @property list 用户信息列表
 * @property nextOffset 下一页的偏移量起始位置
 */
@Serializable
public data class GetVillaMembersResult(
    val list: List<Member> = emptyList(),
    @SerialName("next_offset_str")
    val nextOffset: String,
)


