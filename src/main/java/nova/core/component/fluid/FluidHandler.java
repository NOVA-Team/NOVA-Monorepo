/*
 * Copyright (c) 2017 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */
package nova.core.component.fluid;

import nova.core.component.Component;
import nova.core.retention.Storable;

import java.util.function.Predicate;

/**
 * @author ExE Boss
 */
public abstract class FluidHandler extends Component implements FluidIO, TankProvider, Storable {

	public static FluidHandler singleTank() {
		return FluidHandlerSimple.simpleSingleTank();
	}

	public static FluidHandler singleTank(int capacity) {
		return FluidHandlerSimple.simpleSingleTank(capacity);
	}

	public static FluidHandler singleTank(Predicate<Fluid> fluidFilter) {
		return FluidHandlerSimple.simpleSingleTank(fluidFilter);
	}

	public static FluidHandler singleTank(int capacity, Predicate<Fluid> fluidFilter) {
		return FluidHandlerSimple.simpleSingleTank(capacity, fluidFilter);
	}

	@Override
	public int getFluidAmount() {
		return getTanks().stream().mapToInt(Tank::getFluidAmount).sum();
	}

	@Override
	public boolean isEmpty() {
		return getTanks().isEmpty() || getTanks().stream().allMatch(Tank::isEmpty);
	}

	@Override
	public boolean hasFluid() {
		return !getTanks().isEmpty() && getTanks().stream().anyMatch(Tank::hasFluid);
	}

	@Override
	public boolean hasFluidType(String fluidID) {
		return !getTanks().isEmpty() && getTanks().stream().anyMatch(t -> t.hasFluidType(fluidID));
	}

	@Override
	public boolean hasFluidType(Fluid sample) {
		return !getTanks().isEmpty() && getTanks().stream().anyMatch(t -> t.hasFluidType(sample));
	}

	@Override
	public boolean hasFluidType(FluidFactory sample) {
		return !getTanks().isEmpty() && getTanks().stream().anyMatch(t -> t.hasFluidType(sample));
	}
}
