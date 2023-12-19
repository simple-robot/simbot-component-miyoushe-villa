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

package love.forte.simbot.miyoushe.utils.collection

import kotlin.concurrent.AtomicReference

/**
 * native平台下的 [SimpleConcurrentQueue] 实现。
 *
 * _注意：此实现尚处于试验阶段。_
 *
 */
private class SimpleConcurrentQueueImpl<T> : SimpleConcurrentQueue<T> {
    private val listRef = AtomicReference(emptyList<T>())

    /**
     * 向此队列添加一个元素。
     */
    override fun add(element: T): Boolean {
        while (true) {
            val list = listRef.value
            val newList = list + element
            if (list.size == newList.size) {
                return false
            }

            if (listRef.compareAndSet(list, newList)) {
                return true
            }
        }
    }

    /**
     * 移除指定目标。
     */
    override fun remove(element: T): Boolean {
        while (true) {
            val list = listRef.value
            if (list.isEmpty()) {
                return false
            }

            val newList = list - element
            if (list.size == newList.size) {
                return false
            }

            if (listRef.compareAndSet(list, newList)) {
                return true
            }
        }
    }

    override fun iterator(): Iterator<T> = listRef.value.iterator()
}

public actual fun <T> createSimpleConcurrentQueue(): SimpleConcurrentQueue<T> = SimpleConcurrentQueueImpl()
