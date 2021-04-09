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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import nova.core.component.inventory.Inventory;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;

public class FWInventory implements IInventory {

	public Inventory wrapped;

	public FWInventory(Inventory inventory) {
		this.wrapped = inventory;
	}

	@Override
	public int getSizeInventory() {
		return wrapped.size();
	}

	@Override
    public boolean isEmpty() {
		int i = 0;
		for (Item item : wrapped) {
			i++;
		}
		return i == 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemConverter.instance().toNative(wrapped.get(slot).orElse(null));
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		ItemStack ret = stack.copy();
		ret.setCount(Math.min(ret.getCount(), amount));
		stack.setCount(stack.getCount() - ret.getCount());
		if (stack.getCount() <= 0) {
			setInventorySlotContents(slot, null);
			return ItemStack.EMPTY;
		}
		markDirty();
		return ret;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		ItemStack stack = getStackInSlot(slot);
		decrStackSize(slot, stack.getCount());
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		wrapped.set(slot, stack != null ? ItemConverter.instance().getNovaItem(stack) : null);
	}

	@Override
	public String getName() {
		// TODO Shouldn't be empty?
		return "";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		wrapped.markChanged();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {

	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.wrapped = null;
	}
}
