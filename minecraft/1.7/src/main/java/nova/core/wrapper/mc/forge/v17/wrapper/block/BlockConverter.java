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

package nova.core.wrapper.mc.forge.v17.wrapper.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.Category;
import nova.core.event.BlockEvent;
import nova.core.loader.Mod;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc.forge.v17.launcher.ForgeLoadable;
import nova.core.wrapper.mc.forge.v17.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v17.wrapper.CategoryConverter;
import nova.core.wrapper.mc.forge.v17.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v17.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v17.wrapper.item.FWItemBlock;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;

import java.util.HashMap;

/**
 * @author Calclavia
 */
public class BlockConverter implements NativeConverter<Block, net.minecraft.block.Block>, ForgeLoadable {
	/**
	 * A map of all blockFactory to MC blocks registered
	 */
	public final HashMap<BlockFactory, net.minecraft.block.Block> blockFactoryMap = new HashMap<>();

	public static BlockConverter instance() {
		return Game.natives().getNative(Block.class, net.minecraft.block.Block.class);
	}

	@Override
	public Class<Block> getNovaSide() {
		return Block.class;
	}

	@Override
	public Class<net.minecraft.block.Block> getNativeSide() {
		return net.minecraft.block.Block.class;
	}

	@Override
	public Block toNova(net.minecraft.block.Block nativeBlock) {
		//Prevent recursive wrapping
		if (nativeBlock instanceof FWBlock) {
			return ((FWBlock) nativeBlock).dummy;
		}

		if (nativeBlock == Blocks.air) {
			return Game.blocks().getAirBlock().build();
		}

		return new BWBlock(nativeBlock);
	}

	@Override
	public net.minecraft.block.Block toNative(Block novaBlock) {
		//Prevent recursive wrapping
		if (novaBlock instanceof BWBlock) {
			return ((BWBlock) novaBlock).mcBlock;
		}

		return toNative(novaBlock.getFactory());
	}

	public net.minecraft.block.Block toNative(BlockFactory blockFactory) {
		return blockFactoryMap.get(blockFactory);
	}

	/**
	 * Register all Nova blocks
	 */
	@Override
	public void preInit(FMLPreInitializationEvent evt) {
		registerMinecraftToNOVA();
		registerNOVAToMinecraft();
	}

	private void registerMinecraftToNOVA() {
		//TODO: Will this register ALL Forge mod blocks as well?
		BlockManager blockManager = Game.blocks();
		net.minecraft.block.Block.blockRegistry.forEach(obj ->
				blockManager.register(
					new BlockFactory(net.minecraft.block.Block.blockRegistry.getNameForObject(obj).toString(),
						() -> new BWBlock((net.minecraft.block.Block) obj), evt -> {
					})
				)
		);
	}

	private void registerNOVAToMinecraft() {
		BlockManager blockManager = Game.blocks();

		//Register air block
		BlockFactory airBlock = new BlockFactory("air", () -> new BWBlock(Blocks.air) {
			@Override
			public boolean canReplace() {
				return true;
			}
		}, evt -> {
		});

		blockManager.register(airBlock);

		//NOTE: There should NEVER be blocks already registered in preInit() stage of a NativeConverter.
		Game.events().on(BlockEvent.Register.class).bind(evt -> registerNovaBlock(evt.blockFactory));
	}

	private void registerNovaBlock(BlockFactory blockFactory) {
		FWBlock blockWrapper = new FWBlock(blockFactory);
		blockFactoryMap.put(blockFactory, blockWrapper);
		NovaMinecraft.proxy.registerBlock(blockWrapper);
		String blockId = blockFactory.getID();
		if (!blockId.contains(":"))
			blockId = NovaLauncher.instance().flatMap(NovaLauncher::getCurrentMod).map(Mod::id).orElse("nova") + ':' + blockId;
		GameRegistry.registerBlock(blockWrapper, FWItemBlock.class, blockId);

		if (blockWrapper.dummy.components.has(Category.class) && FMLCommonHandler.instance().getSide().isClient()) {
			//Add into creative tab
			Category category = blockWrapper.dummy.components.get(Category.class);
			blockWrapper.setCreativeTab(CategoryConverter.instance().toNative(category, blockWrapper));
		}

		Game.logger().info("Registered block: {}", blockFactory.getID());
	}
}