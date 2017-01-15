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
 */package nova.core.recipes.crafting;

import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.exception.RegistrationException;
import nova.core.util.id.Identifier;
import nova.internal.core.Game;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Specifies an ingredient identifying a single kind of item.
 * @author Stan Hebben
 */
public class SpecificItemIngredient implements ItemIngredient {
	private final Identifier itemId;

	public SpecificItemIngredient(Identifier itemId) {
		this.itemId = itemId;
	}

	@Override
	public Identifier getID() {
		return itemId;
	}

	@Override
	public Optional<Collection<Identifier>> getPossibleItemIDs() {
		return Optional.of(Collections.singleton(itemId));
	}

	@Override
	public Optional<Collection<Item>> getExampleItems() {
		return Optional.of(Collections.singleton(getItem(itemId)));
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
	public Optional<String> getTag() {
		return Optional.empty();
	}

	@Override
	public Item consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return original;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return itemId.equals(((SpecificItemIngredient) o).itemId);
	}

	@Override
	public int hashCode() {
		return itemId.hashCode();
	}

	private Item getItem(Identifier itemId) {
		Optional<ItemFactory> itemFactory = Game.items().get(itemId);

		if (itemFactory.isPresent()) {
			return itemFactory.get().build();
		}

		throw new RegistrationException("Missing item: " + itemId);
	}
}
