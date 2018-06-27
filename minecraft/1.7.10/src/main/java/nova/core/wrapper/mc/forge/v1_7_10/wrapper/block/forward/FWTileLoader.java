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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.wrapper.mc.forge.v1_7_10.asm.lib.ComponentInjector;
import nova.core.wrapper.mc.forge.v1_7_10.util.WrapperEvent;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * @author Vic Nightfall
 */
public final class FWTileLoader {

	private static ComponentInjector<FWTile> injector = new ComponentInjector<>(FWTile.class);

	private FWTileLoader() {
	}

	public static FWTile loadTile(NBTTagCompound data) {
		try {
			String blockID = data.getString("novaID");
			Block block = createBlock(blockID);
			FWTile tile = injector.inject(block, new Class<?>[0], new Object[0]);
			tile.setBlock(block);
			WrapperEvent.FWTileCreate event = new WrapperEvent.FWTileCreate(block, tile);
			Game.events().publish(event);
			return tile;
		} catch (Exception e) {
			throw new RuntimeException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	public static FWTile loadTile(String blockID) {
		try {
			Block block = createBlock(blockID);
			FWTile tile = injector.inject(block, new Class<?>[] { String.class }, new Object[] { blockID });
			tile.setBlock(block);
			WrapperEvent.FWTileCreate event = new WrapperEvent.FWTileCreate(block, tile);
			Game.events().publish(event);
			return tile;
		} catch (Exception e) {
			throw new RuntimeException("Fatal error when trying to create a new NOVA tile.", e);
		}
	}

	private static Block createBlock(String blockID) {
		Optional<BlockFactory> blockFactory = Game.blocks().get(blockID);
		if (blockFactory.isPresent()) {
			return blockFactory.get().build();
		} else {
			throw new RuntimeException("Error! Invalid NOVA block ID: " + blockID);
		}
	}
}
