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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.backward;

import nova.core.wrapper.mc.forge.v1_11_2.wrapper.recipes.forward.NovaCraftingGrid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import nova.core.entity.Entity;
import nova.core.entity.component.Player;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;

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
		return recipe.matches(new NovaCraftingGrid(craftingGrid), (net.minecraft.world.World) craftingGrid.getPlayer().map(Player::entity)
			.map(Entity::world).map(WorldConverter.instance()::toNative).filter(w -> w instanceof net.minecraft.world.World).orElse(null));
	}

	@Override
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid) {
		return Optional.of(recipe.getCraftingResult(new NovaCraftingGrid(craftingGrid))).filter(item -> !item.isEmpty()).map(ItemConverter.instance()::toNova);
	}

	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
		NonNullList<ItemStack> remainder = recipe.getRemainingItems(new NovaCraftingGrid(craftingGrid));
		for (int i = 0; i < remainder.size(); i++) {
			Optional<Item> result = Optional.of(remainder.get(i)).filter(item -> !item.isEmpty()).map(ItemConverter.instance()::toNova);
			if (!result.isPresent()) {
				result = craftingGrid.getCrafting(i).filter(item -> item.count() > 1).map(item -> item.withAmount(item.count() - 1));
			}
			craftingGrid.setCrafting(i, result);
		}
	}

	@Override
	public Optional<Item> getExampleOutput() {
		return Optional.of(recipe.getRecipeOutput()).filter(item -> !item.isEmpty()).map(ItemConverter.instance()::toNova);
	}
}
