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

	private Optional<Fluid> containedFluid = Optional.empty();
	private int capacity;

	public TankSimple(int maxCapacity) {
		this.capacity = maxCapacity;
	}

	public TankSimple setCapacity(int capacity) {
		this.capacity = capacity;
		setFluid(containedFluid);
		return this;
	}

	@Override
	public int addFluid(Fluid fluid, boolean simulate) {
		int capacity = this.capacity - containedFluid.orElse(fluid.withAmount(0)).amount();
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
	public Optional<Fluid> removeFluid(int amount, boolean simulate) {
		if (!containedFluid.isPresent()) {
			return Optional.empty();
		}

		int toGet = Math.min(amount, containedFluid.get().amount());

		Optional<Fluid> fluid = containedFluid;
		if (!simulate) {
			if(containedFluid.get().amount() > toGet) {
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
		return capacity;
	}

	@Override
	public Optional<Fluid> getFluid() {
		return containedFluid;
	}

	public TankSimple setFluid(Optional<Fluid> fluid) {
		this.containedFluid = fluid;
		if (containedFluid.isPresent()) {
			containedFluid.get().setAmount(Math.max(Math.min(containedFluid.get().amount(), capacity), 0));
		}
		return this;
	}

	@Override
	public void save(Data data) {
		if (containedFluid.isPresent()) {
			data.put("fluid", containedFluid.get());
		}
	}

	@Override
	public void load(Data data) {
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
		containedFluid = Optional.of(packet.readStorable());
	}
}
