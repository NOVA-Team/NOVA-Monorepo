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

package nova.core.wrapper.mc.forge.v1_11_2.recipes;

import net.minecraft.item.crafting.IRecipe;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * @author Stan Hebben
 */
public class MCCraftingRecipe implements CraftingRecipe {
	private final IRecipe recipe;

	public MCCraftingRecipe(IRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean matches(CraftingGrid craftingGrid) {
		// TODO: supply world somehow?
		return recipe.matches(new NovaCraftingGrid(craftingGrid), null);
	}

	@Override
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid) {
		return Game.natives().toNova(recipe.getCraftingResult(new NovaCraftingGrid(craftingGrid)));
	}

	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
		// not supported
	}

	@Override
	public Optional<Item> getNominalOutput() {
		return Game.natives().toNova(recipe.getRecipeOutput());
	}
}
