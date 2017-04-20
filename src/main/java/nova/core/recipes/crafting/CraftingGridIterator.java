/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */
package nova.core.recipes.crafting;

import nova.core.item.Item;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author ExE Boss
 */
public class CraftingGridIterator implements Iterator<Item> {
	private final CraftingGrid grid;
	private int nextPos;
	private int currentPos;
	private Item next = null;

	CraftingGridIterator(CraftingGrid grid) {
		this.grid = grid;
		findNext();
	}

	private void findNext() {
		while (nextPos < grid.size()) {
			currentPos = nextPos;
			Optional<Item> o = grid.getCrafting(nextPos++);
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
	public Item next() {
		Item current = next;
		findNext();
		return current;
	}

	@Override
	public void remove() {
		grid.setCrafting(currentPos, Optional.empty());
	}
}
