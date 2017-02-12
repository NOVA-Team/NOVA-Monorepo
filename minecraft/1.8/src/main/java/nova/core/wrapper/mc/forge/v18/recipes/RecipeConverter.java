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

package nova.core.wrapper.mc.forge.v18.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import nova.core.item.Item;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.core.recipes.ingredient.ItemIngredient;
import nova.core.recipes.ingredient.OreItemIngredient;
import nova.core.recipes.crafting.ShapedCraftingRecipe;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;
import nova.core.recipes.ingredient.SpecificItemIngredient;
import nova.core.util.Identifiable;
import nova.core.wrapper.mc.forge.v18.util.ReflectionUtil;
import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Wraps Minecraft crafting recipes to NOVA crafting recipes and vice versa.
 *
 * Although a naive implementation could wrap NOVA recipes into its own recipe class and forward that, mods like NEI
 * won't be able to handle those recipes. Hence we wrap them into ShapedRecipes / ShapelessRecipes / ShapedOreRecipes and ShapelessOreRecipes
 * as much as possible. Only when that is not possible (a rare case, in practice) the generic wrapper is used.
 *
 * The reverse is also done, such that mods listening to NOVA ShapedCraftingRecipes and ShapelessCraftingRecipes would be
 * given the proper information and be informed of their registration, if desired.
 * @author Stan Hebben
 */
public class RecipeConverter {
	public static final int TYPE_ADVANCED = 0;
	public static final int TYPE_ORE = 1;
	public static final int TYPE_BASIC = 2;

	private RecipeConverter() {
	}

	private static int getIngredientType(ItemIngredient ingredient) {
		if (ingredient instanceof SpecificItemIngredient) {
			return TYPE_BASIC;
		} else if (ingredient instanceof OreItemIngredient) {
			return TYPE_ORE;
		} else {
			return TYPE_ADVANCED;
		}
	}

	private static Object getInternal(ItemIngredient ingredient) {
		if (ingredient instanceof SpecificItemIngredient) {
			return wrapSpecific((SpecificItemIngredient) ingredient);
		} else if (ingredient instanceof OreItemIngredient) {
			return ((OreItemIngredient) ingredient).getName();
		}

		return null;
	}

	private static ItemIngredient getIngredient(Object ingredient) {
		if (ingredient == null) {
			return null;
		} else if (ingredient instanceof ItemStack) {
			return new SpecificItemIngredient(((Identifiable) Game.natives().toNova(ingredient)).getID());
		} else if (ingredient instanceof String) {
			return new OreItemIngredient((String) ingredient);
		} else if (ingredient instanceof List) {
			String oreDictEntry = findOreDictEntryFor((List<?>) ingredient);
			if (oreDictEntry == null) {
				return null;
			}

			return new OreItemIngredient(oreDictEntry);
		} else {
			return null;
		}
	}

	private static String findOreDictEntryFor(List<?> ingredient) {
		for (String key : net.minecraftforge.oredict.OreDictionary.getOreNames()) {
			if (net.minecraftforge.oredict.OreDictionary.getOres(key).equals(ingredient)) {
				return key;
			}
		}

		return null;
	}

	private static ItemStack wrapSpecific(SpecificItemIngredient ingredient) {
		for (Item item : ingredient.getExampleItems()) {
			return Game.natives().toNative(item.getFactory().build());
		}

		throw new AssertionError("this can't be!");
	}

	private static int getRecipeType(ItemIngredient[] ingredients) {
		int type = TYPE_BASIC;
		for (ItemIngredient ingredient : ingredients) {
			type = Math.min(type, getIngredientType(ingredient));
		}
		return type;
	}

	public static IRecipe toMinecraft(CraftingRecipe recipe) {
		if (recipe instanceof ShapedCraftingRecipe) {
			return convert((ShapedCraftingRecipe) recipe);
		} else if (recipe instanceof ShapelessCraftingRecipe) {
			return convert((ShapelessCraftingRecipe) recipe);
		} else {
			return new NovaCraftingRecipe(recipe);
		}
	}

	public static IRecipe convert(ShapelessCraftingRecipe recipe) {
		ItemIngredient[] ingredients = recipe.getIngredients();
		int type = getRecipeType(ingredients);

		switch (type) {
			case TYPE_BASIC: {
				ItemStack[] items = new ItemStack[ingredients.length];
				for (int i = 0; i < ingredients.length; i++) {
					items[i] = wrapSpecific((SpecificItemIngredient) ingredients[i]);
				}
				return new ShapelessRecipeBasic(items, recipe);
			} case TYPE_ORE: {
				Object[] items = new Object[ingredients.length];
				for (int i = 0; i < ingredients.length; i++) {
					items[i] = getInternal(ingredients[i]);
				}
				return new ShapelessRecipeOre(items, recipe);
			} default:
				return new NovaCraftingRecipe(recipe);
		}
	}

