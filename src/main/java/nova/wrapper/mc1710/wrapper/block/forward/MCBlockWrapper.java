package nova.wrapper.mc1710.wrapper.block.forward;

import nova.core.util.transform.vector.Vector3i;
import nova.core.world.World;
import nova.internal.dummy.Wrapper;

/**
 * @author Calclavia
 */
public class MCBlockWrapper extends Wrapper {
	public final World world;
	public final Vector3i position;

	public MCBlockWrapper(World world, Vector3i position) {
		this.world = world;
		this.position = position;
	}
}
