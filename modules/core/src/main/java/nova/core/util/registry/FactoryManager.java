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

package nova.core.util.registry;

import nova.core.util.Identifiable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A manager holds a registry of factories.
 * @author Calclavia
 */
//TODO: Registry methods should be encapsulated to the RegisterEvent
public abstract class FactoryManager<S extends FactoryManager<S, T, F>, T extends Identifiable, F extends Factory<F, T>> extends Manager<S> {
	public final Registry<F> registry;

	public FactoryManager(Registry<F> registry) {
		this.registry = registry;
	}

	/**
	 * Register a new object construction factory.
	 *
	 * Note that you make use: register(id, BlockStone.class), passing the class of the implementation.
	 * @param id The ID of the factory
	 * @param mapping The custom DI mapping
	 * @param type The class containing the implementation
	 * @return The factory
	 */
	public abstract F register(String id, Class<? extends T> type, Function<Class<?>, Optional<?>> mapping);

	/**
	 * Register a new object construction factory.
	 *
	 * Note that you make use: register(id, BlockStone.class), passing the class of the implementation.
	 * @param id The ID of the factory
	 * @param type The class containing the implementation
	 * @return The factory
	 */
	public abstract F register(String id, Class<? extends T> type);

	/**
	 * Register a new object construction factory.
	 *
	 * Note that you make use: register(BlockStone::new), passing a method reference.
	 * @param id The ID of the factory
	 * @param constructor Instance supplier {@link Supplier}
	 * @return The factory
	 */
	public abstract F register(String id, Supplier<T> constructor);

	/**
	 * Register a new object construction factory.
	 * @param factory The construction factory
	 * @return The factory
	 */
	public F register(F factory) {
		registry.register(factory);
		return factory;
	}

	/**
	 * Gets an object by its registered name.
	 * @param name Registered name
	 * @return The object
	 */
	public Optional<F> get(String name) {
		return registry.get(name);
	}
}
