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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This interface provides inventory that can hold {@link Item Items}
 * @see InventorySimple
 * @see InventoryView
 */
public interface Inventory extends Iterable<Item> {

	/**
	 * @param slot Slot number
	 * @return The item in the slot or an {@link Optional#empty empty optional}
	 * if there is no item in the slot
	 * @throws IndexOutOfBoundsException if the slot is out of range
	 * (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	Optional<Item> get(int slot) throws IndexOutOfBoundsException;

	/**
	 * Sets {@link Item} in slot
	 * @param slot Slot number
	 * @param item Item to insert
	 * @return Whether the operation succeeded or not
	 * @throws IndexOutOfBoundsException if the slot is out of range
	 * (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	boolean set(int slot, Item item) throws IndexOutOfBoundsException;

	/**
	 * Gets count of slots
	 * @return Number of slots in this inventory
	 */
	int size();

	/**
	 * Tells this inventory that something has changed
	 */
	void markChanged();

	/**
	 * Adds items to this inventory at specified slot
	 * @param slot Slot to add items into
	 * @param item Item to add
	 * @return Amount of items left(did not fit inside this inventory)
	 * @throws IndexOutOfBoundsException if the slot is out of range
	 * (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	default Optional<Item> add(int slot, Item item) throws IndexOutOfBoundsException {
		Optional<Item> o = get(slot);
		if (item == null) {
			return Optional.empty();
		}
		if (o.isPresent()) {
			if (item.sameItemType(o.get())) {
				return Optional.of(item.withAmount(item.count() - o.get().addCount(item.count())));
			} else {
				return Optional.of(item);
			}
		} else {
			set(slot, item);
			return Optional.empty();
		}
	}

	/**
	 * Adds items to this inventory
	 * @param item {@link Item} containing items
	 * @return Amount of items left(did not fit inside this inventory)
	 */
	default Optional<Item> add(Item item) {
		if (item == null) {
			return Optional.empty();
		}
		int itemsLeft = item.count();
		for (int i = 0; i < size(); i++) {
			itemsLeft = add(i, item.withAmount(itemsLeft)).map(Item::count).orElse(0);
			if (itemsLeft == 0)
				break;
		}

		if (itemsLeft != item.count()) {
			markChanged();
		}

		final int il = itemsLeft;
		return Optional.of(item)
			.filter(i -> il > 0)
			.map(i -> i.withAmount(il));
	}

	/**
	 * Removes a one count of the item from a slot.
	 * @param slot The slot index to remove
	 * @return The items removed
	 * @throws IndexOutOfBoundsException if the slot is out of range
	 * (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	Optional<Item> remove(int slot) throws IndexOutOfBoundsException;

	/**
	 * Removes a certain amount of items from a slot.
	 * @param slot The slot index to remove
	 * @param amount The amount of items to remove
	 * @return The items removed
	 * @throws IndexOutOfBoundsException if the slot is out of range
	 * (<tt>index &lt; 0 || index &gt;= size()</tt>)
	 */
	default Optional<Item> remove(int slot, int amount) throws IndexOutOfBoundsException {
		Optional<Item> o = get(slot);
		if (o.isPresent()) {
			Item item = o.get();
			item.setCount(item.count() - amount);

			if (item.count() <= 0) {
				return remove(slot);
			}
			return Optional.of(item.withAmount(amount));
		}
		return Optional.empty();
	}

	/**
	 * Removes a certain item from a slot.
	 * @param item The item type to check with
	 * @return The items removed
	 */
	default Optional<Item> remove(Item item) {
		if (item == null) {
			return Optional.empty();
		}
		int left = item.count();
		for (int i = 0; i < size(); i++) {
			Optional<Item> opItem = get(i);

			if (opItem.isPresent() && item.sameItemType(opItem.get())) {
				Optional<Item> removed = remove(i, left);

				if (removed.isPresent()) {
					left -= removed.get().count();
				}
			}
		}

		int removed = item.count() - left;
		if (removed > 0) {
			return Optional.of(item.withAmount(removed));
		}
		return Optional.empty();
	}

	/**
	 * Represents this inventory as list of {@link Item Items}
	 * @return This inventory as list of {@link Item Items}
	 */
	default List<Item> toList() {
		List<Item> list = new ArrayList<>();
		for (Item i : this) {
			list.add(i);
		}
		return list;
	}

	default Set<Item> toSet() {
		Set<Item> list = new HashSet<>();
		for (Item i : this) {
			list.add(i);
		}
		return list;
	}

	@Override
	default Iterator<Item> iterator() {
		return new InventoryIterator(this);
	}

	@Override
	default Spliterator<Item> spliterator() {
		return Spliterators.spliterator(iterator(), size(), Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
	}

	/**
	 * Represents this inventory as an {@link Item} {@link Stream}
	 * @return This inventory as an {@link Item} {@link Stream}
	 */
	default Stream<Item> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
