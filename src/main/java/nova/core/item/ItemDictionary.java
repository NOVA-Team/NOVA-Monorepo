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

package nova.core.item;

import nova.core.util.Dictionary;

/**
 * @author Stan Hebben
 */
public class ItemDictionary extends Dictionary<Item> {

	/**
	 * Add an object to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param factory the item to register.
	 */
	public void add(String key, ItemFactory factory) {
		add(key, factory.build());
	}

	/**
	 * Add multiple objects to the dictionary.
	 *
	 * @param key the name of the object.
	 * @param factories the items to register.
	 */
	@SuppressWarnings("unchecked")
	public void add(String key, ItemFactory... factories) {
		for (ItemFactory factory : factories) {
			add(key, factory);
		}
	}

	/**
	 * Removes an object from the dictionary.
	 *
	 * @param key the name of the object.
	 * @param factory the item to remove.
	 */
	public void remove(String key, ItemFactory factory) {
		remove(key, factory.build());
	}

	/**
	 * Removes multiple objects from the dictionary.
	 *
	 * @param key the name of the object.
	 * @param factories the items to remove.
	 */
	@SuppressWarnings("unchecked")
	public void remove(String key, ItemFactory... factories) {
		for (ItemFactory factory : factories) {
			remove(key, factory);
		}
	}
}
