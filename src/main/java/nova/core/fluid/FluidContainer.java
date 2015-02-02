package nova.core.fluid;

import java.util.Optional;

/**
 * Classes with this interface declare ability of storing fluids
 * 
 * @see FluidConsumer
 * @see FluidContainer
 */
public interface FluidContainer extends FluidConsumer, FluidProvider {
	
	/**
	 * @return Maximum capacity of this container
	 */
	public int getMaximumCapacity();
	
	/**
	 * @return Fluid stored in this container
	 */
	public Optional<FluidStack> getStoredFluid();
	
	/**
	 * @return Whethet this container is storing a fluid
	 */
	public default boolean hasFluid() {
		return getStoredFluid().isPresent();
	}
	
}
