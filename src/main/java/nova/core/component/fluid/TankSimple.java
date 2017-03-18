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

import nova.core.network.Packet;
import nova.core.network.Syncable;
import nova.core.retention.Data;
import nova.core.retention.Storable;
import nova.core.util.math.MathUtil;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;

/**
 * This class provides basic implementation of {@link Tank}
 */
public class TankSimple implements Tank, Storable, Syncable {

	private Optional<Fluid> containedFluid = Optional.empty();
	private OptionalInt capacity;
	private Predicate<Fluid> fluidFilter = f -> true;

	public TankSimple() {
		this.capacity = OptionalInt.empty();
	}

	public TankSimple(int maxCapacity) {
		this.capacity = OptionalInt.of(maxCapacity);
	}

	public TankSimple removeCapacity() {
		this.capacity = OptionalInt.empty();
		return this;
	}

	public TankSimple setCapacity(int capacity) {
		this.capacity = OptionalInt.of(capacity);
		setFluid(containedFluid);
		return this;
	}

	@Override
	public int addFluid(Fluid fluid, boolean simulate) {
		if (fluid.amount() == 0 || !fluidFilter.test(fluid))
			return 0;

		int capacity = this.capacity.orElse(Integer.MAX_VALUE) - containedFluid.orElseGet(() -> fluid.withAmount(0)).amount();
		int toPut = Math.min(fluid.amount(), capacity);

		if (containedFluid.isPresent()) {
			if (!containedFluid.get().sameType(fluid)) {
				return 0;
			}
			if (!simulate) {
				containedFluid.get().add(toPut);
			}
		} else if (!simulate) {
			containedFluid = Optional.of(fluid.withAmount(toPut));
		}

		if (fluid.amount() - toPut > 0) {
			return toPut;
		} else {
			return fluid.amount();
		}
	}

	@Override
	public Optional<Fluid> removeFluid(Fluid fluid, boolean simulate) {
		if (!containedFluid.filter(fluid::sameType).isPresent())
			return Optional.empty();

		return removeFluid(fluid.amount(), simulate);
	}

	@Override
	public Optional<Fluid> removeFluid(int amount, boolean simulate) {
		if (!containedFluid.isPresent()) {
			return Optional.empty();
		}

		int toGet = Math.min(amount, containedFluid.get().amount());

		Optional<Fluid> fluid = containedFluid;
		if (!simulate) {
			if (containedFluid.get().amount() > toGet) {
				containedFluid.get().remove(toGet);
			} else {
				containedFluid = Optional.empty();
			}
		}

		if (toGet > 0) {
			return Optional.of(fluid.get().withAmount(toGet));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public int getFluidCapacity() {
		return capacity.orElse(Integer.MAX_VALUE);
	}

	@Override
	public Optional<Fluid> getFluid() {
		return containedFluid;
	}

	public TankSimple setFluidFilter(Predicate<Fluid> fluidFilter) {
		this.fluidFilter = fluidFilter;
		return this;
	}

	public TankSimple setFluid(Optional<Fluid> fluid) {
		containedFluid = fluid.filter(fluidFilter).map(f -> f.withAmount(MathUtil.clamp(f.amount(), 0, capacity.orElse(Integer.MAX_VALUE)))).filter(f -> f.amount() > 0);
		return this;
	}

	@Override
	public void save(Data data) {
		data.put("capacity", capacity);

		if (containedFluid.isPresent()) {
			data.put("fluid", containedFluid.get());
		}
	}

	@Override
	public void load(Data data) {
		setCapacity(data.get("capacity"));

		if (data.containsKey("fluid")) {
			containedFluid = Optional.of(data.getStorable("fluid"));
		} else {
			containedFluid = Optional.empty();
		}
	}

	@Override
	public void read(Packet packet) {
		if (containedFluid.isPresent()) {
			packet.write(containedFluid.get());
		}
	}

	@Override
	public void write(Packet packet) {
		containedFluid = Optional.of((Fluid) packet.readStorable());
	}
}
