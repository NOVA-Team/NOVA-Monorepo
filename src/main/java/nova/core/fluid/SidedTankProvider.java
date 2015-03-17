package nova.core.fluid;

import nova.core.util.Direction;

import java.util.Set;

/**
 * A block that provides a fluid container.
 * @author Calclavia
 */
public interface SidedTankProvider {
	Set<Tank> getTank(Direction dir);
}
