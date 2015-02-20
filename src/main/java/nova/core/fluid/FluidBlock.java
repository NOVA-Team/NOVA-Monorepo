package nova.core.fluid;

/**
 * An interface applied to blocks that are fluids
 *
 * @author Calclavia
 */
public interface FluidBlock {

	/**
	 * Returns the Fluid associated with this Block.
	 */
	Fluid getFluid();

	/**
	 * Attempt to drain the block. This method should be called by devices such as pumps.
	 *
	 * NOTE: The block is intended to handle its own state changes.
	 *
	 * @param doDrain If false, the drain will only be simulated.
	 * @return
	 */
	FluidStack drain(boolean doDrain);
}
