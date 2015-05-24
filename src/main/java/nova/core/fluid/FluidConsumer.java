package nova.core.fluid;

import nova.core.component.Component;

/**
 * Objects with this interface declare their ability to consume {@link Fluid FluidStacks}
 * @see FluidConsumer
 */
public interface FluidConsumer extends Component {
	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @param simulate Whether to simulate the insertion
	 * @return The amount actually filled
	 */
	public int addFluid(Fluid fluid, boolean simulate);

	/**
	 * Attempt to insert fluid into this consumer
	 * @param fluid {@link Fluid} to insert
	 * @return Left {@link Fluid}
	 */
	public default int addFluid(Fluid fluid) {
		return addFluid(fluid, false);
	}
}
