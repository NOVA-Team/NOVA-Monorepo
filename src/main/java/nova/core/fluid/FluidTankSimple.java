package nova.core.fluid;

import java.util.Optional;

public class FluidTankSimple implements FluidContainer {

	private Optional<FluidStack> containedFluid;
	private int maxCapacity;
	
	@Override
	public Optional<FluidStack> consumeFluid(FluidStack fluidStack, boolean simulate) {
		int capacity = maxCapacity - containedFluid.orElse(fluidStack.withAmount(0)).getStackSize();
		int toPut = Math.min(fluidStack.getStackSize(), capacity);
		
		if(containedFluid.isPresent()) {
			if(!containedFluid.get().sameStackType(fluidStack))
				return Optional.of(fluidStack.clone());			
			if(!simulate)
				containedFluid.get().addStackSize(toPut);
		} else if(!simulate) {
				containedFluid = Optional.of(fluidStack.withAmount(toPut));
		}
		
		if(fluidStack.getStackSize() - toPut > 0)
			return Optional.of(fluidStack.withAmount(fluidStack.getStackSize() - toPut));
		else
			return Optional.empty();
	}

	@Override
	public Optional<FluidStack> extractFluid(Fluid fluid, int amount, boolean simulate) {
		if(!containedFluid.isPresent() || containedFluid.get().getFluid() != fluid)
			return Optional.empty();
		
		int toGet = Math.min(amount, containedFluid.get().getStackSize());
		
		if(!simulate)
			containedFluid.get().subStackSize(toGet);
		
		if(toGet > 0)
			return Optional.of(new FluidStack(fluid, toGet));
		else
			return Optional.empty();
	}

	@Override
	public int getMaximumCapacity() {
		return maxCapacity;
	}

	@Override
	public Optional<FluidStack> getStoredFluid() {
		return containedFluid;
	}

}
