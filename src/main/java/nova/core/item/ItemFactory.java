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

package nova.core.item;

import nova.core.component.misc.FactoryProvider;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.registry.Factory;
import nova.core.util.id.Identifiable;
import nova.core.util.id.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class ItemFactory extends Factory<ItemFactory, Item> implements Identifiable {
	public ItemFactory(Identifier id, Supplier<Item> constructor, Function<Item, Item> processor) {
		super(id, constructor, processor);
	}

	public ItemFactory(Identifier id, Supplier<Item> constructor) {
		super(id, constructor);
	}

	/**
	 * Makes a new item with no data
	 * @return Resulting item
	 */
	@Override
	public Item build() {
		Item build = super.build();
		Data data = new Data();
		data.className = build.getClass().getName();
		build.load(data);
		build.components.add(new FactoryProvider(this));
		return build;
	}

	/**
	 * Creates a new instance of the Item.
	 * @param data Item data, used if item is {@link Storable}
	 * @return Resulting item
	 */
	public Item build(Data data) {
		Item build = super.build();
		build.load(data);
		build.components.add(new FactoryProvider(this));
		return build;
	}

	public Data save(Item item) {
		Data data = new Data();
		item.save(data);
		return data;
	}

	@Override
	protected ItemFactory selfConstructor(Identifier id, Supplier<Item> constructor, Function<Item, Item> processor) {
		return new ItemFactory(id, constructor, processor);
	}
}
