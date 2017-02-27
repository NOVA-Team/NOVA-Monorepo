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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import nova.core.block.Block;
import nova.core.component.transform.BlockTransform;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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

	@Override
	public void setWorld(World world) {
		this.world.removeBlock(position);
		world.setBlock(position, block.getFactory());
	}

	@Override
	public void setPosition(Vector3D position) {
		world.removeBlock(position);
		world.setBlock(position, block.getFactory());
	}
}
