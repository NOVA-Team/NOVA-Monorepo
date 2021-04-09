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

package nova.core.wrapper.mc.forge.v1_7_10.asm;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import nova.core.component.misc.FactoryProvider;
import nova.core.event.BlockEvent;
import nova.core.wrapper.mc.forge.v1_7_10.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.FWTileLoader;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward.MCBlockTransform;
import nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.world.WorldConverter;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Static forwarder forwards injected methods.
 * @author Calclavia
 */
public class StaticForwarder {

	private StaticForwarder() {}

	public static void chunkSetBlockEvent(Chunk chunk, int x, int y, int z, Block oldBlock, int oldMeta, Block newBlock, int newMeta) {
		nova.core.world.World world = WorldConverter.instance().toNova(chunk.worldObj);
		Vector3D position = new Vector3D((chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z);
		nova.core.block.Block oldBlockInstance;
		nova.core.block.Block newBlockInstance;

		if (oldBlock instanceof FWBlock) {
			oldBlockInstance = ((FWBlock) oldBlock).getFactory().build();
			oldBlockInstance.components.add(new MCBlockTransform(oldBlockInstance, world, position));
		} else {
			oldBlockInstance = new BWBlock(oldBlock, world, position);
			Game.blocks().get(net.minecraft.block.Block.blockRegistry.getNameForObject(oldBlock))
				.ifPresent(blockFactory -> oldBlockInstance.components.getOrAdd(new FactoryProvider(blockFactory)));
		}

		if (newBlock instanceof FWBlock) {
			newBlockInstance = ((FWBlock) newBlock).getFactory().build();
			newBlockInstance.components.add(new MCBlockTransform(newBlockInstance, world, position));
		} else {
			newBlockInstance = new BWBlock(newBlock, world, position);
			Game.blocks().get(net.minecraft.block.Block.blockRegistry.getNameForObject(newBlock))
				.ifPresent(blockFactory -> newBlockInstance.components.getOrAdd(new FactoryProvider(blockFactory)));
		}

		// Publish the event
		Game.events().publish(new BlockEvent.Change(world, position, oldBlockInstance, newBlockInstance));
	}

	/**
	 * Used to inject forwarded TileEntites
	 * @param data The TileEntity data
	 * @param clazz The TileEntity class
	 * @return The new TileEntity instance
	 * @throws Exception If an exception occurred
	 */
	public static TileEntity loadTileEntityHook(NBTTagCompound data, Class<? extends TileEntity> clazz) throws Exception {
		if (clazz.equals(FWTile.class)) {
			return FWTileLoader.loadTile(data);
		} else {
			return clazz.newInstance();
		}
	}

	/**
	 * Checks if the name's prefix is a nova mod ID prefix.
	 *
	 * @param name The prefix to check
	 * @return If the name's prefix is a nova mod ID prefix.
	 */
	public static boolean hasNovaPrefix(String name) {
		if (!name.contains(":"))
			return false;
		String prefix = name.substring(0, name.lastIndexOf(':'));
		return NovaLauncher.instance()
			.map(loader -> loader.getLoadedMods()
				.stream()
				.anyMatch(mod -> prefix.startsWith(mod.id())))
			.orElse(false);
	}

	/**
	 * Checks if the prefix is equal to the NOVA mod ID ("nova").
	 *
	 * @param prefix The prefix to check
	 * @return If the prefix is equal to the NOVA mod ID ("nova").
	 */
	public static boolean isNovaPrefix(String prefix) {
		return NovaMinecraft.id.equals(prefix);
	}
}
