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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

class InventoryIterator implements Iterator<Item> {
	private final Inventory inv;
	private int nextPos;
	private int currentPos;
	private Item next = null;
	private boolean initialized = false;

	InventoryIterator(Inventory inv) {
		this.inv = Objects.requireNonNull(inv, "`inv` can't be null");
		findNext();
	}

	private void findNext() {
		while (nextPos < inv.size()) {
			currentPos = nextPos;
			Optional<Item> o = inv.get(nextPos++);
			if (o.isPresent()) {
				next = o.get();
				return;
			}
		}
		next = null;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public Item next() throws NoSuchElementException {
		if (next == null) {
			throw new NoSuchElementException();
		}
		initialized = true;
		Item current = next;
		findNext();
		return current;
	}

	@Override
	public void remove() {
		if (!initialized || !inv.remove(currentPos).isPresent()) {
			throw new IllegalStateException();
		}
	}
}
