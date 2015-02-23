package nova.core.fluid;

import nova.core.network.Packet;
import nova.core.network.PacketHandler;
import nova.core.retention.Data;
import nova.core.retention.Storable;

import java.util.Optional;

/**
 * This class provides basic implementation of {@link Tank}
 */
public class TankSimple implements Tank, Storable, PacketHandler {

	private Optional<Fluid> containedFluid;
	private int maxCapacity;

	public TankSimple(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	@Override
	public int addFluid(Fluid fluid, boolean simulate) {
		int capacity = maxCapacity - containedFluid.orElse(fluid.withAmount(0)).amount();
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
			return 0;
		}
	}

	@Override
	public Optional<Fluid> removeFluid(int amount, boolean simulate) {
		if (!containedFluid.isPresent()) {
			return Optional.empty();
		}

		int toGet = Math.min(amount, containedFluid.get().amount());

		if (!simulate) {
			containedFluid.get().remove(toGet);
		}

		if (toGet > 0) {
			return Optional.of(containedFluid.get().withAmount(toGet));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public int getFluidCapacity() {
		return maxCapacity;
	}

	@Override
	public Optional<Fluid> getFluid() {
		return containedFluid;
	}

	@Override
	public void save(Data data) {
		if (containedFluid.isPresent()) {
			data.put("fluid", containedFluid);
		}
	}

	@Override
	public void load(Data data) {
		if (data.containsKey("fluid")) {
			containedFluid = Optional.of(data.getStorable(data.get("fluid")));
		} else {
			containedFluid = Optional.empty();
		}
	}

	@Override
	public void read(int id, Packet packet) {
		if (containedFluid.isPresent()) {
			containedFluid.get().save();
		}
	}

	@Override
	public void write(int id, Packet packet) {

	}
}
