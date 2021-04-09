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
import nova.core.item.ItemFactory;
import nova.core.recipes.ingredient.ItemIngredient;
import nova.core.util.math.MathUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Contains a single shaped crafting recipe. Can contain custom crafting logic.
 * <p>
 * Crafting recipes can be specified as a 2-dimensional array of ingredients (with Optional.empty() being used for empty spots)
 * or as a 1-dimensional array of ingredients and a pattern string (lines are separated by -, spaces for empty spots, A-Z for ingredients.
 * <p>
 * For instance, to define a stick recipe with a pattern string:
 * <p>
 * new ShapedCraftingRecipe("A - A", ItemIngredient.forDictionary("plankWood"));
 * <p>
 * Two kinds of recipes can be defined: basic or advanced. Basic recipes always return the same item, while advanced
 * recipes have their output defined by a lambda expression. RecipeFunctions will receive information about the
 * @author Stan Hebben
 */
public class ShapedCraftingRecipe implements CraftingRecipe {
	/*
	 * This class contains an optimized recipe resolution algorithm.
     *
     * This algorithms works as follows:
     *  A: During construction, the 2D ingredients array is transformed to a flat list of ingredients and their positions
     *  B: During recipe resolution, the first non-empty crafting grid spot is searched
     *  C: That position marks the position of the recipe inside the crafting grid (a 2x2 recipe has 4 possible positions in a 3x3 crafting grid)
     *  D: The list of ingredients is run over and filled into an array for later processing
     *  E: If any of the ingredients doesn't match, the crafting is rejected
     *  F: For mirrored recipes B-E are repeated in the mirrored direction
     */

	private final int width;
	private final int height;
	private final int[] posx;
	private final int[] posy;
	private final boolean mirrored;
	private final int lastIngredientIndexOnFirstLine; // only actually matters for mirrored recipes
	private final RecipeFunction recipeFunction;
	private final ItemFactory output;

	private final ItemIngredient[] ingredients;

	/**
	 * Defines a basic structured crafting recipe, using a format string.
	 * @param output Output {@link Item} of the recipe
	 * @param format Format
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, String format, ItemIngredient... ingredients) {
		this(output, format, false, ingredients);
	}

	/**
	 * Defines a basic structured crafting recipe, possibly mirrored, using a format string.
	 * @param output Output {@link Item} of the recipe
	 * @param format Format
	 * @param mirrored Whether this recipe is mirrored
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, String format, boolean mirrored, ItemIngredient... ingredients) {
		this(output, (grid, tagged, o) -> Optional.of(o.build()), format, mirrored, ingredients);
	}

	/**
	 * Defines an advanced crafting recipe, using a format string.
	 * @param output Nominal output of the recipe
	 * @param recipeFunction {@link RecipeFunction}
	 * @param format Format
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, RecipeFunction recipeFunction, String format, ItemIngredient... ingredients) {
		this(output, recipeFunction, format, false, ingredients);
	}

	/**
	 * Defines an advanced crafting recipe, using a format string.
	 * @param output Nominal output of the recipe
	 * @param recipeFunction {@link RecipeFunction}
	 * @param format Format
	 * @param mirrored Whether this recipe is mirrored
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, RecipeFunction recipeFunction, String format, boolean mirrored, ItemIngredient... ingredients) {
		this.output = output;

		String[] formatLines = format.split("\\-");
		int numIngredients = 0;
		int width = 0;
		for (String formatLine : formatLines) {
			width = Math.max(width, formatLine.length());
			for (char c : formatLine.toCharArray()) {
				if (c == ' ') {
					continue;
				} else if (c >= 'A' && c <= 'Z') {
					numIngredients++;
				} else {
					throw new IllegalArgumentException("Invalid character in format string " + format + ": " + c);
				}
			}
		}

		this.width = width;
		this.height = formatLines.length;
		this.posx = new int[numIngredients];
		this.posy = new int[numIngredients];
		this.ingredients = new ItemIngredient[numIngredients];
		this.mirrored = mirrored;

		int ingredientIndex = 0;
		for (int y = 0; y < this.height; y++) {
			String formatLine = formatLines[y];
			for (int x = 0; x < formatLine.length(); x++) {
				char c = formatLine.charAt(x);
				if (c == ' ') {
					continue;
				}

				this.posx[ingredientIndex] = x;
				this.posy[ingredientIndex] = y;
				this.ingredients[ingredientIndex] = ingredients[c - 'A'];
				ingredientIndex++;
			}
		}

		this.recipeFunction = recipeFunction;
		this.lastIngredientIndexOnFirstLine = getLastIngredientIndexOnFirstLine();
	}

	/**
	 * Defines a basic crafting recipe, using a 2D ingredients array.
	 * @param output Output {@link Item} of the recipe
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, Optional<ItemIngredient>[][] ingredients) {
		this(output, ingredients, false);
	}

	/**
	 * Defines a basic crafting recipe, using a 2D ingredients array.
	 * @param output Output {@link Item} of the recipe
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 * @param mirrored Whether this recipe is mirrored
	 */
	public ShapedCraftingRecipe(ItemFactory output, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
		this(output, (grid, tagged, o) -> Optional.of(o.build()), ingredients, mirrored);
	}

	/**
	 * Defines a basic crafting recipe, using a 2D ingredients array.
	 * @param output Output {@link Item} of the recipe
	 * @param recipeFunction {@link RecipeFunction}
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 */
	public ShapedCraftingRecipe(ItemFactory output, RecipeFunction recipeFunction, Optional<ItemIngredient>[][] ingredients) {
		this(output, recipeFunction, ingredients, false);
	}

	/**
	 * Defines an advanced crafting recipe, using a 2D ingredients array.
	 * @param output Nominal output of the recipe
	 * @param recipeFunction {@link RecipeFunction}
	 * @param ingredients {@link ItemIngredient ItemIngredients}
	 * @param mirrored Whether this recipe is mirrored
	 */
	public ShapedCraftingRecipe(ItemFactory output, RecipeFunction recipeFunction, Optional<ItemIngredient>[][] ingredients, boolean mirrored) {
		this.output = output;

		int numIngredients = 0;
		for (Optional<ItemIngredient>[] row : ingredients) {
			for (Optional<ItemIngredient> ingredient : row) {
				if (ingredient.isPresent()) {
					numIngredients++;
				}
			}
		}

		if (numIngredients == 0) {
			throw new IllegalArgumentException("Recipe has no ingredients");
		}

		// translate 2d ingredient array to ingredient list
		this.posx = new int[numIngredients];
		this.posy = new int[numIngredients];
		this.ingredients = new ItemIngredient[numIngredients];
		this.recipeFunction = recipeFunction;

		int width1 = 0;
		int height1 = ingredients.length;

		int ix = 0;
		for (int j = 0; j < ingredients.length; j++) {
			Optional<ItemIngredient>[] row = ingredients[j];
			width1 = Math.max(width1, row.length);

			for (int i = 0; i < row.length; i++) {
				if (row[i].isPresent()) {
					this.posx[ix] = (byte) i;
					this.posy[ix] = (byte) j;
					this.ingredients[ix] = row[i].get();
					ix++;
				}
			}
		}

		this.width = width1;
		this.height = height1;
		this.mirrored = mirrored;
		this.lastIngredientIndexOnFirstLine = getLastIngredientIndexOnFirstLine();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isMirrored() {
		return mirrored;
	}

	public ItemIngredient[] getIngredients() {
		return ingredients;
	}

	public int[] getIngredientsX() {
		return posx;
	}

	public int[] getIngredientsY() {
		return posy;
	}

	@Override
	public boolean matches(CraftingGrid inventory) {
		return findIngredientMapping(inventory) != null;
	}

	@Override
	public Optional<Item> getCraftingResult(CraftingGrid craftingGrid) {
		ShapedMapping mapping = findIngredientMapping(craftingGrid);
		if (mapping == null) {
			return Optional.empty();
		}

		return getRecipeOutput(craftingGrid, mapping);
	}

	@Override
	public void consumeItems(CraftingGrid craftingGrid) {
		ShapedMapping mapping = findIngredientMapping(craftingGrid);
		if (mapping == null) {
			return;
		}

		for (int i = 0; i < ingredients.length; i++) {
			Item original = mapping.items[i];
			Optional<Item> consumed = ingredients[i].consumeOnCrafting(original, craftingGrid);
			Objects.requireNonNull(consumed, "The result of 'ItemIngredient.consumeOnCrafting' can't be null");
			mapping.setStack(craftingGrid, i, consumed.filter(item -> item.count() > 0));
		}
	}

	@Override
	public Collection<String> getPossibleItemsInFirstSlot() {
		if (isMirrored()) {
			Collection<String> optionsForFirstItem = ingredients[0].getPossibleItemIds();
			if (optionsForFirstItem.isEmpty()) {
				return Collections.emptyList();
			}

			Collection<String> optionsForSecondItem = ingredients[lastIngredientIndexOnFirstLine].getPossibleItemIds();
			if (optionsForFirstItem.isEmpty()) {
				return Collections.emptyList();
			}

			Set<String> result = new HashSet<>();
			result.addAll(optionsForFirstItem);
			result.addAll(optionsForSecondItem);
			return result;
		} else {
			return ingredients[0].getPossibleItemIds();
		}
	}

	@Override
	public Optional<Item> getExampleOutput() {
		return Optional.of(output.build());
	}

	// #######################
	// ### Private methods ###
	// #######################

	private int getLastIngredientIndexOnFirstLine() {
		if (ingredients.length == 0) {
			return -1;
		}

		int firstLineIndex = Arrays.stream(posy).min().orElse(0);
		int result = 0;
		for (int i = 0; i < ingredients.length; i++) {
			if (posy[i] == firstLineIndex) {
				result = i;
			}
		}

		return result;
	}

	private ShapedMapping findIngredientMapping(CraftingGrid craftingGrid) {
		if (ingredients.length == 0) {
			return null;
		}

		if (craftingGrid.countFilledStacks() != ingredients.length) {
			return null;
		}

		ShapedMapping mapping = findIngredientMapping(craftingGrid, false);
		if (mapping == null && isMirrored()) {
			mapping = findIngredientMapping(craftingGrid, true);
		}

		return mapping;
	}

	private ShapedMapping findIngredientMapping(CraftingGrid craftingGrid, boolean mirrored) {
		Optional<Vector2D> optOffset = craftingGrid.getFirstNonEmptyPosition();
		if (!optOffset.isPresent()) {
			return null;
		}

		ShapedMapping mapping;
		if (mirrored) {
			mapping = new MirroredMapping(optOffset.get());
		} else {
			mapping = new NonMirroredMapping(optOffset.get());
		}

		if (!mapping.fitsInCraftingGrid(craftingGrid)) {
			return null;
		}

		for (int i = 0; i < ingredients.length; i++) {
			Optional<Item> item = mapping.getStack(craftingGrid, i);
			if (!item.isPresent()) {
				return null;
			}

			if (!ingredients[i].matches(item.get())) {
				return null;
			}

			mapping.items[i] = item.get();
		}

		return mapping;
	}

	private Optional<Item> getRecipeOutput(
		CraftingGrid craftingGrid,
		ShapedMapping shapedMapping) {
		Map<String, Item> tagged = new HashMap<>();
		for (int k = 0; k < ingredients.length; k++) {
			if (ingredients[k].getTag().isPresent()) {
				tagged.put(ingredients[k].getTag().get(), shapedMapping.items[k]);
			}
		}

		return recipeFunction.doCrafting(craftingGrid, tagged, output);
	}

	private abstract class ShapedMapping {
		public final int offsetX;
		public final int offsetY;
		public final Item[] items;

		private ShapedMapping(Vector2D offset) {
			this.offsetX = (int) offset.getX();
			this.offsetY = (int) offset.getY();
			this.items = new Item[ingredients.length];
		}

		public boolean fitsInCraftingGrid(CraftingGrid craftingGrid) {
			return offsetX >= 0
				&& offsetX + getWidth() <= craftingGrid.getWidth()
				&& offsetY >= 0
				&& offsetY + getHeight() <= craftingGrid.getHeight();
		}

		public abstract Optional<Item> getStack(CraftingGrid craftingGrid, int ingredient);

		public abstract void setStack(CraftingGrid craftingGrid, int ingredient, Optional<Item> value);

		@Override
		public String toString() {
			return String.format("%s{offsetX=%s, offsetY=%s, items=%s}", getClass().getSimpleName(), offsetX, offsetY, Arrays.toString(items));
		}
	}

	private class NonMirroredMapping extends ShapedMapping {
		private NonMirroredMapping(Vector2D firstItemOffset) {
			super(new Vector2D(
				firstItemOffset.getX() - posx[0],
				firstItemOffset.getY() - posy[0]));
		}

		@Override
		public Optional<Item> getStack(CraftingGrid craftingGrid, int ingredient) {
			return craftingGrid.getCrafting(
				offsetX + posx[ingredient],
				offsetY + posy[ingredient]);
		}

		@Override
		public void setStack(CraftingGrid craftingGrid, int ingredient, Optional<Item> value) {
			craftingGrid.setCrafting(
				offsetX + posx[ingredient],
				offsetY + posy[ingredient],
				value);
		}
	}

	private class MirroredMapping extends ShapedMapping {
		private MirroredMapping(Vector2D firstItemOffset) {
			super(new Vector2D(
				firstItemOffset.getX() + posx[lastIngredientIndexOnFirstLine] - Arrays.stream(posx).max().orElse(0),
				firstItemOffset.getY() - posy[0]));
		}

		@Override
		public Optional<Item> getStack(CraftingGrid craftingGrid, int ingredient) {
			return craftingGrid.getCrafting(
				offsetX + getWidth() - posx[ingredient] - 1,
				offsetY + posy[ingredient]);
		}

		@Override
		public void setStack(CraftingGrid craftingGrid, int ingredient, Optional<Item> value) {
			craftingGrid.setCrafting(
				offsetX + getWidth() - posx[ingredient] - 1,
				offsetY + posy[ingredient],
				value);
		}
	}
}
