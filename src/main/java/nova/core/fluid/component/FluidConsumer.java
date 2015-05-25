package nova.core.fluid.component;

import nova.core.fluid.Fluid;

/**
 * Objects with this interface declare their ability to consume {@link Fluid FluidStacks}
 * @see FluidConsumer
 */
public interface FluidConsumer {
	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @param simulate Whether to simulate the insertion
	 * @return The amount actually filled
	 */
	int addFluid(Fluid fluid, boolean simulate);

	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @return Left {@link Fluid}
	 */
	default int addFluid(Fluid fluid) {
		return addFluid(fluid, false);
	}
}
