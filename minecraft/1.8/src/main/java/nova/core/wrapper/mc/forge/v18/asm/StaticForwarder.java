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

package nova.core.wrapper.mc.forge.v18.asm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import nova.core.event.BlockEvent;
import nova.core.wrapper.mc.forge.v18.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v18.wrapper.block.forward.FWTile;
import nova.core.wrapper.mc.forge.v18.wrapper.block.forward.FWTileLoader;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Static forwarder forwards injected methods.
 * @author Calclavia
 */
public class StaticForwarder {

	private StaticForwarder() {}

	public static void chunkSetBlockEvent(Chunk chunk, BlockPos pos, IBlockState oldBlockState, IBlockState newBlockState) {
		// Publish the event
		Game.events().publish(
			new BlockEvent.Change(
				Game.natives().toNova(chunk.getWorld()),
				new Vector3D((chunk.xPosition << 4) + pos.getX(), pos.getY(), (chunk.zPosition << 4) + pos.getZ()),
				Game.natives().toNova(oldBlockState.getBlock()), Game.natives().toNova(newBlockState.getBlock())
			)
		);
	}

	/**
	 * Used to inject forwarded TileEntites
	 * @param data
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static TileEntity loadTileEntityHook(NBTTagCompound data, Class<? extends TileEntity> clazz) throws Exception {
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
	public static boolean addPrefix$isNovaPrefix(String prefix) {
		return NovaMinecraft.id.equals(prefix);
	}
}
