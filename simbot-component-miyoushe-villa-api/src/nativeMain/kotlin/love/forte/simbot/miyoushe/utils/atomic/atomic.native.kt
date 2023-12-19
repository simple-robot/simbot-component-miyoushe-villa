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

package love.forte.simbot.miyoushe.utils.atomic

public actual fun atomic(value: Long): AtomicLong = NAtomicLong(value)

private class NAtomicLong(value: Long) : AtomicLong {
    private val atomic = kotlin.concurrent.AtomicLong(value)

    override var value: Long by atomic::value

    override fun incrementAndGet(delta: Long): Long =
        if (delta == 1L) atomic.incrementAndGet() else atomic.addAndGet(delta)

    override fun decrementAndGet(delta: Long): Long =
        if (delta == 1L) atomic.decrementAndGet() else atomic.addAndGet(-delta)

    override fun getAndIncrement(delta: Long): Long =
        if (delta == 1L) atomic.getAndIncrement() else atomic.getAndAdd(delta)

    override fun getAndDecrement(delta: Long): Long =
        if (delta == 1L) atomic.getAndDecrement() else atomic.getAndAdd(-delta)

    override fun compareAndSet(expect: Long, value: Long): Boolean = atomic.compareAndSet(expect, value)
}

public actual fun atomic(value: ULong): AtomicULong = NAtomicULong(value)

private class NAtomicULong(value: ULong) : AtomicULong {
    private val atomic = kotlin.concurrent.AtomicLong(value.toLong())

    override var value: ULong
        get() = atomic.value.toULong()
        set(value) {
            atomic.value = value.toLong()
        }

    override fun incrementAndGet(delta: ULong): ULong =
        if (delta == ONE) atomic.incrementAndGet().toULong() else atomic.addAndGet(delta.toLong()).toULong()

    override fun decrementAndGet(delta: ULong): ULong =
        if (delta == ONE) atomic.decrementAndGet().toULong() else atomic.addAndGet(-delta.toLong()).toULong()

    override fun getAndIncrement(delta: ULong): ULong =
        if (delta == ONE) atomic.getAndIncrement().toULong() else atomic.getAndAdd(delta.toLong()).toULong()

    override fun getAndDecrement(delta: ULong): ULong =
        if (delta == ONE) atomic.getAndDecrement().toULong() else atomic.getAndAdd(-delta.toLong()).toULong()

    override fun compareAndSet(expect: ULong, value: ULong): Boolean =
        atomic.compareAndSet(expect.toLong(), value.toLong())

    companion object {
        private const val ONE: ULong = 1u
    }
}

public actual fun atomic(value: Boolean): AtomicBoolean = NAtomicBoolean(value)

private class NAtomicBoolean(value: Boolean) : AtomicBoolean {
    private val atomic = kotlin.concurrent.AtomicInt(value.toBInt())
    override var value: Boolean
        get() = atomic.value.toBool()
        set(value) {
            atomic.value = value.toBInt()
        }

    override fun compareAndSet(expect: Boolean, value: Boolean): Boolean =
        atomic.compareAndSet(expect.toBInt(), value.toBInt())

    companion object {
        const val FALSE = 0
        const val TRUE = 1
        fun Boolean.toBInt() = if (this) TRUE else FALSE
        fun Int.toBool() = this == TRUE
    }
}
