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

package nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward;

import net.minecraft.nbt.NBTTagCompound;
import nova.core.block.Block;

/**
 *
 * @author ExE Boss
 */
public interface FWCustomTileLoader {

	/**
	 * Load a FWTile.
	 *
	 * @param block The Nova Block
	 * @param data The Minecraft NBTTagCompound
	 * @return The loaded FWTile or null, if this CustomTileLoader doesn't support this Tile.
	 */
	FWTile loadTile(Block block, NBTTagCompound data);

	/**
	 * Load a FWTile.
	 *
	 * @param block The Nova Block
	 * @param blockID The block ID
	 * @return The loaded FWTile or null, if this CustomTileLoader doesn't support this Tile.
	 */
	FWTile loadTile(Block block, String blockID);
}
