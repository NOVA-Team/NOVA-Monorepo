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

import nova.core.item.Item;
import nova.core.recipes.Recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Contains a single crafting recipe, no matter its crafting logic.
 *
 * @author Stan Hebben
 */
public interface CraftingRecipe extends Recipe {
	/**
	 * Checks if this crafting recipe matches the content of the given crafting
	 * grid.
	 *
	 * @param craftingGrid crafting grid to read from
	 * @return true if a crafting operation would return a valid result
	 */
	boolean matches(CraftingGrid craftingGrid);

	/**
	 * Calculates the crafting result for the given crafting grid. Does not
	 * modify the contents of the crafting grid.
	 *
	 * @param craftingGrid crafting grid
	 * @return crafting result, empty if the recipe doesn't match
	 */
	Optional<Item> getCraftingResult(CraftingGrid craftingGrid);

	/**
	 * Gets a nominal (example) output for this recipe. Used in recipe display.
	 *
	 * @return example output
	 */
	Optional<Item> getExampleOutput();

	/**
	 * Consumes items for the crafting of a single item. Removes items and
	 * applies any necessary modifications on the stacks in the given crafting
	 * grid, and gives back any items if necessary.
	 *
	 * @param craftingGrid crafting grid to modify
	 */
	void consumeItems(CraftingGrid craftingGrid);

	/**
	 * Gets the possible items for this recipe in the first non-empty recipe
	 * slot. Used to index recipes and can return empty if the recipe is
	 * calculated dynamically.
	 *
	 * @return The items
	 */
	default Collection<String> getPossibleItemsInFirstSlot() {
		return Collections.emptyList();
	}
}
