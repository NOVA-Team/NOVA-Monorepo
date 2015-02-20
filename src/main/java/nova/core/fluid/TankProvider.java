package nova.core.fluid;

import nova.core.util.Direction;

/**
 * An object that provides a fluid container.
 *
 * @author Calclavia
 */
public interface TankProvider {
	FluidContainer getTank(Direction dir);
}
