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

package love.forte.simbot.miyoushe.utils

/**
 * 将一个 [ByteArray] 转为16进制的字符串。
 */
public fun ByteArray.toHex(): String {
    /*
        等 Kotlin ByteArray.toHexString 稳定后迁移实现
     */
    return buildString {
        this@toHex.forEach { b ->
            val str = (b.toInt() and 0xff).toString(16)
            if (str.length == 1) {
                append('0')
            }
            append(str)
        }
    }
}
