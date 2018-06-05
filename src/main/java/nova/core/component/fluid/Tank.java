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

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Classes with this interface declare ability to store fluids
 * @see FluidConsumer
 * @see Tank
 */
public interface Tank extends FluidIO {

	/**
	 * @return Maximum capacity of this container
	 */
	OptionalInt getFluidCapacity();

	/**
	 * @return Fluid stored in this container
	 */
	Optional<Fluid> getFluid();

	@Override
	default int getFluidAmount() {
		return hasFluid() ? getFluid().get().amount() : 0;
	}

	@Override
	default boolean isEmpty() {
		return !getFluid().isPresent();
	}

	@Override
	default boolean hasFluid() {
		return getFluid().isPresent();
	}

	@Override
	default boolean hasFluidType(String fluidID) {
		if (hasFluid()) {
			return getFluid().get().getID().equals(fluidID);
		}

		return false;
	}

	@Override
	default boolean hasFluidType(Fluid sample) {
		if (hasFluid()) {
			return getFluid().get().sameType(sample);
		}

		return false;
	}

	@Override
	default boolean hasFluidType(FluidFactory sample) {
		if (hasFluid()) {
			return getFluid().get().sameType(sample);
		}

		return false;
	}

	default Optional<String> getTag() {
		return Optional.empty();
	}
}
