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

package nova.core.wrapper.mc.forge.v1_8.wrapper.block.forward;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_8.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.world.WorldConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * @author Calclavia
 */
public class MCBlockTransform extends BlockTransform {

	private Block block;
	private World world;
	private Vector3D position;

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
		world.setBlock(position, block.getFactory());
		Optional<Data> data = Optional.empty();
		if (block instanceof Storable) {
			data = Optional.of(new Data());
			((Storable) block).save(data.get());
		}
		this.world.removeBlock(position);
		Optional<Block> newBlock = world.getBlock(position);
		if (newBlock.isPresent()) {
			block = newBlock.get();
			if (newBlock.get() instanceof Storable && data.isPresent()) {
				((Storable) newBlock.get()).load(data.get());
			}
		}
		this.world = world;
	}

	@Override
	public void setPosition(Vector3D position) {
		world.setBlock(position, block.getFactory());
		Optional<Data> data = Optional.empty();
		if (block instanceof Storable) {
			data = Optional.of(new Data());
			((Storable) block).save(data.get());
		}
		world.removeBlock(this.position);
		Optional<Block> newBlock = world.getBlock(position);
		if (newBlock.isPresent()) {
			block = newBlock.get();
			if (newBlock.get() instanceof Storable && data.isPresent()) {
				((Storable) newBlock.get()).load(data.get());
			}
		}
		this.position = position;
	}
}
