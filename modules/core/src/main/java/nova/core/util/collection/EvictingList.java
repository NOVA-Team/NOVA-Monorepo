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
 */package nova.core.util.collection;

import java.util.LinkedList;

/**
 * EvictingList is a LinkedList with a size limit, the oldest entries will be removed is the list is full
 *
 * @param <E> Type contained in the list
 */
public class EvictingList<E> extends LinkedList<E> {
	private static final long serialVersionUID = 1L;

	private final int limit;

	/**
	 * A new EvictingList with the specified size limit
	 *
	 * @param limit Max size for the list
	 */
	public EvictingList(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean add(E o) {
		boolean value = super.add(o);

		while (size() > limit) {
			super.remove();
		}

		return value;
	}

	/**
	 * Limit specifies how many elements can be contained in this list.
	 * @return maximum number of entries in this list.
	 */
	public int limit() {
		return limit;
	}

	/**
	 * Get the oldest entry in the list
	 *
	 * @return The oldest entry in the list
	 */
	public E getOldest() {
		return get(0);
	}

	/**
	 * Get the latest entry in the list
	 *
	 * @return The latest entry in the list
	 */
	public E getLastest() {
		return get(size() - 1);
	}
}
