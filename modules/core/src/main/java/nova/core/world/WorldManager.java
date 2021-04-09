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

package nova.core.world;

import nova.core.event.WorldEvent;
import nova.core.event.bus.GlobalEvents;
import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class WorldManager extends FactoryManager<WorldManager, World, WorldFactory> {

	/**
	 * The set of worlds that currently exist
	 */
	public final Set<World> clientWorlds = new HashSet<>();
	public final Set<World> serverWorlds = new HashSet<>();

	public WorldManager(Registry<WorldFactory> registry, GlobalEvents events) {
		super(registry);

		//Bind events
		events.on(WorldEvent.Load.class).bind(evt -> sidedWorlds().add(evt.world));
		events.on(WorldEvent.Unload.class).bind(evt -> sidedWorlds().remove(evt.world));
	}

	@Override
	public WorldFactory register(String id, Class<? extends World> type, Function<Class<?>, Optional<?>> mapping) {
		return register(new WorldFactory(id, type, world -> world, mapping));
	}

	@Override
	public WorldFactory register(String id, Class<? extends World> type) {
		return register(new WorldFactory(id, type));
	}

	@Override
	public WorldFactory register(String id, Supplier<World> constructor) {
		return register(new WorldFactory(id, constructor));
	}

	public Set<World> sidedWorlds() {
		if (Game.network().isClient()) {
			return clientWorlds;
		}
		return serverWorlds;
	}

	public Optional<World> findWorld(String id) {
		return sidedWorlds()
			.stream()
			.filter(world -> id.equals(world.getID()))
			.findFirst();
	}

	@Override
	public void init() {
		//TODO: Implement
	}
}
