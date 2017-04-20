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
public class OreItemIngredientTest {

    public OreItemIngredientTest() {
    }

	private ItemFactory item1;
	private ItemFactory item2;
	private ItemFactory item3;

    @Before
    public void setUp() {
		NovaLauncherTestFactory.createDummyLauncher();

		item1 = new ItemFactory("test:item1", Item::new);
		item2 = new ItemFactory("test:item2", Item::new);
		item3 = new ItemFactory("test:item3", Item::new);

		Game.itemDictionary().add("ore", item1, item2, item3);
		Game.itemDictionary().add("ore1", item1, item2);
		Game.itemDictionary().add("ore2", item3);
    }

	@Test
	public void testGetPossibleItemIds() {
		assertThat(ItemIngredient.forDictionary("ore").getPossibleItemIds()) .hasSize(3).containsOnly(item1.getID(), item2.getID(), item3.getID());
		assertThat(ItemIngredient.forDictionary("ore1").getPossibleItemIds()).hasSize(2).containsOnly(item1.getID(), item2.getID());
		assertThat(ItemIngredient.forDictionary("ore2").getPossibleItemIds()).hasSize(1).containsOnly(item3.getID());
	}

	@Test
	public void testGetExampleItems() {
		assertThat(ItemIngredient.forDictionary("ore") .getExampleItems()).hasSize(3).containsOnly(item1.build(), item2.build(), item3.build());
		assertThat(ItemIngredient.forDictionary("ore1").getExampleItems()).hasSize(2).containsOnly(item1.build(), item2.build());
		assertThat(ItemIngredient.forDictionary("ore2").getExampleItems()).hasSize(1).containsOnly(item3.build());
	}

	@Test
	public void testMatches() {
		assertThat(ItemIngredient.forDictionary("ore1").matches(item1.build())).isTrue();
		assertThat(ItemIngredient.forDictionary("ore1").matches(item2.build())).isTrue();
		assertThat(ItemIngredient.forDictionary("ore1").matches(item3.build())).isFalse();

		assertThat(ItemIngredient.forDictionary("ore2").matches(item1.build())).isFalse();
		assertThat(ItemIngredient.forDictionary("ore2").matches(item2.build())).isFalse();
		assertThat(ItemIngredient.forDictionary("ore2").matches(item3.build())).isTrue();
	}

	@Test
	public void testIsSubsetOf() {
		assertThat(ItemIngredient.forDictionary("ore1").isSubsetOf(ItemIngredient.forDictionary("ore"))).isTrue();
		assertThat(ItemIngredient.forDictionary("ore2").isSubsetOf(ItemIngredient.forDictionary("ore"))).isTrue();

		assertThat(ItemIngredient.forDictionary("ore2").isSubsetOf(ItemIngredient.forDictionary("ore1"))).isFalse();
		assertThat(ItemIngredient.forDictionary("ore1").isSubsetOf(ItemIngredient.forDictionary("ore2"))).isFalse();
	}

	@Test
	public void testTag() {
		assertThat(ItemIngredient.forDictionary("ore1").withTag("tag").getTag()).isPresent().contains("tag");
		assertThat(ItemIngredient.forDictionary("ore2").getTag()).isEmpty();
	}

	@Test
	public void testToString() {
		assertThat(ItemIngredient.forDictionary("ore2").toString()).isEqualTo("OreItemIngredient[ore2:[test:item3]]");
	}

	@Test
	public void testEquals() {
		ItemIngredient ingredient = ItemIngredient.forDictionary("ore");
		assertThat(ingredient).isEqualTo(ingredient);
		assertThat(ItemIngredient.forDictionary("ore1")).isEqualTo(ItemIngredient.forDictionary("ore1"));
		assertThat(ItemIngredient.forDictionary("ore2")).isEqualTo(ItemIngredient.forDictionary("ore2"));
		assertThat(ingredient).isNotEqualTo(null);
		assertThat(ingredient).isNotEqualTo(ItemIngredient.forItem(item1));
	}

	@Test
	public void testHashCode() {
		assertThat(ItemIngredient.forDictionary("ore") .hashCode()).isEqualTo(ItemIngredient.forDictionary("ore") .hashCode());
		assertThat(ItemIngredient.forDictionary("ore1").hashCode()).isEqualTo(ItemIngredient.forDictionary("ore1").hashCode());
		assertThat(ItemIngredient.forDictionary("ore2").hashCode()).isEqualTo(ItemIngredient.forDictionary("ore2").hashCode());
	}
}
