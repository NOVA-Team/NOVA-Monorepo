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

import nova.core.event.bus.EventBus;
import nova.core.event.bus.EventListener;
import nova.core.event.bus.EventListenerHandle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A dictionary where each identifying string represents a set of objects
 * and each object can have a set of identifiers.
 *
 * @param <T> The object type
 */
public class Dictionary<T> {
	private final Map<String, Set<T>> entries = new HashMap<>();
	private final Map<T, Set<String>> locations = new HashMap<>();
	private final EventBus<AddEvent<T>> addEventListeners = new EventBus<>();
	private final EventBus<RemoveEvent<T>> removeEventListeners = new EventBus<>();

	/**
	 * Add an object to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param object the object to register.
	 */
	public void add(String key, T object) {
		// TODO: Enforce name to be in camelCase
		if (!entries.containsKey(key)) {
			entries.put(key, new HashSet<>());
		}

		entries.get(key).add(object);

		if (!locations.containsKey(object)) {
			locations.put(object, new HashSet<>());
		}

		locations.get(object).add(key);

		addEventListeners.publish(new AddEvent<>(key, object));
	}

	/**
	 * .
	 * Removes an object from the dictionary
	 *
	 * @param key the name of the object
	 * @param object the object to remove
	 */
	public void remove(String key, T object) {
		if (!entries.containsKey(key))
			return;

		entries.get(key).remove(object);
		locations.get(object).remove(key);

		removeEventListeners.publish(new RemoveEvent<>(key, object));
	}

	/**
	 * Get an object set from the dictionary.
	 *
	 * @param name the dictionary name.
	 * @return the list of objects.
	 */
	public Set<T> get(String name) {
		if (!entries.containsKey(name)) {
			entries.put(name, new HashSet<>());
		}

		return Collections.unmodifiableSet(entries.get(name));
	}

	/**
	 * Find the names of a given object.
	 *
	 * @param object the object to find.
	 * @return the list of names this object is identified by
	 */
	public Set<String> find(T object) {
		if (!locations.containsKey(object)) {
			locations.put(object, new HashSet<>());
		}

		return Collections.unmodifiableSet(locations.get(object));
	}

	/**
	 * @return a {@link java.util.Set Set} of the names in this dictionary.
	 */
	public Set<String> keys() {
		return entries.keySet();
	}

	public EventListenerHandle<AddEvent<T>> whenEntryAdded(EventListener<AddEvent<T>> listener) {
		return addEventListeners.on().bind(listener);
	}

	public EventListenerHandle<RemoveEvent<T>> whenEntryRemoved(EventListener<RemoveEvent<T>> listener) {
		return removeEventListeners.on().bind(listener);
	}

	public static class AddEvent<T> {
		public final String key;
		public final T value;

		public AddEvent(String key, T value) {
			this.key = key;
			this.value = value;
		}
	}

	public static class RemoveEvent<T> {
		public final String key;
		public final T value;

		public RemoveEvent(String key, T value) {
			this.key = key;
			this.value = value;
		}
	}
}
