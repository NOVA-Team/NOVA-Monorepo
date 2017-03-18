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

import nova.core.component.Component;
import nova.core.component.SidedComponent;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.retention.Store;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A sided component that provides a fluid container.
 * @author Calclavia
 */
@SidedComponent
public class FluidHandler extends Component implements FluidIO, Storable {

	protected Set<Tank> tanks = new HashSet<>();
	@Store
	protected boolean resizable;

	public static FluidHandler singleTank() {
		return new FluidHandler(false, new TankSimple(Fluid.BUCKET_VOLUME));
	}

	public static FluidHandler singleTank(int capacity) {
		return new FluidHandler(false, new TankSimple(capacity));
	}

	public static FluidHandler singleTank(Predicate<Fluid> fluidFilter) {
		return new FluidHandler(false, new TankSimple(Fluid.BUCKET_VOLUME).setFluidFilter(fluidFilter));
	}

	public static FluidHandler singleTank(int capacity, Predicate<Fluid> fluidFilter) {
		return new FluidHandler(false, new TankSimple(capacity).setFluidFilter(fluidFilter));
	}

	public FluidHandler() {
		this.resizable = true;
	}

	public FluidHandler(Tank... tanks) {
		this(true, tanks);
	}

	public FluidHandler(boolean resizable, Tank... tanks) {
		this.tanks.addAll(Arrays.asList(tanks));
		this.resizable = resizable;
		if (resizable) {
			this.tanks.removeIf(Tank::isEmpty);
		}
	}

	@Override
	public int addFluid(Fluid fluid, boolean simulate) {
		if (fluid.amount() == 0)
			return 0;

		Fluid f = fluid.clone();
		int added = 0;

		for (Tank tank : tanks) {
			int a = tank.addFluid(f, simulate);
			added += a;
			f.remove(a);
			if (f.amount() == 0)
				break;
		}

		if (f.amount() > 0 && resizable) {
			Tank t = new TankSimple();
			added += t.addFluid(f);
			tanks.add(t);
		}

		return added;
	}

	@Override
	public Optional<Fluid> removeFluid(Fluid fluid, boolean simulate) {
		if (fluid.amount() == 0)
			return Optional.empty();

		Fluid f = fluid.withAmount(0);
		Fluid r = fluid.clone();

		for (Tank tank : tanks) {
			if (!tank.hasFluidType(fluid))
				continue;

			int removed = tank.removeFluid(r).get().amount();
			r.remove(removed);
			f.add(removed);

			if (r.amount() == 0)
				break;
		}

		if (resizable) {
			this.tanks.removeIf(Tank::isEmpty);
		}

		return Optional.of(f).filter(fl -> fl.amount() > 0);
	}

	@Override
	public Optional<Fluid> removeFluid(int amount, boolean simulate) {
		Optional<Fluid> fluid = tanks.stream()
			.filter(Tank::hasFluid)
			.findFirst()
			.flatMap(Tank::getFluid)
			.map(f -> f.withAmount(0));

		if (amount == 0 || !fluid.isPresent())
			return fluid;

		Fluid f = fluid.get();

		for (Tank tank : tanks) {
			if (!tank.hasFluidType(f))
				continue;

			int removed = tank.removeFluid(amount).get().amount();
			amount -= removed;
			f.add(removed);

			if (amount == 0)
				break;
		}

		if (resizable) {
			this.tanks.removeIf(Tank::isEmpty);
		}

		return Optional.of(f).filter(fl -> fl.amount() > 0);
	}

	@Override
	public void save(Data data) {
		Storable.super.save(data);
		data.put("tanks", tanks.stream()
			.filter(t -> t instanceof Storable)
			.collect(Collectors.toSet()));
	}

	@Override
	public void load(Data data) {
		Storable.super.load(data);
		tanks.removeIf(t -> t instanceof Storable);
		tanks.addAll(data.getCollection("tanks"));
	}

	@Override
	public int getFluidAmount() {
		return tanks.stream().mapToInt(FluidIO::getFluidAmount).sum();
	}

	@Override
	public boolean isEmpty() {
		return tanks.isEmpty() || tanks.stream().allMatch(FluidIO::isEmpty);
	}

	@Override
	public boolean hasFluid() {
		return !tanks.isEmpty() && tanks.stream().anyMatch(FluidIO::hasFluid);
	}

	@Override
	public boolean hasFluidType(String fluidID) {
		return !tanks.isEmpty() && tanks.stream().anyMatch(t -> t.hasFluidType(fluidID));
	}

	@Override
	public boolean hasFluidType(Fluid sample) {
		return !tanks.isEmpty() && tanks.stream().anyMatch(t -> t.hasFluidType(sample));
	}

	@Override
	public boolean hasFluidType(FluidFactory sample) {
		return !tanks.isEmpty() && tanks.stream().anyMatch(t -> t.hasFluidType(sample));
	}
}
