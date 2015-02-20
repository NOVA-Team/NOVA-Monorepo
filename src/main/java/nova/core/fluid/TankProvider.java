package nova.core.fluid;

import nova.core.util.Direction;

import java.util.Optional;

/**
 * An object that provides a fluid container.
 *
 * @author Calclavia
 */
public interface TankProvider {
	Optional<Tank> getTank(Direction dir);
}
