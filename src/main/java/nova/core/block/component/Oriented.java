package nova.core.block.component;

import nova.core.block.Stateful;
import nova.core.component.Component;
import nova.core.network.Sync;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.Direction;

/**
 * A component that is applied to blocks with specific orientations.
 * @author Calclavia
 */
public class Oriented extends Component implements Storable, Stateful {

	/**
	 * The allowed rotation directions the block can face.
	 */
	public int rotationMask = 0x3C;

	/**
	 * The direction the block is facing.
	 */
	@Sync
	@Stored
	public Direction direction = Direction.UNKNOWN;
}
