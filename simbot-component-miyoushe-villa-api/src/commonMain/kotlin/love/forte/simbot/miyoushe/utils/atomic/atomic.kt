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


/**
 *
 * @author ForteScarlet
 */
public interface AtomicLong {
    public var value: Long
    public fun incrementAndGet(delta: Long = 1L): Long
    public fun decrementAndGet(delta: Long = 1L): Long
    public fun getAndIncrement(delta: Long = 1L): Long
    public fun getAndDecrement(delta: Long = 1L): Long
    public fun compareAndSet(expect: Long, value: Long): Boolean
}

/**
 *
 * @author ForteScarlet
 */
public interface AtomicULong {
    public var value: ULong
    public fun incrementAndGet(delta: ULong = 1u): ULong
    public fun decrementAndGet(delta: ULong = 1u): ULong
    public fun getAndIncrement(delta: ULong = 1u): ULong
    public fun getAndDecrement(delta: ULong = 1u): ULong
    public fun compareAndSet(expect: ULong, value: ULong): Boolean
}

public interface AtomicBoolean {
    public var value: Boolean
    public fun compareAndSet(expect: Boolean, value: Boolean): Boolean
}

public expect fun atomic(value: Long): AtomicLong
public expect fun atomic(value: Boolean): AtomicBoolean
public expect fun atomic(value: ULong): AtomicULong

public fun atomicULong(value: ULong): AtomicULong = atomic(value)
