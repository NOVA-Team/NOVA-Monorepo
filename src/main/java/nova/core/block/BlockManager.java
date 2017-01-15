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

package nova.core.block;

import nova.core.event.BlockEvent;
import nova.core.util.id.Identifier;
import nova.core.util.id.StringIdentifier;
import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;

import java.util.function.Supplier;

public class BlockManager extends FactoryManager<BlockManager, Block, BlockFactory> {

	private BlockManager(Registry<BlockFactory> registry) {
		super(registry);
	}

	/**
	 * Gets the block registered that represents air.
	 * @return The air block factory
	 */
	public BlockFactory getAirBlock() {
		return get(new StringIdentifier("air")).get();
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param constructor Block instance {@link Supplier}
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(Identifier id, Supplier<Block> constructor) {
		return register(new BlockFactory(id, constructor));
	}

	/**
	 * Register a new block with custom constructor arguments.
	 * @param factory {@link BlockFactory} of registered block
	 * @return Dummy block
	 */
	@Override
	public BlockFactory register(BlockFactory factory) {
		BlockEvent.Register event = new BlockEvent.Register(factory);
		Game.events().publish(event);
		registry.register(event.blockFactory);
		return event.blockFactory;
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	public class Init extends ManagerEvent<BlockManager> {
		public Init(BlockManager manager) {
			super(manager);
		}
	}
}
