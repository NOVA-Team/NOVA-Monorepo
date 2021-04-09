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

/**
 * Objects with this interface declare their ability to consume {@link Fluid FluidStacks}
 * @see FluidConsumer
 */
public interface FluidConsumer {
	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @param simulate Whether to simulate the insertion
	 * @return The amount actually filled
	 */
	int addFluid(Fluid fluid, boolean simulate);

	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @return Left {@link Fluid}
	 */
	default int addFluid(Fluid fluid) {
		return addFluid(fluid, false);
	}
}
