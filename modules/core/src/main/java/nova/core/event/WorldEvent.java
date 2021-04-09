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

import nova.core.event.bus.Event;
import nova.core.world.World;

/**
 * All events related to the world.
 */
public abstract class WorldEvent extends Event {
	public final World world;

	public WorldEvent(World world) {
		this.world = world;
	}

	/**
	 * Event is triggered when a world loads.
	 */
	public static class Load extends WorldEvent {
		public Load(World world) {
			super(world);
		}
	}

	/**
	 * Event is triggered when a world unloads.
	 */
	public static class Unload extends WorldEvent {
		public Unload(World world) {
			super(world);
		}
	}
}