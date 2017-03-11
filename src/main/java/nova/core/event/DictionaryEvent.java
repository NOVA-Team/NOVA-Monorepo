/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.event;

import nova.core.event.bus.CancelableEvent;

/**
 * All events related to the dictionary.
 *
 * @param <T> dictionary type
 * @author ExE Boss
 */
public abstract class DictionaryEvent<T> extends CancelableEvent  {
	public final String key;
	public final T value;

	public DictionaryEvent(String key, T value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * A recipe added event is fired when a recipe of the right type has been added
	 * to the Dictionary.
	 *
	 * @param <T> dictionary type
	 */
	public static class Add<T> extends DictionaryEvent<T> {
		public Add(String key, T value) {
			super(key, value);
		}
	}

	/**
	 * A recipe removed event is fired when a recipe of the right type has been
	 * removed from the Dictionary.
	 *
	 * @param <T> dictionary type
	 */
	public static class Remove<T> extends DictionaryEvent<T> {
		public Remove(String key, T value) {
			super(key, value);
		}
	}
}
