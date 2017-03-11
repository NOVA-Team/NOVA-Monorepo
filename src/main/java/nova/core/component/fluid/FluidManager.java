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

import nova.core.util.registry.FactoryManager;
import nova.core.util.registry.Registry;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class FluidManager extends FactoryManager<FluidManager, Fluid, FluidFactory> {
	public final FluidFactory water;
	public final FluidFactory lava;

	private FluidManager(Registry<FluidFactory> fluidRegistry) {
		super(fluidRegistry);
		//TODO: Too Minecraft specific. Implementation should be hidden.
		this.water = register("water", Fluid::new);
		this.lava = register("lava", Fluid::new);
	}

	@Override
	public FluidFactory register(String id, Class<? extends Fluid> type, Function<Class<?>, Optional<?>> mapping) {
		return register(new FluidFactory(id, type, fluid -> fluid, mapping));
	}

	@Override
	public FluidFactory register(String id, Class<? extends Fluid> type) {
		return register(new FluidFactory(id, type));
	}

	@Override
	public FluidFactory register(String id, Supplier<Fluid> constructor) {
		return register(new FluidFactory(id, constructor));
	}

	@Override
	public FluidFactory register(FluidFactory factory) {
		registry.register(factory);
		return factory;
	}

	@Override
	public void init() {
		//TODO: Implement
	}
}
