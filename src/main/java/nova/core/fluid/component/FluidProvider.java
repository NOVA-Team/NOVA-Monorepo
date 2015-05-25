package nova.core.fluid.component;

import nova.core.fluid.Fluid;

import java.util.Optional;

/**
 * Objects with this interface declare their ability to provide {@link Fluid FluidStacks}
 * @see FluidConsumer
 */
public interface FluidProvider {
	/**
	 * Attempt to extract fluid from this FluidProvider
	 * @param amount Amount of fluid to extract
	 * @param simulate Whether to simulate the extraction
	 * @return Extracted {@link Fluid}
	 */
	public Optional<Fluid> removeFluid(int amount, boolean simulate);

	/**
	 * Attempt to extract fluid from this FluidProvider
	 * @param amount Amount of fluid to extract
	 * @return Extracted {@link Fluid}
	 */
	default public Optional<Fluid> removeFluid(int amount) {
		return removeFluid(amount, false);
	}
}
