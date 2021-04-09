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
import nova.core.item.ItemFactory;
import nova.core.recipes.ingredient.ItemIngredient;

import java.util.Optional;

/**
 * @author ExE Boss
 */
public class BasicSmeltingRecipe implements SmeltingRecipe {

	private final ItemFactory output;
	private final ItemIngredient ingredient;

	/**
	 * Defines a basic structured crafting recipe, using a format string.
	 * @param output Output {@link Item} of the recipe
	 * @param ingredient {@link ItemIngredient}
	 */
	public BasicSmeltingRecipe(ItemFactory output, ItemIngredient ingredient) {
		this.output = output;
		this.ingredient = ingredient;
	}

	@Override
	public boolean matches(Item input) {
		return ingredient.matches(input);
	}

	@Override
	public Optional<Item> getCraftingResult(Item input) {
		return matches(input) ? Optional.of(output.build()) : Optional.empty();
	}

	@Override
	public Optional<Item> getExampleOutput() {
		return Optional.of(output.build());
	}

	@Override
	public Optional<ItemIngredient> getInput() {
		return Optional.of(ingredient);
	}
}
