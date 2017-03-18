/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

import java.util.function.Predicate;

/**
 * A filter that only accepts a specific sub-type of {@link Fluid}. For use with tanks.
 *
 * @author ExE Boss
 */
@FunctionalInterface
public interface FluidFilter extends Predicate<Fluid> {

	/**
	 * Returns an {@link FluidFilter} that accepts an {@link Fluid} of the same
	 * type as the provided.
	 *
	 * @param item
	 * @return ItemFilter
	 */
	static FluidFilter of(Fluid fluid) {
		return fluid::sameType;
	}

	/**
	 * Returns an {@link FluidFilter} that accepts an {@link Fluid} of the same
	 * type as provided.
	 *
	 * @param id
	 * @return ItemFilter
	 */
	static FluidFilter of(String id) {
		return (other) -> id.equals(other.getID());
	}

	/**
	 * Accepts any {@link Fluid} that has a &gt;= amount than provided.
	 *
	 * @param amount
	 * @return
	 */
	static FluidFilter of(int amount) {
		return (other) -> other.amount() >= amount;
	}
}
