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

import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.event.bus.CancelableEvent;
import nova.core.item.event.ItemIDNotFoundEvent;
import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemManager extends FactoryManager<ItemManager, Item, ItemFactory> {

	private final Supplier<BlockManager> blockManager;

	private ItemManager(Registry<ItemFactory> itemRegistry, Supplier<BlockManager> blockManager) {
		super(itemRegistry);
		this.blockManager = blockManager;
	}

	/**
	 * Register a new item with custom constructor arguments.
	 * @param constructor The lambda expression to create a new constructor.
	 * @return Dummy item
	 */
	@Override
	public ItemFactory register(String id, Supplier<Item> constructor) {
		return register(new ItemFactory(id, constructor));
	}

	@Override
	public ItemFactory register(ItemFactory factory) {
		registry.register(factory);
		Game.events().publish(new ItemRegistrationEvent(factory));
		return factory;
	}

	public ItemFactory getItemFromBlock(BlockFactory block) {
		return registry.get(block.getID()).get();
	}

	public Optional<BlockFactory> getBlockFromItem(Item item) {
		return blockManager.get().get(item.getID());
	}

	@Override
	public Optional<ItemFactory> get(String name) {
		if (!registry.contains(name)) {
			ItemIDNotFoundEvent event = new ItemIDNotFoundEvent(name);
			Game.events().publish(event);

			if (event.getRemappedFactory() != null) {
				registry.register(event.getRemappedFactory());
			}
		}

		return registry.get(name);
	}

	//TODO: Move to item event
	public class ItemRegistrationEvent extends CancelableEvent {
		public final ItemFactory itemFactory;

		public ItemRegistrationEvent(ItemFactory itemFactory) {
			this.itemFactory = itemFactory;
		}
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	public class Init extends ManagerEvent<ItemManager> {
		public Init(ItemManager manager) {
			super(manager);
		}
	}
}
