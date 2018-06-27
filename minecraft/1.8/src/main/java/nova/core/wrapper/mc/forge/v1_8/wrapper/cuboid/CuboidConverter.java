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

package nova.core.wrapper.mc.forge.v1_8.wrapper.cuboid;

import net.minecraft.util.AxisAlignedBB;
import nova.core.nativewrapper.NativeConverter;
import nova.core.util.shape.Cuboid;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class CuboidConverter implements NativeConverter<Cuboid, AxisAlignedBB> {

	public static CuboidConverter instance() {
		return Game.natives().getNative(Cuboid.class, AxisAlignedBB.class);
	}

	@Override
	public Class<Cuboid> getNovaSide() {
		return Cuboid.class;
	}

	@Override
	public Class<AxisAlignedBB> getNativeSide() {
		return AxisAlignedBB.class;
	}

	@Override
	public Cuboid toNova(AxisAlignedBB aabb) {
		return new Cuboid(new Vector3D(aabb.minX, aabb.minY, aabb.minZ), new Vector3D(aabb.maxX, aabb.maxY, aabb.maxZ));
	}

	@Override
	public AxisAlignedBB toNative(Cuboid cuboid) {
		return AxisAlignedBB.fromBounds(cuboid.min.getX(), cuboid.min.getY(), cuboid.min.getZ(), cuboid.max.getX(), cuboid.max.getY(), cuboid.max.getZ());
	}
}
