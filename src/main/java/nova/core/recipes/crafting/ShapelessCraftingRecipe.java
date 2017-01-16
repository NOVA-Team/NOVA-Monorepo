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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Stan
 */
public class ShapelessCraftingRecipe implements CraftingRecipe {
	private final Item nominalOutput;
	private final RecipeFunction recipeFunction;
	private final ItemIngredient[] ingredients;

	public ShapelessCraftingRecipe(Item output, ItemIngredient[] ingredients) {
		this(output, (crafting, tagged) -> Optional.of(output), ingredients);
	}

	public ShapelessCraftingRecipe(Item nominalOutput, RecipeFunction recipeFunction, ItemIngredient[] ingredients) {
		this.nominalOutput = nominalOutput;
		this.recipeFunction = recipeFunction;
		this.ingredients = ingredients;
	}

	/**
	 * Reorders ingredients to match the given shapeless recipe.
	 *
	 * @param recipe shapeless recipe
	 * @param craftingGrid crafting input
	 * @return reordered inputs, or null if no match was found
	 */
	private static RecipeMatching matchShapeless(
		ItemIngredient[] recipe,
		CraftingGrid craftingGrid) {
		if (craftingGrid.countFilledStacks() != recipe.length) {
			return null;
		}

		Item[] matched = new Item[recipe.length];
		int[] indices = new int[recipe.length];

		outer:
		for (int i = 0; i < craftingGrid.size(); i++) {
			Optional<Item> ingredient = craftingGrid.getStack(i);
			if (!ingredient.isPresent()) {
				continue;
			}

			for (int j = 0; j < recipe.length; j++) {
				if (matched[j] != null) {
					continue;
				}

				if (recipe[j].matches(ingredient.get())) {
					matched[j] = ingredient.get();
					indices[j] = i;
					continue outer;
				}
			}

			return null;
		}

		return new RecipeMatching(matched, indices);
	}

	public int size() {
		return ingredients.length;
	}

	public ItemIngredient[] getIngredients() {
		return ingredients;
	}

	@Override
	public boolean matches(CraftingGrid craftingGrid) {
		return matchShapeless(ingredients, craftingGrid) != null;
	}

	@Override
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid) {
		RecipeMatching matching = matchShapeless(ingredients, craftingGrid);

		Map<String, Item> map = new HashMap<>();
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i].getTag().isPresent()) {
				map.put(ingredients[i].getTag().get(), matching.inputs[i]);
			}
		}

		return recipeFunction.doCrafting(craftingGrid, map);
	}

	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
		RecipeMatching matching = matchShapeless(ingredients, craftingGrid);

		for (int i = 0; i < ingredients.length; i++) {
			ItemIngredient ingredient = ingredients[i];
			Item consumed = ingredient.consumeOnCrafting(matching.inputs[i], craftingGrid);
			Objects.requireNonNull(consumed, "The result of 'ItemIngredient.consumeOnCrafting' can't be null");
			craftingGrid.setStack(matching.indices[i], Optional.ofNullable(consumed));
		}
	}

	@Override
	public Optional<Item> getNominalOutput() {
		return Optional.of(nominalOutput);
	}

	private static class RecipeMatching {
		public final Item[] inputs;
		public final int[] indices;

		private RecipeMatching(Item[] inputs, int[] indices) {
			this.inputs = inputs;
			this.indices = indices;
		}
	}
}
