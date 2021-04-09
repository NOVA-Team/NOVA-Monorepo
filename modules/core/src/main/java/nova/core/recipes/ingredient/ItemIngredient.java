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

package nova.core.recipes.ingredient;

import nova.core.block.BlockFactory;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.recipes.crafting.CraftingGrid;

import java.util.Collection;
import java.util.Optional;

/**
 * Contains a single item ingredient. An ingredient can be a specific item,
 * an ore dictionary entry, or anything else that can match against items.
 *
 * @author Stan Hebben
 */
public interface ItemIngredient {

	/**
	 * Retrieves an ingredient to represent a specific block.
	 *
	 * @param block The block
	 * @return ingredient
	 */
	static ItemIngredient forBlock(BlockFactory block) {
		return forItem(block.getID());
	}

	/**
	 * Retrieves an ingredient to represent a specific item.
	 *
	 * @param item The item
	 * @return ingredient
	 */
	static ItemIngredient forItem(ItemFactory item) {
		return forItem(item.getID());
	}

	/**
	 * Retrieves an ingredient to represent a specific item.
	 *
	 * @param itemId item ID
	 * @return ingredient
	 */
	static ItemIngredient forItem(String itemId) {
		return new SpecificItemIngredient(itemId);
	}

	/**
	 * Retrieves an ingredient to represent a dictionary entry.
	 *
	 * @param id dictionary entry ID
	 * @return ingredient
	 */
	static ItemIngredient forDictionary(String id) {
		return new OreItemIngredient(id);
	}

	/**
	 * Returns a list of all items that could possibly match this ingredient.
	 * Should return Optional.empty() if there is no such list.
	 *
	 * @return possible items
	 */
	Collection<String> getPossibleItemIds();

	/**
	 * Returns a list of example items. This list could be used to render
	 * ingredients when displayed to users.
	 *
	 * @return example items
	 */
	Collection<Item> getExampleItems();

	/**
	 * Checks if this ingredient is a subset of another ingredient. An
	 * ingredient is a subset if all items that match this ingredient also
	 * match the other ingredient.
	 *
	 * @param ingredient The another ingredient
	 * @return Result of the check
	 */
	boolean isSubsetOf(ItemIngredient ingredient);

	/**
	 * Checks if this ingredient matches the given item.
	 *
	 * @param item Item to check
	 * @return Whether the item matches or not
	 */
	boolean matches(Item item);

	/**
	 * Returns the ingredient tag. Ingredients can be tagged to make them easy to read from recipe functions.
	 *
	 * @return ingredient tag
	 */
	Optional<String> getTag();

	/**
	 * Sets the ingredient tag. Ingredients can be tagged to make them
	 * easy to read from recipe functions.
	 *
	 * @param tag The ingredient tag
	 * @return this instance
	 */
	public ItemIngredient withTag(String tag);

	/**
	 * Performs any necessary actions when this ingredient is consumed due to crafting a single item. May return a
	 * stack with zero size, but cannot return null.
	 *
	 * @param original ingredient
	 * @param craftingGrid {@link CraftingGrid} used
	 * @return Resulting {@link Item}, or an empty optional if the item was empty.
	 */
	Optional<Item> consumeOnCrafting(Item original, CraftingGrid craftingGrid);
}
