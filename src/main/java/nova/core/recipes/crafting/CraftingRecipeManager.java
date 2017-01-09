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
 */package nova.core.recipes.crafting;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import nova.core.item.Item;
import nova.core.recipes.RecipeAddedEvent;
import nova.core.recipes.RecipeManager;
import nova.core.recipes.RecipeRemovedEvent;
import nova.core.util.id.Identifier;

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
	private final Multimap<Identifier, CraftingRecipe> staticRecipes;

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

	/*
	 * Removes a recipe. Removes if from the global recipe list.
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

		Identifier firstItemId = firstItem.get().getID();
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

	private <T extends CraftingRecipe> void onCraftingRecipeAdded(RecipeAddedEvent<T> e) {
		Optional<Collection<Identifier>> possibleFirstItemIds = e.getRecipe().getPossibleItemsInFirstSlot();
		if (possibleFirstItemIds.isPresent()) {
			for (Identifier itemId : possibleFirstItemIds.get()) {
				staticRecipes.put(itemId, e.getRecipe());
			}
		} else {
			dynamicRecipes.add(e.getRecipe());
		}
	}

	private <T extends CraftingRecipe> void onCraftingRecipeRemoved(RecipeRemovedEvent<T> e) {
		Optional<Collection<Identifier>> possibleFirstItemIds = e.getRecipe().getPossibleItemsInFirstSlot();
		if (possibleFirstItemIds.isPresent()) {
			for (Identifier itemId : possibleFirstItemIds.get()) {
				staticRecipes.remove(itemId, e.getRecipe());
			}
		} else {
			dynamicRecipes.remove(e.getRecipe());
		}
	}
}
