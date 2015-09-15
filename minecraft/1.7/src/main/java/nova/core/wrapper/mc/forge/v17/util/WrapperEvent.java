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

package nova.core.wrapper.mc.forge.v17.util;

import nova.core.event.BlockEvent;
import nova.core.util.Direction;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Calclavia
 */
public class WrapperEvent {

	public static class RedstoneConnect extends BlockEvent {
		public final Direction direction;
		public boolean canConnect;

		public RedstoneConnect(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class StrongRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public StrongRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}

	public static class WeakRedstone extends BlockEvent {
		public final Direction direction;
		public int power;

		public WeakRedstone(World world, Vector3D position, Direction direction) {
			super(world, position);
			this.direction = direction;
		}
	}
}
