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

package nova.core.wrapper.mc.forge.v1_7_10.wrapper.block.world;

import net.minecraft.world.IBlockAccess;
import nova.core.nativewrapper.NativeConverter;
import nova.core.world.World;
import nova.internal.core.Game;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class WorldConverter implements NativeConverter<World, IBlockAccess> {

	public static WorldConverter instance() {
		return Game.natives().getNative(World.class, IBlockAccess.class);
	}

	@Override
	public Class<World> getNovaSide() {
		return World.class;
	}

	@Override
	public Class<IBlockAccess> getNativeSide() {
		return IBlockAccess.class;
	}

	@Override
	public World toNova(IBlockAccess nativeObj) {
		if (nativeObj instanceof net.minecraft.world.World) {
			Optional<World> opWorld = Game.worlds().findWorld(((net.minecraft.world.World) nativeObj).provider.getDimensionName());
			if (opWorld.isPresent()) {
				return opWorld.get();
			}
		}

		return new BWWorld(nativeObj);
	}

	@Override
	public IBlockAccess toNative(World novaObj) {
		if (novaObj instanceof BWWorld) {
			return ((BWWorld) novaObj).world();
		}

		//TODO: Right exception?
		throw new RuntimeException("Attempt to convert a world that is not a BWWorld!");
	}
}
