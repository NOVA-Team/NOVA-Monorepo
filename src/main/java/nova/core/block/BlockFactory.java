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
import nova.core.language.LanguageManager;
import nova.core.language.Translatable;
import nova.core.util.registry.Factory;
import nova.internal.core.Game;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The factory type for blocks.
 * @author Calclavia
 */
public class BlockFactory extends Factory<BlockFactory, Block> implements Translatable {

	final Consumer<BlockFactory> postRegister;
	private String unlocalizedName;

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 * @param processor The function applied to the block after construction
	 * @param postRegister Function for registering item blocks
	 * @param mapping The custom DI mapping
	 */
	public BlockFactory(String id, Class<? extends Block> type, Function<Block, Block> processor, Consumer<BlockFactory> postRegister, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, processor, mapping);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 * @param processor The function applied to the block after construction
	 * @param postRegister Function for registering item blocks
	 */
	public BlockFactory(String id, Class<? extends Block> type, Function<Block, Block> processor, Consumer<BlockFactory> postRegister) {
		super(id, type, processor);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 * @param mapping The custom DI mapping
	 */
	public BlockFactory(String id, Class<? extends Block> type, Function<Class<?>, Optional<?>> mapping) {
		this(id, type, blockFactory -> Game.items().register(id, () -> new ItemBlock(blockFactory)), mapping);
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 */
	public BlockFactory(String id, Class<? extends Block> type) {
		this(id, type, blockFactory -> {Game.items().register(id, () -> new ItemBlock(blockFactory));});
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 * @param postRegister Function for registering item blocks
	 * @param mapping The custom DI mapping
	 */
	public BlockFactory(String id, Class<? extends Block> type, Consumer<BlockFactory> postRegister, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, block -> block, mapping);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param type The class of the block
	 * @param postRegister Function for registering item blocks
	 */
	public BlockFactory(String id, Class<? extends Block> type, Consumer<BlockFactory> postRegister) {
		super(id, type);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param constructor The constructor function
	 * @param processor The function applied to the block after construction
	 * @param postRegister Function for registering item blocks
	 */
	public BlockFactory(String id, Supplier<Block> constructor, Function<Block, Block> processor, Consumer<BlockFactory> postRegister) {
		super(id, constructor, processor);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param constructor The constructor function
	 */
	public BlockFactory(String id, Supplier<Block> constructor) {
		this(id, constructor, blockFactory -> Game.items().register(id, () -> new ItemBlock(blockFactory)));
	}

	/**
	 * Initializes a BlockFactory. A specific implementation of item block generation
	 * may be provided by post create.
	 *
	 * @param id The block ID
	 * @param constructor The constructor function
	 * @param postRegister Function for registering item blocks
	 */
	public BlockFactory(String id, Supplier<Block> constructor, Consumer<BlockFactory> postRegister) {
		super(id, constructor);
		this.postRegister = postRegister;
		this.setUnlocalizedName(getID().replaceAll(":", "."));
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

	public BlockFactory setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		return this;
	}

	@Override
	public String getUnlocalizedName() {
		return "block." + this.unlocalizedName;
	}

	@Override
	public String getLocalizedName() {
		return LanguageManager.instance().translate(this.getUnlocalizedName() + ".name", this.getReplacements());
	}
}
