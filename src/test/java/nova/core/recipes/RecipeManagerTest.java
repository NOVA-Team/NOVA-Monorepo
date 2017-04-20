/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.recipes;

import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.recipes.crafting.CraftingRecipe;
import nova.core.recipes.crafting.CraftingRecipeManager;
import nova.core.recipes.crafting.ShapedCraftingRecipe;
import nova.core.recipes.crafting.ShapelessCraftingRecipe;
import nova.core.recipes.ingredient.ItemIngredient;
import nova.core.recipes.smelting.BasicSmeltingRecipe;
import nova.core.recipes.smelting.SmeltingRecipe;
import nova.core.recipes.smelting.SmeltingRecipeManager;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class RecipeManagerTest {

    public RecipeManagerTest() {
    }

	private ItemFactory item1;
	private ItemFactory item2;
	private ItemFactory item3;

	private ShapedCraftingRecipe shapedCraftingRecipe;
	private ShapedCraftingRecipe shapedMirroredCraftingRecipe;
	private ShapelessCraftingRecipe shapelessCraftingRecipe;
	private BasicSmeltingRecipe basicSmeltingRecipe;

	private RecipeManager recipeManager;
	private CraftingRecipeManager craftingRecipeManager;
	private SmeltingRecipeManager smeltingRecipeManager;

    @Before
    public void setUp() {
		recipeManager = new RecipeManager();
		craftingRecipeManager = new CraftingRecipeManager(recipeManager);
		smeltingRecipeManager = new SmeltingRecipeManager(recipeManager);

		item1 = new ItemFactory("test:item1", Item::new);
		item2 = new ItemFactory("test:item2", Item::new);
		item3 = new ItemFactory("test:item3", Item::new);

		shapedCraftingRecipe = new ShapedCraftingRecipe(item1, "AB", ItemIngredient.forItem(item2), ItemIngredient.forItem(item3));
		shapedMirroredCraftingRecipe = new ShapedCraftingRecipe(item1, "AA-B", true, ItemIngredient.forItem(item2), ItemIngredient.forItem(item3));
		shapelessCraftingRecipe = new ShapelessCraftingRecipe(item1, ItemIngredient.forItem(item2), ItemIngredient.forItem(item3));
		basicSmeltingRecipe = new BasicSmeltingRecipe(item1, ItemIngredient.forItem(item2));
    }

	@Test
	public void testRecipes() {
		assertThat(recipeManager.getRecipes(Recipe.class)).isEmpty();
		assertThat(recipeManager.getRecipes(CraftingRecipe.class)).isEmpty();
		assertThat(recipeManager.getRecipes(SmeltingRecipe.class)).isEmpty();

		craftingRecipeManager.addRecipe(shapedCraftingRecipe);
		craftingRecipeManager.addRecipe(shapedMirroredCraftingRecipe);
		craftingRecipeManager.addRecipe(shapelessCraftingRecipe);
		smeltingRecipeManager.addRecipe(basicSmeltingRecipe);

		assertThat(recipeManager.getRecipes(Recipe.class)).hasSize(4).containsOnly(shapedCraftingRecipe, shapedMirroredCraftingRecipe, shapelessCraftingRecipe, basicSmeltingRecipe);
		assertThat(recipeManager.getRecipes(CraftingRecipe.class)).hasSize(3).containsOnly(shapedCraftingRecipe, shapedMirroredCraftingRecipe, shapelessCraftingRecipe);
		assertThat(recipeManager.getRecipes(SmeltingRecipe.class)).hasSize(1).containsOnly(basicSmeltingRecipe);

		craftingRecipeManager.removeRecipe(shapedCraftingRecipe);
		craftingRecipeManager.removeRecipe(shapedMirroredCraftingRecipe);
		craftingRecipeManager.removeRecipe(shapelessCraftingRecipe);
		smeltingRecipeManager.removeRecipe(basicSmeltingRecipe);

		assertThat(recipeManager.getRecipes(Recipe.class)).isEmpty();
		assertThat(recipeManager.getRecipes(CraftingRecipe.class)).isEmpty();
		assertThat(recipeManager.getRecipes(SmeltingRecipe.class)).isEmpty();
	}
}
