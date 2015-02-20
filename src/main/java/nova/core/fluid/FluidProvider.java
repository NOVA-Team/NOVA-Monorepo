package nova.core.fluid;

import java.util.Optional;

/**
 * Objects with this interface declare their ability to provide {@link Fluid FluidStacks}
 *
 * @see FluidConsumer
 */
public interface FluidProvider {
	/**
	 * Attempt to extract fluid from this FluidProvider
	 *
	 * @param fluid Fluid to extract
	 * @param simulate Whether to simulate the extraction
	 * @return Extracted {@link Fluid}
	 */
	public Optional<Fluid> removeFluid(Fluid fluid, boolean simulate);

	/**
	 * Attempt to extract fluid from this FluidProvider
	 *
	 * @param fluid Fluid to extract
	 * @return Extracted {@link Fluid}
	 */
	default public Optional<Fluid> removeFluid(Fluid fluid) {
		return removeFluid(fluid, false);
	}
}
