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

/**
 * Objects with this interface declare their ability to provide {@link Fluid FluidStacks}
 * @see FluidConsumer
 */
public interface FluidProvider {
	/**
	 * Attempt to extract fluid from this FluidProvider
	 * @param amount Amount of fluid to extract
	 * @param simulate Whether to simulate the extraction
	 * @return Extracted {@link Fluid}
	 */
	Optional<Fluid> removeFluid(int amount, boolean simulate);

	/**
	 * Attempt to extract fluid from this FluidProvider
	 * @param amount Amount of fluid to extract
	 * @return Extracted {@link Fluid}
	 */
	default Optional<Fluid> removeFluid(int amount) {
		return removeFluid(amount, false);
	}
}
