package nova.core.fluid;

import java.util.Optional;

/**
 * This class provides basic implementation of {@link Tank}
 */
public class FluidTankSimple implements Tank {

	private Optional<Fluid> containedFluid;
	private int maxCapacity;

	@Override
	public Optional<Fluid> addFluid(Fluid fluid, boolean simulate) {
		int capacity = maxCapacity - containedFluid.orElse(fluid.withAmount(0)).amount();
		int toPut = Math.min(fluid.amount(), capacity);

		if (containedFluid.isPresent()) {
			if (!containedFluid.get().sameType(fluid)) {
				return Optional.of(fluid.clone());
			}
			if (!simulate) {
				containedFluid.get().add(toPut);
			}
		} else if (!simulate) {
			containedFluid = Optional.of(fluid.withAmount(toPut));
		}

		if (fluid.amount() - toPut > 0) {
			return Optional.of(fluid.withAmount(fluid.amount() - toPut));
		} else
			return Optional.empty();
	}

	@Override
	public Optional<Fluid> removeFluid(Fluid fluid, boolean simulate) {
		if (!containedFluid.isPresent() || containedFluid.get() != fluid)
			return Optional.empty();

		int toGet = Math.min(fluid.amount(), containedFluid.get().amount());

		if (!simulate) {
			containedFluid.get().remove(toGet);
		}

		if (toGet > 0) {
			return Optional.of(fluid.withAmount(toGet));
		} else
			return Optional.empty();
	}

	@Override
	public int getFluidCapacity() {
		return maxCapacity;
	}

	@Override
	public Optional<Fluid> getFluid() {
		return containedFluid;
	}

}
