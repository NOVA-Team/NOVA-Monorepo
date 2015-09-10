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

package nova.core.component.inventory;

import nova.core.item.Item;

import java.util.function.Predicate;

/**
 * A filter that only accepts a specific sub-type of {@link Item}. For use with slots.
 *
 * @author Vic Nightfall
 */
@FunctionalInterface
public interface ItemFilter extends Predicate<Item> {

	/**
	 * Returns an {@link ItemFilter} that accepts an {@link Item} of the same
	 * type as the provided.
	 *
	 * @param item
	 * @return ItemFilter
	 */
	static ItemFilter of(Item item) {
		return item::sameItemType;
	}

	/**
	 * Returns an {@link ItemFilter} that accepts an {@link Item} of the same
	 * type as provided.
	 *
	 * @param id
	 * @return ItemFilter
	 */
	static ItemFilter of(String id) {
		return (other) -> id.equals(other.getID());
	}

	/**
	 * Accepts any {@link Item} that has a &gt;= stack size than provided.
	 *
	 * @param amount
	 * @return
	 */
	static ItemFilter of(int amount) {
		return (other) -> other.count() >= amount;
	}
}
