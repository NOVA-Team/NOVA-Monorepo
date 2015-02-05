package nova.core.fluid;

import java.util.Optional;

/**
 * Objects with this interface declare their ability to provide {@link FluidStack FluidStacks}
 *
 * @see FluidConsumer
 */
public interface FluidProvider {
	/**
	 * Attempt to extract fluid from this FluidProvider
	 *
	 * @param fluid Fluid to extract
	 * @param amount Amount of fluid to extract
	 * @param simulate Whether to simulate the extraction
	 * @return Extracted {@link FluidStack}
	 */
	public Optional<FluidStack> extractFluid(Fluid fluid, int amount, boolean simulate);

	/**
	 * Attempt to extract fluid from this FluidProvider
	 *
	 * @param fluid Fluid to extract
	 * @param amount Amount of fluid to extract
	 * @return Extracted {@link FluidStack}
	 */
	default public Optional<FluidStack> extractFluid(Fluid fluid, int amount) {
		return extractFluid(fluid, amount, false);
	}
}
