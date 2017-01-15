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
import nova.core.util.id.Identifiable;
import nova.core.util.id.Identifier;
import nova.core.util.id.StringIdentifier;

import java.util.Optional;

/**
 * For object that belong specific categories.
 * Used by blocks and items to sort into manageable categories for the game.
 * @author Calclavia
 */
public class Category extends Component implements Identifiable {

	public final String name;
	public Optional<Item> item;

	public Category(String name, Item item) {
		this.name = name;
		this.item = Optional.of(item);
	}

	public Category(String name) {
		this.name = name;
		this.item = Optional.empty();
	}

	@Override
	public Identifier getID() {
		return new StringIdentifier(name);
	}
}
