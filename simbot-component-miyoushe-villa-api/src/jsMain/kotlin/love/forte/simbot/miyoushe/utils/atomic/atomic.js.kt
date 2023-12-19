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


public actual fun atomic(value: Long): AtomicLong = JSAtomicLong(value)

private class JSAtomicLong(override var value: Long) : AtomicLong {

    override fun incrementAndGet(delta: Long): Long {
        value += delta
        return value
    }

    override fun decrementAndGet(delta: Long): Long {
        value -= delta
        return value
    }

    override fun getAndIncrement(delta: Long): Long {
        val result = value
        value += delta
        return result
    }

    override fun getAndDecrement(delta: Long): Long {
        val result = value
        value -= delta
        return result
    }

    override fun compareAndSet(expect: Long, value: Long): Boolean {
        val current = this.value
        if (current == expect) {
            this.value = value
            return true
        }

        return false
    }
}

public actual fun atomic(value: ULong): AtomicULong = JSAtomicULong(value)


private class JSAtomicULong(override var value: ULong) : AtomicULong {
    override fun incrementAndGet(delta: ULong): ULong {
        value += delta
        return value
    }

    override fun decrementAndGet(delta: ULong): ULong {
        value -= delta
        return value
    }

    override fun getAndIncrement(delta: ULong): ULong {
        val result = value
        value += delta
        return result
    }

    override fun getAndDecrement(delta: ULong): ULong {
        val result = value
        value -= delta
        return result
    }

    override fun compareAndSet(expect: ULong, value: ULong): Boolean {
        val current = this.value
        if (current == expect) {
            this.value = value
            return true
        }

        return false
    }
}

private class JSAtomicBoolean(override var value: Boolean) : AtomicBoolean {
    override fun compareAndSet(expect: Boolean, value: Boolean): Boolean {
        val current = this.value
        if (current == expect) {
            this.value = value
            return true
        }

        return false
    }
}

public actual fun atomic(value: Boolean): AtomicBoolean = JSAtomicBoolean(value)
