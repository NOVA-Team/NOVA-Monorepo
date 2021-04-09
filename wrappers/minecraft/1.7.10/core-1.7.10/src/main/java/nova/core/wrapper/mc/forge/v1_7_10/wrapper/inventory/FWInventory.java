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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import nova.core.component.inventory.Inventory;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.item.ItemConverter;

public class FWInventory implements IInventory {

	public final Inventory wrapped;

	public FWInventory(Inventory inventory) {
		this.wrapped = inventory;
	}

	@Override
	public int getSizeInventory() {
		return wrapped.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemConverter.instance().toNative(wrapped.get(slot).orElse(null));
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		ItemStack ret = stack.copy();
		ret.stackSize = Math.min(ret.stackSize, amount);
		stack.stackSize -= ret.stackSize;
		if (stack.stackSize <= 0) {
			setInventorySlotContents(slot, null);
			return null;
		}
		markDirty();
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return getStackInSlot(slot);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		wrapped.set(slot, stack != null ? ItemConverter.instance().getNovaItem(stack) : null);
	}

	@Override
	public String getInventoryName() {
		// TODO Shouldn't be null
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
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
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
	}

}
