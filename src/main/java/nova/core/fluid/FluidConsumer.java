package nova.core.fluid;

import java.util.Optional;

/**
 * Objects with this interface declare their ability to consume {@link Fluid FluidStacks}
 *
 * @see FluidConsumer
 */
public interface FluidConsumer {
	/**
	 * Attempt to insert fluid into this consumer
	 *
	 * @param fluid {@link Fluid} to insert
	 * @param simulate Whether to simulate the insertion
	 * @return Left {@link Fluid}
	 */
	public Optional<Fluid> addFluid(Fluid fluid, boolean simulate);

	/**
	 * Attempt to insert fluid into this consumer
	 *
	 * @param fluid {@link Fluid} to insert
	 * @return Left {@link Fluid}
	 */
	public default Optional<Fluid> addFluid(Fluid fluid) {
		return addFluid(fluid, false);
	}
}
