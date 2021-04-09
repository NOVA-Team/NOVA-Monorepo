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

package nova.core.recipes.crafting;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import nova.core.event.RecipeEvent;
import nova.core.item.Item;
import nova.core.recipes.RecipeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Manages crafting recipes and has functions to efficiently lookup a crafting
 * recipe.
 *
 * @author Stan Hebben
 */
public class CraftingRecipeManager {
	private final RecipeManager recipeManager;
	private final List<CraftingRecipe> dynamicRecipes;
	private final Multimap<String, CraftingRecipe> staticRecipes;

	public CraftingRecipeManager(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		this.dynamicRecipes = new ArrayList<>();
		this.staticRecipes = ArrayListMultimap.create();

		recipeManager.whenRecipeAdded(CraftingRecipe.class, this::onCraftingRecipeAdded);
		recipeManager.whenRecipeRemoved(CraftingRecipe.class, this::onCraftingRecipeRemoved);
	}

	/**
	 * Adds a recipe. Adds it to the global recipe list as CraftingRecipe.
	 *
	 * @param recipe {@link CraftingRecipe}
	 */
	public void addRecipe(CraftingRecipe recipe) {
		recipeManager.addRecipe(recipe);
	}

	/**
	 * Removes a recipe. Removes it from the global recipe list.
	 *
	 * @param recipe {@link CraftingRecipe}
	 */
	public void removeRecipe(CraftingRecipe recipe) {
		recipeManager.removeRecipe(recipe);
	}

	/**
	 * Gets the recipe that matches the given crafting grid.
	 *
	 * @param grid crafting grid
	 * @return matching crafting recipe, if any
	 */
	public Optional<CraftingRecipe> getRecipe(CraftingGrid grid) {
		for (CraftingRecipe dynamicRecipe : dynamicRecipes) {
			if (dynamicRecipe.matches(grid)) {
				return Optional.of(dynamicRecipe);
			}
		}

		Optional<Item> firstItem = grid.getFirstNonEmptyItem();
		if (!firstItem.isPresent()) {
			return Optional.empty();
		}

		String firstItemId = firstItem.get().getID();
		if (!staticRecipes.containsKey(firstItemId)) {
			return Optional.empty();
		}

		for (CraftingRecipe staticRecipe : staticRecipes.get(firstItemId)) {
			if (staticRecipe.matches(grid)) {
				return Optional.of(staticRecipe);
			}
		}

		return Optional.empty();
	}

	// #######################
	// ### Private Methods ###
	// #######################

	private <T extends CraftingRecipe> void onCraftingRecipeAdded(RecipeEvent.Add<T> evt) {
		Collection<String> possibleFirstItemIds = evt.recipe.getPossibleItemsInFirstSlot();
		if (!possibleFirstItemIds.isEmpty()) {
			for (String itemId : possibleFirstItemIds) {
				staticRecipes.put(itemId, evt.recipe);
			}
		} else {
			dynamicRecipes.add(evt.recipe);
		}
	}

	private <T extends CraftingRecipe> void onCraftingRecipeRemoved(RecipeEvent.Remove<T> evt) {
		Collection<String> possibleFirstItemIds = evt.recipe.getPossibleItemsInFirstSlot();
		if (!possibleFirstItemIds.isEmpty()) {
			for (String itemId : possibleFirstItemIds) {
				staticRecipes.remove(itemId, evt.recipe);
			}
		} else {
			dynamicRecipes.remove(evt.recipe);
		}
	}
}
