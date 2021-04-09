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

import nova.core.event.DictionaryEvent;
import nova.core.event.bus.EventBus;
import nova.core.event.bus.EventListener;
import nova.core.event.bus.EventListenerHandle;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * A dictionary where each identifying string represents a set of objects
 * and each object can have a set of identifiers.
 *
 * @param <T> The object type
 */
public class Dictionary<T> {
	private final Map<String, Set<T>> entries = new ConcurrentHashMap<>();
	private final Map<T, Set<String>> locations = new ConcurrentHashMap<>();
	private final EventBus<DictionaryEvent<T>> events = new EventBus<>();

	/**
	 * Add an object to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param object the object to register.
	 */
	public void add(String key, T object) {
		// TODO: Enforce name to be in camelCase
		if (!entries.containsKey(key)) {
			entries.put(key, ConcurrentHashMap.newKeySet());
		}

		entries.get(key).add(object);

		if (!locations.containsKey(object)) {
			locations.put(object, ConcurrentHashMap.newKeySet());
		}

		locations.get(object).add(key);

		events.publish(new DictionaryEvent.Add<>(key, object));
	}

	/**
	 * Add multiple objects to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param objects the objects to register.
	 */
	@SuppressWarnings("unchecked")
	public void add(String key, T... objects) {
		for (T object : objects) {
			add(key, object);
		}
	}

	/**
	 * Removes an object from the dictionary.
	 *
	 * @param key the name of the object.
	 * @param object the object to remove.
	 */
	public void remove(String key, T object) {
		if (!entries.containsKey(key))
			return;

		entries.get(key).remove(object);
		locations.get(object).remove(key);

		events.publish(new DictionaryEvent.Remove<>(key, object));
	}

	/**
	 * Removes multiple objects from the dictionary.
	 *
	 * @param key the name of the object.
	 * @param objects the objects to remove.
	 */
	@SuppressWarnings("unchecked")
	public void remove(String key, T... objects) {
		for (T object : objects) {
			remove(key, object);
		}
	}

	/**
	 * Removes all objects from the dictionary registered for the key.
	 *
	 * @param key the name of the objects
	 */
	public void removeAll(String key) {
		if (!entries.containsKey(key))
			return;

		Set<T> objects = entries.get(key);
		objects.stream().forEach(object -> {
			locations.get(object).remove(key);
			events.publish(new DictionaryEvent.Remove<>(key, object));
		});
	}

	/**
	 * Get an object set from the dictionary.
	 *
	 * @param name the dictionary name.
	 * @return the list of objects.
	 */
	public Set<T> get(String name) {
		if (!entries.containsKey(name))
			entries.put(name, ConcurrentHashMap.newKeySet());

		return Collections.unmodifiableSet(entries.get(name));
	}

	/**
	 * Find the names of a given object.
	 *
	 * @param object the object to find.
	 * @return the list of names this object is identified by
	 */
	public Set<String> find(T object) {
		if (!locations.containsKey(object))
			locations.put(object, ConcurrentHashMap.newKeySet());

		return Collections.unmodifiableSet(locations.get(object));
	}

	/**
	 * Gets a {@link java.util.Set Set} view of the names in this dictionary.
	 * This view is backed by the dictionary, but it cannot be used to modify the dictionary.
	 * To do that use either {@link #remove(java.lang.String, java.lang.Object) remove(String, T)}
	 * or {@link #removeAll(java.lang.String) removeAll(String)}.
	 *
	 * @return a {@link java.util.Set Set} view of the names in this dictionary.
	 */
	public Set<String> keys() {
		return Collections.unmodifiableSet(entries.keySet());
	}

	public void forEach(BiConsumer<? super String, ? super Set<T>> action) {
		entries.forEach(action);
	}

	public Stream<Map.Entry<String, Set<T>>> stream() {
		return entries.entrySet().stream();
	}

	public Stream<Map.Entry<String, Set<T>>> parallelStream() {
		return entries.entrySet().parallelStream();
	}

	@SuppressWarnings("unchecked")
	public EventListenerHandle<DictionaryEvent.Add<T>> whenEntryAdded(EventListener<DictionaryEvent.Add<T>> listener) {
		return events.on(DictionaryEvent.Add.class).bind(listener);
	}

	@SuppressWarnings("unchecked")
	public EventListenerHandle<DictionaryEvent.Remove<T>> whenEntryRemoved(EventListener<DictionaryEvent.Remove<T>> listener) {
		return events.on(DictionaryEvent.Remove.class).bind(listener);
	}
}