	public static IRecipe convert(ShapedCraftingRecipe recipe) {
		ItemIngredient[] ingredients = recipe.getIngredients();
		int[] posx = recipe.getIngredientsX();
		int[] posy = recipe.getIngredientsY();

		// determine recipe type
		int type = getRecipeType(ingredients);

		// construct recipe
		if (type == TYPE_BASIC) {
			ItemStack[] basicIngredients = new ItemStack[recipe.getHeight() * recipe.getWidth()];
			for (int i = 0; i < ingredients.length; i++) {
				basicIngredients[posx[i] + posy[i] * recipe.getWidth()] = wrapSpecific((SpecificItemIngredient) ingredients[i]);
			}

			return new ShapedRecipeBasic(basicIngredients, recipe);
		} else if (type == TYPE_ORE) {
			Object[] converted = new Object[recipe.getHeight() * recipe.getWidth()];
			for (int i = 0; i < ingredients.length; i++) {
				converted[posx[i] + posy[i] * recipe.getWidth()] = getInternal(ingredients[i]);
			}

			// arguments contents:
			// 1) recipe patterns
			// 2) characters + ingredients

			int counter = 0;
			String[] parts = new String[recipe.getHeight()];
			ArrayList rarguments = new ArrayList();
			for (int i = 0; i < recipe.getHeight(); i++) {
				char[] pattern = new char[recipe.getWidth()];
				for (int j = 0; j < recipe.getWidth(); j++) {
					int off = i * recipe.getWidth() + j;
					if (converted[off] == null) {
						pattern[j] = ' ';
					} else {
						pattern[j] = (char) ('A' + counter);
						rarguments.add(pattern[j]);
						rarguments.add(converted[off]);
						counter++;
					}
				}
				parts[i] = new String(pattern);
			}

			rarguments.addAll(0, Arrays.asList(parts));

			return new ShapedRecipeOre(rarguments.toArray(), recipe);
		} else {
			return new NovaCraftingRecipe(recipe);
		}
	}

	public static CraftingRecipe toNova(IRecipe recipe) {
		ItemStack recipeOutput = recipe.getRecipeOutput();
		Item output;
		if (recipeOutput == null) {
			output = null;
		} else {
			output = Game.natives().toNova(recipeOutput);
		}

		if (recipe instanceof ShapelessRecipes) {
			ShapelessRecipes shapeless = (ShapelessRecipes) recipe;

			ItemIngredient[] ingredients = new ItemIngredient[shapeless.recipeItems.size()];
			for (int i = 0; i < ingredients.length; i++) {
				ingredients[i] = getIngredient(shapeless.recipeItems.get(i));
			}

			return new ShapelessCraftingRecipe(output, ingredients);
		} else if (recipe instanceof ShapedRecipes) {
			ShapedRecipes shaped = (ShapedRecipes) recipe;

			Optional<ItemIngredient>[][] ingredients = new Optional[shaped.recipeHeight][shaped.recipeWidth];
			for (int i = 0; i < shaped.recipeHeight; i++) {
				for (int j = 0; j < shaped.recipeWidth; j++) {
					ingredients[i][j] = Optional.ofNullable(getIngredient(shaped.recipeItems[i * shaped.recipeWidth + j]));
				}
			}

			return new ShapedCraftingRecipe(output, ingredients, false);
		} else if (recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;

			int width = ReflectionUtil.getShapedOreRecipeWidth(shaped);
			int height = recipe.getRecipeSize() / width;

			Optional<ItemIngredient>[][] recipeIngredients = new Optional[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					recipeIngredients[i][j] = Optional.ofNullable(getIngredient(shaped.getInput()[i * width + j]));
				}
			}

			return new ShapedCraftingRecipe(output, recipeIngredients, false);
		} else if (recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

			ItemIngredient[] ingredients = new ItemIngredient[shapeless.getRecipeSize()];
			for (int i = 0; i < ingredients.length; i++) {
				ingredients[i] = getIngredient(shapeless.getInput().get(i));
			}

			return new ShapelessCraftingRecipe(output, ingredients);
		} else {
			return new MCCraftingRecipe(recipe);
		}
	}
}
