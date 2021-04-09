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

package nova.core.event;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.event.bus.CancelableEvent;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * All events related to the block.
 */
public abstract class BlockEvent extends CancelableEvent {
	//The world
	public final World world;
	//The position of the block
	public final Vector3D position;

	public BlockEvent(World world, Vector3D position) {
		this.world = world;
		this.position = position;
	}

	/**
	 * Event is triggered when a BlockFactory is registered.
	 *
	 * @see BlockFactory
	 * @see nova.core.block.BlockManager#register(nova.core.block.BlockFactory)
	 * @see nova.core.block.BlockManager#register(java.lang.String, java.util.function.Supplier)
	 */
	public static class Register extends CancelableEvent {
		public BlockFactory blockFactory;

		public Register(BlockFactory blockFactory) {
			this.blockFactory = blockFactory;
		}
	}

	/**
	 * Event is triggered when a block in the world changes.
	 */
	public static class Change extends BlockEvent {

		//The block that was in this position previously
		public final Block oldBlock;
		//The block that was set to in this position
		public final Block newBlock;

		public Change(World world, Vector3D position, Block oldBlock, Block newBlock) {
			super(world, position);
			this.newBlock = newBlock;
			this.oldBlock = oldBlock;
		}
	}
}
