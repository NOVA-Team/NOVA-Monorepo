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

package nova.core.component.fluid;

import nova.core.util.registry.Factory;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public class FluidFactory extends Factory<FluidFactory, Fluid> {

	public FluidFactory(String id, Class<? extends Fluid> type, Function<Fluid, Fluid> processor, Function<Class<?>, Optional<?>> mapping) {
		super(id, type, processor, mapping);
	}

	public FluidFactory(String id, Class<? extends Fluid> type, Function<Fluid, Fluid> processor) {
		super(id, type, processor);
	}

	public FluidFactory(String id, Class<? extends Fluid> type) {
		super(id, type);
	}

	public FluidFactory(String id, Supplier<Fluid> constructor, Function<Fluid, Fluid> processor) {
		super(id, constructor, processor);
	}

	public FluidFactory(String id, Supplier<Fluid> constructor) {
		super(id, constructor);
	}

	@Override
	public Fluid build() {
		Fluid build = super.build();
		build.factory = this;
		return build;
	}

	@Override
	protected FluidFactory selfConstructor(String id, Supplier<Fluid> constructor, Function<Fluid, Fluid> processor) {
		return new FluidFactory(id, constructor, processor);
	}
}
