package nova.core.fluid;

import java.util.Optional;

/**
 * Objects with this interface declare their ability to consume {@link FluidStack FluidStacks}
 * 
 * @see FluidConsumer
 */
public interface FluidConsumer {
	/**
	 * Attempt to insert fluid into this consumer
	 * 
	 * @param fluidStack {@link FluidStack} to insert
	 * @param simulate Whether to simulate the insertion
	 * @return Left {@link FluidStack}
	 */
	public Optional<FluidStack> consumeFluid(FluidStack fluidStack, boolean simulate);
	
	/**
	 * Attempt to insert fluid into this consumer
	 * 
	 * @param fluidStack {@link FluidStack} to insert
	 * @return Left {@link FluidStack}
	 */
	public default Optional<FluidStack> consumeFluid(FluidStack fluidStack) {
		return consumeFluid(fluidStack, false);
	}
}
