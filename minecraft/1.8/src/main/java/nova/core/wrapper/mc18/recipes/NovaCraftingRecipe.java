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
 */package nova.core.wrapper.mc18.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.internal.core.Game;

import java.util.Optional;

public class NovaCraftingRecipe implements IRecipe {
	private final CraftingRecipe recipe;

	public NovaCraftingRecipe(CraftingRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world) {
		return recipe.matches(MCCraftingGrid.get(inventoryCrafting));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
		Optional<Item> craftingResult = recipe.getCraftingResult(MCCraftingGrid.get(inventoryCrafting));
		if (craftingResult.isPresent()) {
			return Game.natives().toNative(craftingResult.get());
		} else {
			return null;
		}
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack getRecipeOutput() {
		Optional<Item> nominalOutput = recipe.getNominalOutput();
		if (nominalOutput.isPresent()) {
			return Game.natives().toNative(nominalOutput.get());
		}
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return new ItemStack[0];
	}
}
