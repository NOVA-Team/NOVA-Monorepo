package nova.wrapper.mc1710.forward.block;

import nova.core.block.BlockWrapper;
import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;

/**
 * @author Calclavia
 */
public class MCBlockWrapper implements BlockWrapper {

	public final World world;
	public final Vector3i position;

	public MCBlockWrapper(World world, Vector3i position) {
		this.world = world;
		this.position = position;
	}
}
