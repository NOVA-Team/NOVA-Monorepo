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

package nova.core.component;

import nova.core.item.Item;
import nova.core.util.Identifiable;

import java.util.Optional;

/**
 * For object that belong specific categories.
 * Used by blocks and items to sort into manageable categories for the game.
 * @author Calclavia
 */
public class Category extends Component implements Identifiable {

	/** Building blocks (ie. stone, dirt, bricks etc.) */
	public static final String BUILDING_BLOCKS_NAME = "nova:building_blocks";
	/** Building blocks (ie. stone, dirt, bricks etc.) */
	public static final Category BUILDING_BLOCKS = new Category(BUILDING_BLOCKS_NAME);

	/** Decorative blocks (ie. flower pots, curtains, torches etc.) */
	public static final String DECORATIONS_NAME = "nova:decorations";
	/** Decorative blocks (ie. flower pots, curtains, torches etc.) */
	public static final Category DECORATIONS = new Category(DECORATIONS_NAME);

	/** Technology (ie. buttons, levers, wires etc.) */
	public static final String TECHNOLOGY_NAME = "nova:technology";
	/** Technology (ie. buttons, levers, wires etc.) */
	public static final Category TECHNOLOGY = new Category(TECHNOLOGY_NAME);

	/** Transportation (ie. cars, rails, minecarts etc.) */
	public static final String TRANSPORTATION_NAME = "nova:transportation";
	/** Transportation (ie. cars, rails, minecarts etc.) */
	public static final Category TRANSPORTATION = new Category(TRANSPORTATION_NAME);

	/** Food (ie. bacon and other foods) */
	public static final String FOOD_NAME = "nova:food";
	/** Food (ie. bacon and other foods) */
	public static final Category FOOD = new Category(FOOD_NAME);

	/** Tools */
	public static final String TOOLS_NAME = "nova:tools";
	/** Tools */
	public static final Category TOOLS = new Category(TOOLS_NAME);

	/** Weapons and armor */
	public static final String COMBAT_NAME = "nova:combat";
	/** Weapons and armor */
	public static final Category COMBAT = new Category(COMBAT_NAME);

	/** Alchemy (ie. brewing ingredients, potions etc.) */
	public static final String ALCHEMY_NAME = "nova:alchemy";
	/** Alchemy (ie. brewing ingredients, potions etc.) */
	public static final Category ALCHEMY = new Category(ALCHEMY_NAME);

	/** Raw materials (ie. ingots etc.) */
	public static final String MATERIALS_NAME = "nova:materials";
	/** Raw materials (ie. ingots etc.) */
	public static final Category MATERIALS = new Category(MATERIALS_NAME);

	/** Miscellaneous items (ie. what doesn't fit into any other category) */
	public static final String MISCELLANEOUS_NAME = "nova:miscellaneous";
	/** Miscellaneous items (ie. what doesn't fit into any other category) */
	public static final Category MISCELLANEOUS = new Category(MISCELLANEOUS_NAME);

	public final String name;
	public final Optional<Item> item;

	public Category(String name, Item item) {
		this.name = name;
		this.item = Optional.of(item);
	}

	public Category(String name) {
		this.name = name;
		this.item = Optional.empty();
	}

	public Category withItem(Optional<Item> item) {
		return item.isPresent() ? new Category(name, item.get()) : new Category(name);
	}

	@Override
	public String getID() {
		return name;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.name.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null || getClass() != other.getClass()) return false;
		return name.equals(((Category)other).name);
	}
}
