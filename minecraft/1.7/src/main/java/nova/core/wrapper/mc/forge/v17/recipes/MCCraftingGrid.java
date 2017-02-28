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

package nova.core.wrapper.mc.forge.v17.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import nova.core.entity.component.Player;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.wrapper.mc.forge.v17.util.ReflectionUtil;
import nova.core.wrapper.mc.forge.v17.util.WrapUtility;
import nova.core.wrapper.mc.forge.v17.wrapper.item.ItemConverter;
import nova.internal.core.Game;

import java.util.List;
import java.util.Optional;

/**
 * @author Stan Hebben
 */
public class MCCraftingGrid implements CraftingGrid {
	private static final ThreadLocal<MCCraftingGrid> cache = new ThreadLocal<MCCraftingGrid>();
	private static final ThreadLocal<MCCraftingGrid> cache2 = new ThreadLocal<MCCraftingGrid>();
	private final IInventory inventory;
	private final Optional<Player> player;
	private final EntityPlayer playerOrig;
	private int width;
	private int height;
	private nova.core.item.Item[] stacks;
	private net.minecraft.item.ItemStack[] original;
	private int numberOfStacks;

	private MCCraftingGrid(InventoryCrafting inventory) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		stacks = new nova.core.item.Item[width * height];
		original = new net.minecraft.item.ItemStack[stacks.length];
		numberOfStacks = 0;
		update();

		Container container = ReflectionUtil.getCraftingContainer(inventory);
		if (container != null) {
			@SuppressWarnings("unchecked")
			List<Slot> slots = container.inventorySlots;
			if (!slots.isEmpty() && slots.get(0) instanceof SlotCrafting) {
				SlotCrafting slotCrafting = (SlotCrafting) slots.get(0);
				playerOrig = ReflectionUtil.getCraftingSlotPlayer(slotCrafting);
				player = WrapUtility.getNovaPlayer(playerOrig);
			} else {
				playerOrig = null;
				player = Optional.empty();
			}
		} else {
			playerOrig = null;
			player = Optional.empty();
		}
	}

	private MCCraftingGrid(IInventory inventory, EntityPlayer player) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		stacks = new nova.core.item.Item[width * height];
		original = new ItemStack[stacks.length];
		numberOfStacks = 0;
		update();

		playerOrig = player;
		this.player = WrapUtility.getNovaPlayer(player);
	}

	public static MCCraftingGrid get(InventoryCrafting inventory) {
		if (cache.get() == null || cache.get().inventory != inventory) {
			MCCraftingGrid result = new MCCraftingGrid(inventory);
			cache.set(result);
			return result;
		} else {
			MCCraftingGrid result = cache.get();
			result.update();
			return result;
		}
	}

	public static MCCraftingGrid get(IInventory inventory, EntityPlayer player) {
		if (cache2.get() == null || cache2.get().inventory != inventory || cache2.get().playerOrig != player) {
			MCCraftingGrid result = new MCCraftingGrid(inventory, player);
			cache2.set(result);
			return result;
		} else {
			MCCraftingGrid result = cache2.get();
			result.update();
			return result;
		}
	}

	private void update() {
		if (inventory.getSizeInventory() != original.length) {
			width = height = (int) Math.sqrt(inventory.getSizeInventory());
			stacks = new nova.core.item.Item[inventory.getSizeInventory()];
			original = new ItemStack[stacks.length];
			numberOfStacks = 0;
		}

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (changed(i)) {
				//System.out.println("Slot " + i + " changed");
				original[i] = inventory.getStackInSlot(i);
				if (inventory.getStackInSlot(i) != null) {
					if (stacks[i] == null) {
						numberOfStacks++;
					}

					stacks[i] = Game.natives().toNova(original[i]);
				} else {
					if (stacks[i] != null) {
						numberOfStacks--;
					}

					stacks[i] = null;
				}
			}
		}
		//System.out.println("Num stack count: " + numberOfStacks);
	}

	@Override
	public Optional<Player> getPlayer() {
		return player;
	}

	@Override
	public int size() {
		return width * height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int countFilledStacks() {
		return numberOfStacks;
	}

	@Override
	public Optional<nova.core.item.Item> getStack(int i) {
		return Optional.ofNullable(stacks[i]);
	}

	@Override
	public Optional<nova.core.item.Item> getStack(int x, int y) {
		return Optional.ofNullable(stacks[y * width + x]);
	}

	@Override
	public boolean setStack(int x, int y, Optional<nova.core.item.Item> stack) {
		//System.out.println("SetStack(" + x + ", " + y + ") " + stack);

		int ix = y * width + x;
		if (stack.isPresent()) {
			if (!stack.get().equals(stacks[ix])) {
				inventory.setInventorySlotContents(ix, Game.natives().toNative(stack.get()));

				if (stacks[ix] == null) {
					numberOfStacks++;
				}

				stacks[ix] = stack.get();
			}
		} else {
			numberOfStacks--;
			inventory.setInventorySlotContents(ix, null);
			stacks[ix] = null;
		}

		return true;
	}

	@Override
	public boolean setStack(int i, Optional<nova.core.item.Item> stack) {
		//System.out.println("SetStack(" + i + ") " + stack);

		if (stack.isPresent()) {
			if (stacks[i] == null) {
				numberOfStacks++;
			}

			inventory.setInventorySlotContents(i, Game.natives().toNative(stack.get()));
			stacks[i] = stack.get();
		} else {
			if (stacks[i] == null) {
				return true;
			}

			numberOfStacks--;
			inventory.setInventorySlotContents(i, null);
			stacks[i] = null;
		}

		return true;
	}

	@Override
	public void giveBack(nova.core.item.Item itemStack) {
		playerOrig.inventory.addItemStackToInventory(ItemConverter.instance().toNative(itemStack));
	}

	@Override
	public String getTopology() {
		return CraftingGrid.topologySquare;
	}

	@Override
	public String getType() {
		return CraftingGrid.typeCrafting;
	}

	private boolean changed(int i) {
		if (original[i] != inventory.getStackInSlot(i)) {
			return true;
		}

		if (original[i] != null && stacks[i].count() != original[i].stackSize) {
			return true;
		}

		return false;
	}
}
