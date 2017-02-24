/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.recipes.smelting;

import nova.core.event.RecipeEvent;
import nova.core.recipes.RecipeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ExE Boss
 */
public class SmeltingRecipeManager {
	private final RecipeManager recipeManager;
	private final List<SmeltingRecipe> recipes;

	public SmeltingRecipeManager(RecipeManager recipeManager) {
		this.recipeManager = recipeManager;
		this.recipes = new ArrayList<>();

		this.recipeManager.whenRecipeAdded(SmeltingRecipe.class, this::onSmeltingRecipeAdded);
		this.recipeManager.whenRecipeRemoved(SmeltingRecipe.class, this::onSmeltingRecipeRemoved);
	}

	/**
	 * Adds a recipe. Adds it to the global recipe list as SmeltingRecipe.
	 *
	 * @param recipe {@link SmeltingRecipe}
	 */
	public void addRecipe(SmeltingRecipe recipe) {
		recipeManager.addRecipe(recipe);
	}

	/**
	 * Removes a recipe. Removes it from the global recipe list.
	 *
	 * @param recipe {@link SmeltingRecipe}
	 */
	public void removeRecipe(SmeltingRecipe recipe) {
		recipeManager.removeRecipe(recipe);
	}

	// #######################
	// ### Private Methods ###
	// #######################

	private <T extends SmeltingRecipe> void onSmeltingRecipeAdded(RecipeEvent.Add<T> e) {
		recipes.add(e.recipe);
	}

	private <T extends SmeltingRecipe> void onSmeltingRecipeRemoved(RecipeEvent.Remove<T> e) {
		recipes.remove(e.recipe);
	}
}
