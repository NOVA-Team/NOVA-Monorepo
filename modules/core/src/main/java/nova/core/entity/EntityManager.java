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

package nova.core.entity;

import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;
import nova.internal.core.Game;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityManager extends FactoryManager<EntityManager, Entity, EntityFactory> {

	private EntityManager(Registry<EntityFactory> registry) {
		super(registry);
	}

	/**
	 * Register a new entity type.
	 * @param id The entity ID
	 * @param type The class of the entity
	 * @param mapping The custom DI mapping
	 * @return The entity factory
	 */
	@Override
	public EntityFactory register(String id, Class<? extends Entity> type, Function<Class<?>, Optional<?>> mapping) {
		return register(new EntityFactory(id, type, entity -> entity, mapping));
	}

	/**
	 * Register a new entity type.
	 * @param id The entity ID
	 * @param type The class of the block
	 * @return The entity factory
	 */
	@Override
	public EntityFactory register(String id, Class<? extends Entity> type) {
		return register(new EntityFactory(id, type));
	}

	/**
	 * Register a new entity type.
	 * @param id The entity ID
	 * @param constructor Entity instance {@link Supplier}
	 * @return The entity factory
	 */
	@Override
	public EntityFactory register(String id, Supplier<Entity> constructor) {
		return register(new EntityFactory(id, constructor));
	}

	@Override
	public void init() {
		Game.events().publish(new Init(this));
	}

	public class Init extends ManagerEvent<EntityManager> {
		public Init(EntityManager manager) {
			super(manager);
		}
	}
}
