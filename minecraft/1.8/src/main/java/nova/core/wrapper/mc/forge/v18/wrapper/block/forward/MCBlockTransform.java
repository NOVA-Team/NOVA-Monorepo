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

package nova.core.wrapper.mc.forge.v18.wrapper.block.forward;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v18.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v18.wrapper.block.world.WorldConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class MCBlockTransform extends BlockTransform {

	public final Block block;
	public final World world;
	public final Vector3D position;

	public MCBlockTransform(Block block, World world, Vector3D position) {
		this.block = block;
		this.world = world;
		this.position = position;
	}

	@Override
	public Vector3D position() {
		return position;
	}

	@Override
	public World world() {
		return world;
	}

	public IBlockAccess blockAccess() {
		return WorldConverter.instance().toNative(world);
	}

	public BlockPos blockPos() {
		return VectorConverter.instance().toNative(position);
	}

	@Override
	public void setWorld(World world) {
		BlockPos pos = blockPos();
		Optional<TileEntity> tileEntity = Optional.ofNullable(blockAccess().getTileEntity(pos));
		world.setBlock(position, block.getFactory());
		tileEntity.ifPresent(te -> {
			net.minecraft.world.World newWorld = Game.natives().toNative(world);
			newWorld.setTileEntity(pos, te);
			te.setWorldObj(newWorld);
		});
		this.world.removeBlock(position);
	}

	@Override
	public void setPosition(Vector3D position) {
		BlockPos oldPos = blockPos();
		BlockPos newPos = VectorConverter.instance().toNative(position);
		Optional<TileEntity> tileEntity = Optional.ofNullable(blockAccess().getTileEntity(oldPos));
		world.setBlock(position, block.getFactory());
		tileEntity.ifPresent(te -> {
			net.minecraft.world.World world = Game.natives().toNative(this.world);
			world.setTileEntity(newPos, te);
			te.setPos(newPos);
		});
		world.removeBlock(this.position);
	}
}
