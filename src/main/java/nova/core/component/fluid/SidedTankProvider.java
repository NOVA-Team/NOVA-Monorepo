package nova.core.component.fluid;

import nova.core.util.Direction;

import java.util.Set;

/**
 * A block that provides a fluid container.
 * @author Calclavia
 */
//TODO: Implement Component. There will need to be a better way to handle direction.
public interface SidedTankProvider {
	Set<Tank> getTank(Direction dir);
}
