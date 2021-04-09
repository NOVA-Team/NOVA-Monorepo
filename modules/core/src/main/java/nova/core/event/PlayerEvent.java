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

import nova.core.entity.Entity;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * All events relevant to the player actions.
 * @author Calclavia
 */
public abstract class PlayerEvent extends EntityEvent {
	public PlayerEvent(Entity entity) {
		super(entity);
	}

	/**
	 * Event triggered when a player joins the server.
	 */
	public static class Join extends PlayerEvent {
		public Join(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event triggered when a player leaves the server.
	 */
	public static class Leave extends PlayerEvent {
		public Leave(Entity entity) {
			super(entity);
		}
	}

	/**
	 * Event triggered when a player interacts with the world.
	 */
	public static class Interact extends PlayerEvent {
		public final World world;
		public final Vector3D position;
		public final Action action;
		public Result useBlock = Result.DEFAULT;
		public Result useItem = Result.DEFAULT;

		public Interact(World world, Vector3D position, Entity player, Action action) {
			super(player);
			this.world = world;
			this.position = position;
			this.action = action;
		}

		public enum Action {
			RIGHT_CLICK_AIR,
			RIGHT_CLICK_BLOCK,
			LEFT_CLICK_BLOCK
		}
	}
}
