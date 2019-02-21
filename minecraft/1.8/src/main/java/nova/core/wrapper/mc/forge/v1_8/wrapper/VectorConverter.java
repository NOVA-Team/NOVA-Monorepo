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

package nova.core.wrapper.mc.forge.v1_8.wrapper;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import nova.core.nativewrapper.NativeConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VectorConverter implements NativeConverter<Vector3D, BlockPos> {

	public static VectorConverter instance() {
		return Game.natives().getNative(Vector3D.class, BlockPos.class);
	}

	@Override
	public Class<Vector3D> getNovaSide() {
		return Vector3D.class;
	}

	@Override
	public Class<BlockPos> getNativeSide() {
		return BlockPos.class;
	}

	@Override
	public Vector3D toNova(BlockPos pos) {
		return toNova((Vec3i) pos);
	}

	public Vector3D toNova(Vec3 pos) {
		return new Vector3D(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	public Vector3D toNova(Vec3i pos) {
		return new Vector3D(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public BlockPos toNative(Vector3D vec) {
		return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
	}
}
