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
 */package nova.core.util.registry;

import com.google.common.collect.HashBiMap;
import nova.core.util.Identifiable;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * A registry of one type of identifiable object.
 *
 * @param <T> The object type
 */
public class Registry<T extends Identifiable> implements Iterable<T> {
	private final HashBiMap<String, T> objects = HashBiMap.create();

	public Registry() {

	}

	/**
	 * Registers an identifiable object in the registry.
	 *
	 * @param object the object to register.
	 * @return Given object
	 */
	public T register(T object) {
		objects.put(object.getID(), object);
		return object;
	}

	/**
	 * Tests whether a given ID is in the Registry.
	 *
	 * @param ID the id to find.
	 * @return true if the registry contains the object with the given ID.
	 */
	public boolean contains(String ID) {
		return objects.containsKey(ID);
	}

	/**
	 * Gets the object with the given id from the registry.
	 *
	 * @param ID the id to find.
	 * @return the object found or empty Optional if not found.
	 */
	public Optional<T> get(String ID) {
		return Optional.ofNullable(objects.get(ID));
	}

	/**
	 * Gets the name of a given object if contained in registry.
	 *
	 * @param object the object to find.
	 * @return the name of the object or empty Optional if not found.
	 */
	public Optional<String> getName(T object) {
		return Optional.ofNullable(objects.inverse().get(object));
	}

	/**
	 * @return an iterator on values of the registry.
	 */
	@Override
	public Iterator<T> iterator() {
		return objects.values().iterator();
	}

	public Spliterator<T> spliterator() {
		return objects.values().spliterator();
	}

	public Stream<T> stream() {
		return objects.values().stream();
	}
}
