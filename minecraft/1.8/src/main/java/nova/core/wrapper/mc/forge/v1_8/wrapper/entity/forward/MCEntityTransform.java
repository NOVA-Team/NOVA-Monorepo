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

package nova.core.wrapper.mc.forge.v1_8.wrapper.entity.forward;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import nova.core.component.transform.EntityTransform;
import nova.core.util.UniqueIdentifiable;
import nova.core.util.math.RotationUtil;
import nova.core.util.math.Vector3DUtil;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_8.wrapper.block.world.WorldConverter;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Arrays;

/**
 * Wraps Transform3d used in entity
 * @author Calclavia
 */
public class MCEntityTransform extends EntityTransform implements UniqueIdentifiable {
	public final net.minecraft.entity.Entity wrapper;

	public MCEntityTransform(net.minecraft.entity.Entity wrapper) {
		this.wrapper = wrapper;
		this.setPivot(Vector3D.ZERO);
		this.setScale(Vector3DUtil.ONE);
	}

	@Override
	public World world() {
		return WorldConverter.instance().toNova(wrapper.worldObj);
	}

	@Override
	public void setWorld(World world) {
		wrapper.travelToDimension(Arrays
				.stream(DimensionManager.getWorlds())
				.filter(w -> w.getProviderName().equals(world.getID()))
				.findAny()
				.get()
				.provider
				.getDimensionId()
		);
	}

	@Override
	public Vector3D position() {
		return new Vector3D(wrapper.posX, wrapper.posY, wrapper.posZ);
	}

	@Override
	public void setPosition(Vector3D position) {
		if (wrapper instanceof EntityPlayerMP) {
			((EntityPlayerMP) wrapper).playerNetServerHandler.setPlayerLocation(position.getX(), position.getY(), position.getZ(), wrapper.rotationYaw, wrapper.rotationPitch);
		} else {
			wrapper.setPosition(position.getX(), position.getY(), position.getZ());
		}
	}

	@Override
	public Rotation rotation() {
		return new Rotation(RotationUtil.DEFAULT_ORDER, -Math.toRadians(wrapper.rotationYaw) - Math.PI, -Math.toRadians(wrapper.rotationPitch), 0);
	}

	@Override
	public void setRotation(Rotation rotation) {
		double[] euler = rotation.getAngles(RotationUtil.DEFAULT_ORDER);
		wrapper.rotationYaw = (float) Math.toDegrees(euler[0]);
		wrapper.rotationPitch = (float) Math.toDegrees(euler[1]);
	}

	@Override
	public String getUniqueID() {
		return wrapper.getUniqueID().toString();
	}
}
