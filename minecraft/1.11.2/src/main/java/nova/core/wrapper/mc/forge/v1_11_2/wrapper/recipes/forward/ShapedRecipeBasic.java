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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import nova.core.recipes.crafting.ShapedCraftingRecipe;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.backward.MCCraftingGrid;

/**
 * @author Stan Hebben
 */
public class ShapedRecipeBasic extends ShapedRecipes {
	private final ShapedCraftingRecipe recipe;

	public ShapedRecipeBasic(ItemStack[] basicInputs, ShapedCraftingRecipe recipe) {
		super(recipe.getWidth(), recipe.getHeight(), basicInputs, recipe.getExampleOutput().map(ItemConverter.instance()::toNative).orElse(ItemStack.EMPTY));
		this.recipe = recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inventory, World world) {
		return recipe.matches(MCCraftingGrid.get(inventory));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory) {
		return recipe.getCraftingResult(MCCraftingGrid.get(inventory)).map(ItemConverter.instance()::toNative).orElse(ItemStack.EMPTY);
	}
}
