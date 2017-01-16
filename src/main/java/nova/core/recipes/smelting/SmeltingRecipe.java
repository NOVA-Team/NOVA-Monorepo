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

import nova.core.item.Item;
import nova.core.recipes.crafting.ItemIngredient;
import nova.core.recipes.Recipe;

import java.util.Optional;

/**
 * @author ExE Boss
 */
public interface SmeltingRecipe extends Recipe {
	/**
	 * Checks if this crafting recipe matches the content of the given smelting input.
	 *
	 * @param input smelting input to read from
	 * @return true if a smelting operation would return a valid result
	 */
	boolean matches(Optional<Item> input);

	/**
	 * Calculates the crafting result for the given crafting grid. Does not
	 * modify the contents of the crafting grid.
	 *
	 * @param input crafting grid
	 * @return crafting result, empty if the recipe doesn't match
	 */
	Optional<Item> getCraftingResult(Optional<Item> input);

	/**
	 * Gets a nominal (example) input for this recipe. Used in recipe display.
	 *
	 * @return example input
	 */
	Optional<Item> getNominalInput();

	/**
	 * Gets a nominal (example) output for this recipe. Used in recipe display.
	 *
	 * @return example output
	 */
	Optional<Item> getNominalOutput();

	/**
	 * Gets the input for this recipe. Used during registration.
	 *
	 * @return the input
	 */
	Optional<ItemIngredient> getInput();
}
