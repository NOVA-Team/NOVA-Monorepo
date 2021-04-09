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

package nova.core.wrapper.mc.forge.v1_8.wrapper.recipes.backward;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import nova.core.entity.component.Player;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.wrapper.mc.forge.v1_8.util.ReflectionUtil;
import nova.core.wrapper.mc.forge.v1_8.util.WrapUtility;
import nova.core.wrapper.mc.forge.v1_8.wrapper.item.ItemConverter;

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
	private nova.core.item.Item[] items;
	private ItemStack[] original;
	private int itemCount;

	private MCCraftingGrid(InventoryCrafting inventory) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		items = new nova.core.item.Item[width * height];
		original = new ItemStack[items.length];
		itemCount = 0;
		update();

		Container container = ReflectionUtil.getCraftingContainer(inventory);
		if (container != null) {
			@SuppressWarnings("unchecked")
			List<Slot> slots = container.inventorySlots;

			EntityPlayer playerOrig = null;
			Optional<Player> player = Optional.empty();

			for (Slot slot : slots) {
				if (slot instanceof SlotCrafting) {
					playerOrig = ReflectionUtil.getCraftingSlotPlayer((SlotCrafting) slot);
					player = WrapUtility.getNovaPlayer(playerOrig);

					if (player.isPresent()) {
						break;
					}
				}
			}

			this.playerOrig = playerOrig;
			this.player = player;
		} else {
			playerOrig = null;
			player = Optional.empty();
		}
	}

	private MCCraftingGrid(IInventory inventory, EntityPlayer player) {
		this.inventory = inventory;
		width = height = (int) Math.sqrt(inventory.getSizeInventory());
		items = new nova.core.item.Item[width * height];
		original = new ItemStack[items.length];
		itemCount = 0;
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
			items = new nova.core.item.Item[inventory.getSizeInventory()];
			original = new ItemStack[items.length];
			itemCount = 0;
		}

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (changed(i)) {
				//Game.logger().info("Slot {} changed", i);
				original[i] = inventory.getStackInSlot(i);
				if (inventory.getStackInSlot(i) != null) {
					if (items[i] == null) {
						itemCount++;
					}

					items[i] = ItemConverter.instance().toNova(original[i]);
				} else {
					if (items[i] != null) {
						itemCount--;
					}

					items[i] = null;
				}
			}
		}
		//Game.logger().info("Num stack count: {}", itemCount);
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
		return itemCount;
	}

	@Override
	public Optional<nova.core.item.Item> getCrafting(int i) {
		return Optional.ofNullable(items[i]);
	}

	@Override
	public Optional<nova.core.item.Item> getCrafting(int x, int y) {
		return Optional.ofNullable(items[y * width + x]);
	}

	@Override
	public boolean setCrafting(int x, int y, Optional<nova.core.item.Item> item) {
		//Game.logger().info("setCrafting({}, {}) {}", x, y, item);

		int ix = y * width + x;
		if (item.isPresent()) {
			if (!item.get().equals(items[ix])) {
				inventory.setInventorySlotContents(ix, ItemConverter.instance().toNative(item.get()));

				if (items[ix] == null) {
					itemCount++;
				}

				items[ix] = item.get();
			}
		} else {
			itemCount--;
			inventory.setInventorySlotContents(ix, null);
			items[ix] = null;
		}

		return true;
	}

	@Override
	public boolean setCrafting(int i, Optional<nova.core.item.Item> item) {
		//Game.logger().info("setCrafting({}) {}", i, item);

		if (item.isPresent()) {
			if (items[i] == null) {
				itemCount++;
			}

			inventory.setInventorySlotContents(i, ItemConverter.instance().toNative(item.get()));
			items[i] = item.get();
		} else {
			if (items[i] == null) {
				return true;
			}

			itemCount--;
			inventory.setInventorySlotContents(i, null);
			items[i] = null;
		}

		return true;
	}

	@Override
	public void giveBack(nova.core.item.Item item) {
		if (playerOrig != null) {
			playerOrig.inventory.addItemStackToInventory(ItemConverter.instance().toNative(item));
		}
	}

	@Override
	public String getTopology() {
		return CraftingGrid.TOPOLOGY_SQUARE;
	}

	@Override
	public String getType() {
		return CraftingGrid.TYPE_CRAFTING;
	}

	private boolean changed(int i) {
		if (original[i] != inventory.getStackInSlot(i)) {
			return true;
		}

		if (original[i] != null && items[i].count() != original[i].stackSize) {
			return true;
		}

		return false;
	}
}
