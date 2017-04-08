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

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ExE Boss
 */
public class FluidHandlerWrapper extends FluidHandler {

	private final FluidHandler handler;
	private final Predicate<Tank> tankFilter;

	public FluidHandlerWrapper(FluidHandler handler, Predicate<Tank> tankFilter) {
		this.handler = handler;
		this.tankFilter = tankFilter;
	}

	@Override
	public Optional<Fluid> removeFluid(Fluid fluid, boolean simulate) {
		if (fluid.amount() == 0)
			return Optional.empty();

		Fluid f = fluid.withAmount(0);
		Fluid r = fluid.clone();

		for (Tank tank : getTanks()) {
			if (!tank.hasFluidType(fluid))
				continue;

			int removed = tank.removeFluid(r).get().amount();
			r.remove(removed);
			f.add(removed);

			if (r.amount() == 0)
				break;
		}

		return Optional.of(f).filter(fl -> fl.amount() > 0);
	}

	@Override
	public Optional<Fluid> removeFluid(int amount, boolean simulate) {
		Optional<Fluid> fluid = getTanks().stream()
			.filter(Tank::hasFluid)
			.findFirst()
			.flatMap(Tank::getFluid)
			.map(f -> f.withAmount(0));

		if (amount == 0 || !fluid.isPresent())
			return fluid;

		Fluid f = fluid.get();

		for (Tank tank : getTanks()) {
			if (!tank.hasFluidType(f))
				continue;

			int removed = tank.removeFluid(amount).get().amount();
			amount -= removed;
			f.add(removed);

			if (amount == 0)
				break;
		}

		return Optional.of(f).filter(fl -> fl.amount() > 0);
	}

	@Override
	public int addFluid(Fluid fluid, boolean simulate) {
		if (fluid.amount() == 0)
			return 0;

		Fluid f = fluid.clone();
		int added = 0;

		for (Tank tank : getTanks()) {
			int a = tank.addFluid(f, simulate);
			added += a;
			f.remove(a);
			if (f.amount() == 0)
				break;
		}

		return added;
	}

	@Override
	public Set<Tank> getTanks() {
		return handler.getTanks().stream().filter(tankFilter).collect(Collectors.toSet());
	}
}
