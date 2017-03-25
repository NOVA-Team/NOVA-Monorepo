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

package nova.core.recipes.smelting;

import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.recipes.ingredient.ItemIngredient;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class BasicSmeltingRecipeTest {

    public BasicSmeltingRecipeTest() {
    }

	private ItemFactory item1;
	private ItemFactory item2;
	private ItemFactory item3;

    @Before
    public void setUp() {
		item1 = new ItemFactory("test:item1", Item::new);
		item2 = new ItemFactory("test:item2", Item::new);
		item3 = new ItemFactory("test:item3", Item::new);
    }

	@Test
	public void testExampleOutput() {
		@SuppressWarnings({"unchecked", "rawtypes"})
		SmeltingRecipe recipe = new BasicSmeltingRecipe(item1, ItemIngredient.forItem(item2));
		assertThat(recipe.getExampleOutput()).isPresent().contains(item1.build());
	}

	@Test
	public void testCraftingResult() {
		SmeltingRecipe recipe = new BasicSmeltingRecipe(item1, ItemIngredient.forItem(item2));
		assertThat(recipe.getCraftingResult(item2.build())).isPresent().contains(item1.build());
		assertThat(recipe.getCraftingResult(item3.build())).isEmpty();
	}

	@Test
	public void testMatches() {
		SmeltingRecipe recipe = new BasicSmeltingRecipe(item1, ItemIngredient.forItem(item2));
		assertThat(recipe.matches(item2.build())).isTrue();
		assertThat(recipe.matches(item3.build())).isFalse();
	}

	@Test
	public void testInput() {
		SmeltingRecipe recipe = new BasicSmeltingRecipe(item1, ItemIngredient.forItem(item2));
		assertThat(recipe.getInput()).isPresent().contains(ItemIngredient.forItem(item2));
	}
}
