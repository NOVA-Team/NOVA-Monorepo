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

import nova.core.component.misc.FactoryProvider;
import nova.core.item.ItemBlock;
import nova.core.util.registry.Factory;
import nova.internal.core.Game;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The factory type for blocks.
 * @author Calclavia
 */
public class BlockFactory extends Factory<BlockFactory, Block> {

	final Consumer<BlockFactory> postRegister;

	public BlockFactory(String id, Supplier<Block> constructor, Function<Block, Block> processor, Consumer<BlockFactory> postRegister) {
		super(id, constructor, processor);
		this.postRegister = postRegister;
	}

	public BlockFactory(String id, Supplier<Block> constructor) {
		this(id, constructor, blockFactory -> Game.items().register(id, () -> new ItemBlock(blockFactory)));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 * @param constructor The constructor function
	 * @param postRegister Function for registering item blocks
	 */
	public BlockFactory(String id, Supplier<Block> constructor, Consumer<BlockFactory> postRegister) {
		super(id, constructor);
		this.postRegister = postRegister;
	}

	@Override
	protected BlockFactory selfConstructor(String id, Supplier<Block> constructor, Function<Block, Block> processor) {
		return new BlockFactory(id, constructor, processor, postRegister);
	}

	@Override
	public Block build() {
		Block build = super.build();
		build.components.add(new FactoryProvider(this));
		return build;
	}
}
