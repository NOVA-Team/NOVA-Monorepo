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

import nova.core.util.registry.Factory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory for creating worlds.
 * @author Calclavia
 */
public class WorldFactory extends Factory<WorldFactory, World> {

	public WorldFactory(String id, Class<? extends World> type, Function<World, World> processor, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, processor, mapping);
	}

	public WorldFactory(String id, Class<? extends World> type, Function<World, World> processor) {
		super(id, type, processor);
	}

	public WorldFactory(String id, Class<? extends World> type) {
		super(id, type);
	}

	public WorldFactory(String id, Supplier<World> constructor, Function<World, World> processor) {
		super(id, constructor, processor);
	}

	public WorldFactory(String id, Supplier<World> constructor) {
		super(id, constructor);
	}

	@Override
	protected WorldFactory selfConstructor(String id, Supplier<World> constructor, Function<World, World> processor) {
		return new WorldFactory(id, constructor, processor);
	}
}
