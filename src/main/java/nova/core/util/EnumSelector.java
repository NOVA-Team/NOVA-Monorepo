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

import java.util.EnumSet;

/**
 * This class is used to mark certain values from specified enum as allowed.
 * Note that you must specify default state via #allowAll or #blockAll methods
 *
 * @param <T> The enum
 */
public class EnumSelector<T extends Enum<T>> {
	private EnumSet<T> exceptions;
	private boolean defaultAllow, defaultBlock = false;
	private boolean locked = false;

	private EnumSelector(Class<T> enumClass) {
		exceptions = EnumSet.noneOf(enumClass);
	}

	public static <T extends Enum<T>> EnumSelector<T> of(Class<T> enumClass) {
		return new EnumSelector(enumClass);
	}

	private void checkLocked() {
		if (locked)
			throw new IllegalStateException("No edits are allowed after EnumSelector has been locked.");
	}

	public EnumSelector<T> allowAll() {
		checkLocked();
		if (!defaultBlock)
			defaultAllow = true;
		else
			throw new IllegalStateException("You can't allow all enum values when you are already blocking them.");
		return this;
	}

	public EnumSelector<T> blockAll() {
		checkLocked();
		if (!defaultAllow)
			defaultBlock = true;
		else
			throw new IllegalStateException("You can't block all enum values when you are already allowing them.");
		return this;
	}

	public EnumSelector<T> apart(T value) {
		checkLocked();
		exceptions.add(value);
		return this;
	}

	public EnumSelector<T> lock() {
		if (defaultAllow || defaultBlock)
			locked = true;
		else
			throw new IllegalStateException("Cannot lock EnumSelector without specifying default behaviour.");
		return this;
	}

	public boolean locked() {
		return locked;
	}

	public boolean allows(T value) {
		if (!locked)
			throw new IllegalStateException("Cannot use EnumSelector that is not locked.");
		else
			return defaultAllow ^ exceptions.contains(value);
	}
}
