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
package nova.core.wrapper.mc.forge.v1_8.wrapper.block.backward;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_8.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.world.WorldConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Optional;

/**
 * @author ExE Boss
 */
public class BWBlockTransform extends BlockTransform {

	private final BWBlock block;
	private World world;
	private Vector3D position;

	public BWBlockTransform(BWBlock block, World world, Vector3D position) {
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

	public BlockPos blockPos() {
		return VectorConverter.instance().toNative(position);
	}

	public IBlockAccess blockAccess() {
		return WorldConverter.instance().toNative(world);
	}

	@Override
	public void setWorld(World world) {
		BlockPos pos = blockPos();
		net.minecraft.world.World oldWorld = (net.minecraft.world.World) WorldConverter.instance().toNative(this.world);
		net.minecraft.world.World newWorld = (net.minecraft.world.World) WorldConverter.instance().toNative(world);
		Optional<TileEntity> tileEntity = Optional.ofNullable(oldWorld.getTileEntity(pos));
		Optional<NBTTagCompound> nbt = Optional.empty();
		if (tileEntity.isPresent()) {
			NBTTagCompound compound = new NBTTagCompound();
			tileEntity.get().writeToNBT(compound);
			nbt = Optional.of(compound);
		}
		newWorld.setBlockState(pos, block.blockState());
		oldWorld.removeTileEntity(pos);
		oldWorld.setBlockToAir(pos);
		Optional<TileEntity> newTileEntity = Optional.ofNullable(newWorld.getTileEntity(pos));
		if (newTileEntity.isPresent() && nbt.isPresent()) {
			newTileEntity.get().readFromNBT(nbt.get());
		}
		this.world = world;
	}

	@Override
	public void setPosition(Vector3D position) {
		BlockPos oldPos = blockPos();
		BlockPos newPos = VectorConverter.instance().toNative(position);
		net.minecraft.world.World world = (net.minecraft.world.World) WorldConverter.instance().toNative(this.world);
		Optional<TileEntity> tileEntity = Optional.ofNullable(blockAccess().getTileEntity(oldPos));
		Optional<NBTTagCompound> nbt = Optional.empty();
		if (tileEntity.isPresent()) {
			NBTTagCompound compound = new NBTTagCompound();
			tileEntity.get().writeToNBT(compound);
            compound.setInteger("x", newPos.getX());
            compound.setInteger("y", newPos.getY());
            compound.setInteger("z", newPos.getZ());
			nbt = Optional.of(compound);
		}
		world.setBlockState(newPos, block.blockState());
		world.removeTileEntity(oldPos);
		world.setBlockToAir(oldPos);
		Optional<TileEntity> newTileEntity = Optional.ofNullable(blockAccess().getTileEntity(newPos));
		if (newTileEntity.isPresent() && nbt.isPresent()) {
			newTileEntity.get().readFromNBT(nbt.get());
		}
		this.position = position;
	}
}
