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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.component.inventory.Inventory;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;

import java.util.Optional;

public class BWInventory implements Inventory {
	public final IInventory wrapped;

	public BWInventory(IInventory mcInventory) {
		this.wrapped = mcInventory;
	}

	@Override
	public Optional<Item> get(int slot) {
		return Optional.ofNullable(wrapped.getStackInSlot(slot)).map(ItemConverter.instance()::toNova);
	}

	@Override
	public boolean set(int slot, Item item) {
		Optional<Item> orig = get(slot);
		wrapped.setInventorySlotContents(slot, ItemConverter.instance().toNative(item));
		return !orig.equals(get(slot));
	}

	@Override
	public Optional<Item> remove(int slot) {
		Optional<Item> item = get(slot);
		wrapped.setInventorySlotContents(slot, null);
		return item;
	}

	@Override
	public Optional<Item> remove(int slot, int amount) {
		ItemStack stack = wrapped.getStackInSlot(slot);
		if (stack != null) {
			Item item = ItemConverter.instance().toNova(stack);
			item.setCount(item.count() - amount);
			if (item.count() <= 0) {
				return remove(slot);
			}
			ItemConverter.instance().updateMCItemStack(stack, item);
			return Optional.of(item.withAmount(amount));
		}
		return Optional.empty();
	}

	@Override
	public int size() {
		return wrapped.getSizeInventory();
	}

	@Override
	public void markChanged() {
		wrapped.markDirty();
	}
}
