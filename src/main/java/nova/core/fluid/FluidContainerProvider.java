package nova.core.fluid;

import java.util.Optional;

/**
 * An object that provides a fluid container, not necessarily a block.
 *
 * @author Calclavia
 */
public interface FluidContainerProvider {
	//TODO: Return set of tanks?
	Optional<Tank> getTank();
}
