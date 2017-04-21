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
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;

/**
 * @author ExE Boss
 */
public class TEBlockTransform extends BlockTransform {

	private final FWTile tileEntity;

	public TEBlockTransform(FWTile tileEntity) {
		this.tileEntity = tileEntity;
	}

	public Block block() {
		return tileEntity.block;
	}

	@Override
	public Vector3D position() {
		return VectorConverter.instance().toNova(tileEntity.getPos());
	}

	@Override
	public World world() {
		return WorldConverter.instance().toNova(tileEntity.getWorld());
	}

	public BlockPos blockPos() {
		return tileEntity.getPos();
	}

	public IBlockAccess blockAccess() {
		return tileEntity.getWorld();
	}

	@Override
	public void setWorld(World world) {
		world().setBlock(position(), tileEntity.block.getFactory());
		world().removeBlock(position());
		Objects.requireNonNull((FWTile) WorldConverter.instance().toNative(world).getTileEntity(blockPos())).setBlock(tileEntity.block);
	}

	@Override
	public void setPosition(Vector3D position) {
		world().setBlock(position, tileEntity.block.getFactory());
		world().removeBlock(position());
		Objects.requireNonNull((FWTile) blockAccess().getTileEntity(VectorConverter.instance().toNative(position))).setBlock(tileEntity.block);
	}
}
