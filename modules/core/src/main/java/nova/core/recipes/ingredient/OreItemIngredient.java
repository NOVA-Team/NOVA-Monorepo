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
import nova.core.recipes.crafting.CraftingGrid;
import nova.internal.core.Game;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Stan
 * @since 3/02/2015.
 */
public class OreItemIngredient extends AbstractIngredient {
	private final String name;

	public OreItemIngredient(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public Collection<String> getPossibleItemIds() {
		return Game.itemDictionary().get(name).stream().map(Item::getID).collect(Collectors.toList());
	}

	@Override
	public Collection<Item> getExampleItems() {
		return Game.itemDictionary().get(name);
	}

	@Override
	public boolean matches(Item item) {
		return Game.itemDictionary().get(name).contains(item);
	}

	@Override
	public Optional<Item> consumeOnCrafting(Item original, CraftingGrid craftingGrid) {
		return Optional.of(original)
			.filter(item -> item.count() > 1)
			.map(item -> item.withAmount(original.count() - 1));
	}

	@Override
	public String toString() {
		return String.format("OreItemIngredient[%s:%s]", getName(), getPossibleItemIds());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		return name.equals(((OreItemIngredient) other).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
