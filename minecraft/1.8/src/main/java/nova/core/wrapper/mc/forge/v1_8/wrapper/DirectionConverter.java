/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_8.wrapper;

import net.minecraft.util.EnumFacing;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.Direction;
import nova.internal.core.Game;

/**
 *
 * @author ExE Boss
 */
public class DirectionConverter implements NativeConverter<Direction, EnumFacing> {

	public static DirectionConverter instance() {
		return Game.natives().getNative(Direction.class, EnumFacing.class);
	}

	@Override
	public Class<Direction> getNovaSide() {
		return Direction.class;
	}

	@Override
	public Class<EnumFacing> getNativeSide() {
		return EnumFacing.class;
	}

	@Override
	public Direction toNova(EnumFacing nativeObj) {
		if (null == nativeObj)
			return Direction.UNKNOWN;
		else switch (nativeObj) {
			case DOWN:  return Direction.DOWN;
			case UP:    return Direction.UP;
			case NORTH: return Direction.NORTH;
			case SOUTH: return Direction.SOUTH;
			case WEST:  return Direction.WEST;
			case EAST:  return Direction.EAST;
			default: return Direction.UNKNOWN;
		}
	}

	@Override
	public EnumFacing toNative(Direction novaObj) {
		switch (novaObj) {
			case DOWN:  return EnumFacing.DOWN;
			case UP:    return EnumFacing.UP;
			case NORTH: return EnumFacing.NORTH;
			case SOUTH: return EnumFacing.SOUTH;
			case WEST:  return EnumFacing.WEST;
			case EAST:  return EnumFacing.EAST;
			default: return (EnumFacing) null;
		}
	}
}
