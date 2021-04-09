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

import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.recipes.crafting.CraftingGrid;
import nova.core.util.exception.RegistrationException;
import nova.internal.core.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Specifies an ingredient identifying a single kind of item.
 * @author Stan Hebben
 */
public class SpecificItemIngredient extends AbstractIngredient {
	private final String itemId;

	public SpecificItemIngredient(String itemId) {
		this.itemId = itemId;
	}

	public String getItemId() {
		return itemId;
	}

	@Override
	public Collection<String> getPossibleItemIds() {
		return Collections.singleton(itemId);
	}

	@Override
	public Collection<Item> getExampleItems() {
		return Collections.singleton(getItem(itemId));
	}

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return ingredient.matches(getItem(itemId));
	}

	@Override
	public boolean matches(Item item) {
		return item.getID().equals(itemId);
	}

	@Override
	public Optional<Item> consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return Optional.of(original)
			.filter(item -> item.count() > 1)
			.map(item -> item.withAmount(original.count() - 1));
	}

	@Override
	public String toString() {
		return String.format("SpecificItemIngredient[%s]", getItemId());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		return itemId.equals(((SpecificItemIngredient) other).itemId);
	}

	@Override
	public int hashCode() {
		return itemId.hashCode();
	}

	private Item getItem(String itemId) {
		Optional<ItemFactory> itemFactory = Game.items().get(itemId);

		if (itemFactory.isPresent()) {
			return itemFactory.get().build();
		}

		throw new RegistrationException("Missing item: " + itemId);
	}
}
