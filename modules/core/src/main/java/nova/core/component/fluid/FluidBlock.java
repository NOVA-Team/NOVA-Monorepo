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
 */package nova.core.component.fluid;

import java.util.Optional;

/**
 * An interface applied to blocks that are fluids
 *
 * @author Calclavia
 */
public interface FluidBlock {

	/**
	 * Returns the Fluid associated with this Block.
	 * @return The Fluid associated with this Block.
	 */
	Optional<Fluid> getFluid();

	/**
	 * Attempt to drain the block. This method should be called by devices such as pumps.
	 *
	 * NOTE: The block is intended to handle its own state changes.
	 *
	 * @param doDrain If false, the drain will only be simulated.
	 * @return The drained fluid amount.
	 */
	Optional<Fluid> drain(boolean doDrain);
}
