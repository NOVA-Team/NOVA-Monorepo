/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class is used to mark certain values from specified enum as allowed.
 * Note that you must specify default state via #allowAll or #blockAll methods.
 *
 * Allows iteration of all allowed elements.
 *
 * @param <T> The enum
 */
public class EnumSelector<T extends Enum<T>> implements Iterable<T> {
	private EnumSet<T> exceptions;
	private boolean defaultAllow, defaultBlock = false;
	private boolean locked = false;

	private EnumSelector(Class<T> enumClass) {
		exceptions = EnumSet.noneOf(enumClass);
	}

	/**
	 * Creates a new instance of EnumSelector for the given type.
	 *
	 * @return an instance of EnumSelector for the given type.
	 */
	public static <T extends Enum<T>> EnumSelector<T> of(Class<T> enumClass) {
		return new EnumSelector(enumClass);
	}

	private void checkWritable() {
		if (locked)
			throw new IllegalStateException("No edits are allowed after EnumSelector has been locked.");
	}

	private void checkReadable() {
		if (!locked)
			throw new IllegalStateException("Cannot use EnumSelector that is not locked.");
	}

	/**
	 * Make the EnumSelector allow all for the given type by default.
	 * <p>
	 * Use {@link #apart(java.lang.Enum)} to specify what should be blocked.
	 *
	 * @see #blockAll()
	 * @see #apart(java.lang.Enum)
	 * @return this
	 * @throws IllegalStateException If the EnumSelector has been {@link #lock() locked}.
	 */
	public EnumSelector<T> allowAll() {
		checkWritable();
		if (!defaultBlock)
			defaultAllow = true;
		else
			throw new IllegalStateException("You can't allow all enum values when you are already blocking them.");
		return this;
	}

	/**
	 * Make the EnumSelector block all for the given type by default.
	 * <p>
	 * Use {@link #apart(java.lang.Enum)} to specify what should be allowed.
	 *
	 * @see #allowAll()
	 * @see #apart(java.lang.Enum)
	 * @return this
	 * @throws IllegalStateException If the EnumSelector has been {@link #lock() locked}.
	 */
	public EnumSelector<T> blockAll() {
		checkWritable();
		if (!defaultAllow)
			defaultBlock = true;
		else
			throw new IllegalStateException("You can't block all enum values when you are already allowing them.");
		return this;
	}

	/**
	 * Specify which {@code enum} values should have behavior opposite of the default
	 *
	 * @see #allowAll()
	 * @see #blockAll()
	 * @param value The given {@code enum} value that should have behavior opposite of the default.
	 * @return this
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public EnumSelector<T> apart(T value) {
		checkWritable();
		exceptions.add(value);
		return this;
	}

	/**
	 * Specify which {@code enum} values should have behavior opposite of the default
	 *
	 * @see #allowAll()
	 * @see #blockAll()
	 * @param first The given {@code enum} value that should have behavior opposite of the default.
	 * @param rest The given {@code enum} values that should have behavior opposite of the default.
	 * @return this
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public EnumSelector<T> apart(T first, T... rest) {
		checkWritable();
		exceptions.add(first);
		exceptions.addAll(Arrays.asList(rest));
		return this;
	}

	/**
	 * Lock the EnumSelector, making it immutable.
	 * Required for using of all getter methods.
	 *
	 * @see #allowAll()
	 * @see #blockAll()
	 * @see #locked()
	 * @return this
	 * @throws IllegalStateException If the EnumSelector does not have specified default behavior.
	 */
	public EnumSelector<T> lock() {
		if (defaultAllow || defaultBlock)
			locked = true;
		else
			throw new IllegalStateException("Cannot lock EnumSelector without specifying default behaviour.");
		return this;
	}

	/**
	 * Check if the EnumSelector instance has been locked.
	 *
	 * @return The locked status.
	 */
	public boolean locked() {
		return locked;
	}

	/**
	 * Check if the {@code enum} value is allowed by this EnumSelector.
	 *
	 * @param value The {@code enum} value to test.
	 * @return If the {@code enum} value is allowed by this EnumSelector.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public boolean allows(T value) {
		checkReadable();
		return defaultAllow ^ exceptions.contains(value);
	}

	/**
	 * Check if the EnumSelector allows all values.
	 *
	 * @return If the EnumSelector allows all values.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public boolean allowsAll() {
		checkReadable();
		return defaultAllow && exceptions.isEmpty();
	}

	/**
	 * Check if the {@code enum} value is blocked by this EnumSelector.
	 *
	 * @param value The {@code enum} value to test.
	 * @return If the {@code enum} value is blocked by this EnumSelector.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public boolean blocks(T value) {
		checkReadable();
		return defaultBlock ^ exceptions.contains(value);
	}

	/**
	 * Check if the EnumSelector blocks all values.
	 *
	 * @return If the EnumSelector blocks all values.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public boolean blocksAll() {
		checkReadable();
		return defaultBlock && exceptions.isEmpty();
	}

	/**
	 * Returns an iterator of all the allowed elements in this EnumSelector.
	 *
	 * @return The iterator.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	@Override
	public Iterator<T> iterator() {
		checkReadable();
		return Collections.unmodifiableSet(defaultBlock ? exceptions : EnumSet.complementOf(exceptions)).iterator();
	}

	/**
	 * Returns a spliterator of all the allowed elements in this EnumSelector.
	 *
	 * @return The spliterator.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	@Override
	public Spliterator<T> spliterator() {
		checkReadable();
		return Spliterators.spliterator(iterator(), defaultBlock ? exceptions.size() : EnumSet.complementOf(exceptions).size(),
				Spliterator.DISTINCT | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE);
	}

	/**
	 * Returns a sequential stream of all the allowed elements in this EnumSelector.
	 *
	 * @return The stream.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public Stream<T> stream() {
		checkReadable();
		return StreamSupport.stream(spliterator(), false);
	}

	/**
	 * Returns a parallel stream of all the allowed elements in this EnumSelector.
	 *
	 * @return The stream.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public Stream<T> parallelStream() {
		checkReadable();
		return StreamSupport.stream(spliterator(), true);
	}

	/**
	 * Returns a Set instance of all the allowed elements in this EnumSelector.
	 *
	 * @return The set.
	 * @throws IllegalStateException If the EnumSelector has not been {@link #lock() locked}.
	 */
	public Set<T> toSet() {
		checkReadable();
		return Collections.unmodifiableSet(defaultBlock ? exceptions : EnumSet.complementOf(exceptions));
	}
}
