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
import nova.core.util.id.Identifier;
import nova.core.util.id.StringIdentifier;
import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class OreItemIngredient implements ItemIngredient {
	private final String name;

	public OreItemIngredient(String name) {
		this.name = name;
	}

	@Override
	public StringIdentifier getID() {
		return new StringIdentifier(name);
	}

	@Override
	public Optional<Collection<Identifier>> getPossibleItemIDs() {
		return Optional.of(Game.itemDictionary().get(name).stream().map(Item::getID).collect(Collectors.toList()));
	}

	@Override
	public Optional<Collection<Item>> getExampleItems() {
		List<Item> result = new ArrayList<Item>();


		return Optional.of(Game.itemDictionary().get(name));
	}

	@Override
	public boolean isSubsetOf(ItemIngredient ingredient) {
		return false;
	}

	@Override
	public boolean matches(Item item) {
		return Game.itemDictionary().get(name).contains(item);
	}

	@Override
	public Optional<String> getTag() {
		return Optional.empty();
	}

	@Override
	public Item consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return null;
	}
}
