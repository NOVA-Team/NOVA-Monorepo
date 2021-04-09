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

import nova.core.component.misc.FactoryProvider;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.registry.Factory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factory to create entities of specific type
 * @author Calclavia
 */
public class EntityFactory extends Factory<EntityFactory, Entity> {

	public EntityFactory(String id, Class<? extends Entity> type, Function<Entity, Entity> processor, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, processor, mapping);
	}

	public EntityFactory(String id, Class<? extends Entity> type, Function<Entity, Entity> processor) {
		super(id, type, processor);
	}

	public EntityFactory(String id, Class<? extends Entity> type) {
		super(id, type);
	}

	public EntityFactory(String id, Supplier<Entity> constructor, Function<Entity, Entity> processor) {
		super(id, constructor, processor);
	}

	public EntityFactory(String id, Supplier<Entity> constructor) {
		super(id, constructor);
	}

	@Override
	protected EntityFactory selfConstructor(String id, Supplier<Entity> constructor, Function<Entity, Entity> processor) {
		return new EntityFactory(id, constructor, processor);
	}

	@Override
	public Entity build() {
		Entity build = super.build();
		build.components.add(new FactoryProvider(this));
		return build;
	}
}
