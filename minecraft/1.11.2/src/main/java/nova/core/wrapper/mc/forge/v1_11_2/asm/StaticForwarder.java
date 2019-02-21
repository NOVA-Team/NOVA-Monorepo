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

package nova.core.wrapper.mc.forge.v1_11_2.asm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import nova.core.block.Block;
import nova.core.component.misc.FactoryProvider;
import nova.core.event.BlockEvent;
import nova.core.wrapper.mc.forge.v1_11_2.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWTileLoader;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;

/**
 * Static forwarder forwards injected methods.
 * @author Calclavia
 */
public class StaticForwarder {

	private StaticForwarder() {}

	public static void chunkSetBlockEvent(Chunk chunk, BlockPos pos, IBlockState oldBlockState, IBlockState newBlockState) {
		nova.core.world.World world = WorldConverter.instance().toNova(chunk.getWorld());
		Vector3D position = new Vector3D((chunk.x << 4) + pos.getX(), pos.getY(), (chunk.z << 4) + pos.getZ());
		Block oldBlockInstance;
		Block newBlockInstance;

		if (oldBlockState.getBlock() instanceof FWBlock) {
			oldBlockInstance = ((FWBlock) oldBlockState.getBlock()).getFactory().build();
		} else {
			oldBlockInstance = new BWBlock(oldBlockState, world, position);
			Game.blocks().get(Objects.toString(net.minecraft.block.Block.REGISTRY.getNameForObject(oldBlockState.getBlock())))
				.ifPresent(blockFactory -> oldBlockInstance.components.getOrAdd(new FactoryProvider(blockFactory)));
		}

		if (newBlockState.getBlock() instanceof FWBlock) {
			newBlockInstance = ((FWBlock) newBlockState.getBlock()).getFactory().build();
		} else {
			newBlockInstance = new BWBlock(newBlockState, world, position);
			Game.blocks().get(Objects.toString(net.minecraft.block.Block.REGISTRY.getNameForObject(newBlockState.getBlock())))
				.ifPresent(blockFactory -> newBlockInstance.components.getOrAdd(new FactoryProvider(blockFactory)));
		}

		// Publish the event
		Game.events().publish(new BlockEvent.Change(world, position, oldBlockInstance, newBlockInstance));
	}

	/**
	 * Used to inject forwarded TileEntites
	 * @param world The world to create the TileEntity in.
	 * @param data The TileEntity NBT tag compound
	 * @param clazz The TileEntity class
	 * @return The new TileEntity instance
	 * @throws Exception when an exception occurs
	 */
	public static TileEntity loadTileEntityHook(World world, NBTTagCompound data, Class<? extends TileEntity> clazz) throws Exception {
		if (FWTile.class.isAssignableFrom(clazz)) {
			return FWTileLoader.loadTile(data);
		} else {
			return clazz.newInstance();
		}
	}

	/**
	 * Checks if the prefix is equal to the NOVA mod ID ("nova").
	 *
	 * @param prefix The prefix to check
	 * @return If the prefix is equal to the NOVA mod ID ("nova").
	 */
	public static boolean isNovaPrefix(String prefix) {
		return NovaMinecraft.MOD_ID.equals(prefix);
	}
}
