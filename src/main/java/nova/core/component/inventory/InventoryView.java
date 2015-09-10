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
 */package nova.core.component.inventory;

import nova.core.item.Item;

import java.util.Optional;

/**
 * This class is virtual inventory used for inventory re-mapping
 */
public class InventoryView implements Inventory {
	private final Inventory parent;
	private final int[] slots;

	/**
	 * Creates new inventory view
	 *
	 * @param parent Parent inventory
	 * @param slots Map of slots
	 */
	public InventoryView(Inventory parent, int[] slots) {
		this.parent = parent;
		this.slots = slots;
	}

	@Override
	public Optional<Item> get(int slot) {
		if (slot < 0 || slot >= slots.length) {
			return null;
		} else {
			return parent.get(slots[slot]);
		}
	}

	@Override
	public boolean set(int slot, Item stack) {
		if (slot < 0 || slot >= slots.length) {
			return false;
		} else {
			return parent.set(slots[slot], stack);
		}
	}

	@Override
	public Optional<Item> remove(int slot) {
		return parent.remove(slot);
	}

	@Override
	public int size() {
		return slots.length;
	}

	@Override
	public void markChanged() {
		parent.markChanged();
	}
}
