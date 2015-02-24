package nova.core.fluid;

import nova.core.util.Direction;

import java.util.Optional;

/**
 * A block that provides a fluid container.
 * @author Calclavia
 */
public interface SidedTankProvider {
	//TODO: Return set of tanks?
	Optional<Tank> getTank(Direction dir);
}
