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

package nova.core.recipes.ingredient;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.internal.core.Game;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class SpecificItemIngredientTest {

    public SpecificItemIngredientTest() {
    }

	private ItemFactory item1;
	private ItemFactory item2;
	private ItemFactory item3;

    @Before
    public void setUp() {
		NovaLauncherTestFactory.createDummyLauncher();

		item1 = Game.items().register("test:item1", Item::new);
		item2 = Game.items().register("test:item2", Item::new);
		item3 = Game.items().register("test:item3", Item::new);
    }

	@Test
	public void testForBlock() {
		BlockFactory block = Game.blocks().register("test:block", Block::new);
		ItemFactory itemBlock = Game.items().getItemFromBlock(block);

		assertThat(ItemIngredient.forBlock(block).getPossibleItemIds()).hasSize(1).containsExactly(block.getID());
		assertThat(ItemIngredient.forBlock(block).getExampleItems()).hasSize(1).containsExactly(itemBlock.build());

		assertThat(ItemIngredient.forBlock(block).matches(itemBlock.build())).isTrue();
		assertThat(ItemIngredient.forBlock(block).isSubsetOf(ItemIngredient.forItem(itemBlock))).isTrue();

		assertThat(ItemIngredient.forBlock(block)).isEqualTo(ItemIngredient.forItem(itemBlock));
		assertThat(ItemIngredient.forBlock(block).toString()).isEqualTo("SpecificItemIngredient[test:block]");
		assertThat(ItemIngredient.forBlock(block).hashCode()).isEqualTo(ItemIngredient.forItem(itemBlock).hashCode());
	}

	@Test
	public void testGetPossibleItemIds() {
		assertThat(ItemIngredient.forItem(item1).getPossibleItemIds()).hasSize(1).containsExactly(item1.getID());
		assertThat(ItemIngredient.forItem(item2).getPossibleItemIds()).hasSize(1).containsExactly(item2.getID());
		assertThat(ItemIngredient.forItem(item3).getPossibleItemIds()).hasSize(1).containsExactly(item3.getID());
	}

	@Test
	public void testGetExampleItems() {
		assertThat(ItemIngredient.forItem(item1).getExampleItems()).hasSize(1).containsExactly(item1.build());
		assertThat(ItemIngredient.forItem(item2).getExampleItems()).hasSize(1).containsExactly(item2.build());
		assertThat(ItemIngredient.forItem(item3).getExampleItems()).hasSize(1).containsExactly(item3.build());
	}

	@Test
	public void testMatches() {
		assertThat(ItemIngredient.forItem(item1).matches(item1.build())).isTrue();
		assertThat(ItemIngredient.forItem(item1).matches(item2.build())).isFalse();
		assertThat(ItemIngredient.forItem(item1).matches(item3.build())).isFalse();

		assertThat(ItemIngredient.forItem(item2).matches(item1.build())).isFalse();
		assertThat(ItemIngredient.forItem(item2).matches(item2.build())).isTrue();
		assertThat(ItemIngredient.forItem(item2).matches(item3.build())).isFalse();

		assertThat(ItemIngredient.forItem(item3).matches(item1.build())).isFalse();
		assertThat(ItemIngredient.forItem(item3).matches(item2.build())).isFalse();
		assertThat(ItemIngredient.forItem(item3).matches(item3.build())).isTrue();
	}

	@Test
	public void testIsSubsetOf() {
		assertThat(ItemIngredient.forItem(item1).isSubsetOf(ItemIngredient.forItem(item1))).isTrue();
		assertThat(ItemIngredient.forItem(item1).isSubsetOf(ItemIngredient.forItem(item2))).isFalse();
		assertThat(ItemIngredient.forItem(item1).isSubsetOf(ItemIngredient.forItem(item3))).isFalse();

		assertThat(ItemIngredient.forItem(item2).isSubsetOf(ItemIngredient.forItem(item1))).isFalse();
		assertThat(ItemIngredient.forItem(item2).isSubsetOf(ItemIngredient.forItem(item2))).isTrue();
		assertThat(ItemIngredient.forItem(item2).isSubsetOf(ItemIngredient.forItem(item3))).isFalse();

		assertThat(ItemIngredient.forItem(item3).isSubsetOf(ItemIngredient.forItem(item1))).isFalse();
		assertThat(ItemIngredient.forItem(item3).isSubsetOf(ItemIngredient.forItem(item2))).isFalse();
		assertThat(ItemIngredient.forItem(item3).isSubsetOf(ItemIngredient.forItem(item3))).isTrue();
	}

	@Test
	public void testTag() {
		assertThat(ItemIngredient.forItem(item1).withTag("tag").getTag()).isPresent().contains("tag");
		assertThat(ItemIngredient.forItem(item2).getTag()).isEmpty();
	}

	@Test
	public void testToString() {
		assertThat(ItemIngredient.forItem(item1).toString()).isEqualTo("SpecificItemIngredient[test:item1]");
		assertThat(ItemIngredient.forItem(item2).toString()).isEqualTo("SpecificItemIngredient[test:item2]");
		assertThat(ItemIngredient.forItem(item3).toString()).isEqualTo("SpecificItemIngredient[test:item3]");
	}

	@Test
	public void testEquals() {
		ItemIngredient ingredient = ItemIngredient.forItem(item1);
		assertThat(ingredient).isEqualTo(ingredient);
		assertThat(ItemIngredient.forItem(item2)).isEqualTo(ItemIngredient.forItem(item2));
		assertThat(ItemIngredient.forItem(item3)).isEqualTo(ItemIngredient.forItem(item3));
		assertThat(ingredient).isNotEqualTo(null);
		assertThat(ingredient).isNotEqualTo(ItemIngredient.forDictionary("ore"));
	}

	@Test
	public void testHashCode() {
		assertThat(ItemIngredient.forItem(item1).hashCode()).isEqualTo(ItemIngredient.forItem(item1).hashCode());
		assertThat(ItemIngredient.forItem(item2).hashCode()).isEqualTo(ItemIngredient.forItem(item2).hashCode());
		assertThat(ItemIngredient.forItem(item3).hashCode()).isEqualTo(ItemIngredient.forItem(item3).hashCode());
	}
}
