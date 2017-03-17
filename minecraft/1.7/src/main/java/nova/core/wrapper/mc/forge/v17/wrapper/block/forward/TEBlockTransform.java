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
package nova.core.wrapper.mc.forge.v17.wrapper.block.forward;

import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v17.wrapper.block.world.WorldConverter;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author ExE Boss
 */
public class TEBlockTransform extends BlockTransform {

	private final FWTile tileEntity;

	public TEBlockTransform(FWTile tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public Vector3D position() {
		return new Vector3D(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}

	@Override
	public World world() {
		return WorldConverter.instance().toNova(tileEntity.getWorldObj());
	}

	public net.minecraft.world.World mcWorld() {
		return tileEntity.getWorldObj();
	}

	@Override
	public void setWorld(nova.core.world.World world) {
		nova.core.world.World originalWorld = world();
		Vector3D originalPosition = position();
		world.setBlock(position(), tileEntity.block.getFactory());
		net.minecraft.world.World mcWorld = Game.natives().toNative(world);
		mcWorld.setTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, tileEntity);
		tileEntity.setWorldObj(mcWorld);
		originalWorld.removeBlock(originalPosition);
	}

	@Override
	public void setPosition(Vector3D position) {
		Vector3D originalPosition = position();
		world().setBlock(position, tileEntity.block.getFactory());
		mcWorld().setTileEntity((int) position.getX(), (int) position.getY(), (int) position.getZ(), tileEntity);
		tileEntity.xCoord = (int) position.getX();
		tileEntity.yCoord = (int) position.getY();
		tileEntity.zCoord = (int) position.getZ();
		world().removeBlock(originalPosition);
	}
}
