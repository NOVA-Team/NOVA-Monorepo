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

package nova.core.wrapper.mc.forge.v1_11.wrapper.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.Category;
import nova.core.event.BlockEvent;
import nova.core.loader.Loadable;
import nova.core.nativewrapper.NativeConverter;
import nova.core.wrapper.mc.forge.v1_11.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11.util.ModCreativeTab;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11.wrapper.item.FWItemBlock;
import nova.internal.core.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Calclavia
 */
//TODO: Should be <BlockFactory, Block>
public class BlockConverter implements NativeConverter<Block, net.minecraft.block.Block>, Loadable {
	/**
	 * A map of all blockFactory to MC blocks registered
	 */
	public final HashMap<BlockFactory, net.minecraft.block.Block> blockFactoryMap = new HashMap<>();

	public static BlockConverter instance() {
		return (BlockConverter) Game.natives().getNative(Block.class, net.minecraft.block.Block.class);
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

		if (nativeBlock == Blocks.AIR) {
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
	public void preInit() {
		registerMinecraftToNOVA();
		registerNOVAToMinecraft();
	}

	private void registerMinecraftToNOVA() {
		//TODO: Will this register ALL Forge mod blocks as well?
		BlockManager blockManager = Game.blocks();
		net.minecraft.block.Block.REGISTRY.forEach(obj ->
				blockManager.register(
					new BlockFactory(net.minecraft.block.Block.REGISTRY.getNameForObject(obj).toString(),
						() -> new BWBlock((net.minecraft.block.Block) obj), evt -> {
					})
				)
		);
	}

	private void registerNOVAToMinecraft() {
		BlockManager blockManager = Game.blocks();

		//Register air block
		BlockFactory airBlock = new BlockFactory("air", () -> new BWBlock(Blocks.AIR) {
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
		FWItemBlock itemBlockWrapper = new FWItemBlock(blockWrapper);
		blockFactoryMap.put(blockFactory, blockWrapper);
		GameRegistry.register(blockWrapper, new ResourceLocation(blockFactory.getID().asString()));
		GameRegistry.register(itemBlockWrapper, new ResourceLocation(blockFactory.getID().asString()));
		NovaMinecraft.proxy.postRegisterBlock(blockWrapper);

		if (blockWrapper.dummy.components.has(Category.class) && FMLCommonHandler.instance().getSide().isClient()) {
			//Add into creative tab
			Category category = blockWrapper.dummy.components.get(Category.class);
			Optional<CreativeTabs> first = Arrays.stream(CreativeTabs.CREATIVE_TAB_ARRAY)
				.filter(tab -> tab.getTabLabel().equals(category.name))
				.findFirst();
			if (first.isPresent()) {
				blockWrapper.setCreativeTab(first.get());
			} else {
				Optional<nova.core.item.Item> item = category.item;
				ModCreativeTab tab = new ModCreativeTab(category.name, item.isPresent() ? Game.natives().toNative(item.get()) : Item.getItemFromBlock(blockWrapper));
				blockWrapper.setCreativeTab(tab);
			}
		}

		System.out.println("[NOVA]: Registered '" + blockFactory.getID() + "' block.");
	}
}
