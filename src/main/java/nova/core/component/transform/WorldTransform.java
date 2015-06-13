package nova.core.component.transform;

import nova.core.world.World;

/**
 * A transform that is linked withPriority a world.
 * @author Calclavia
 */
public class WorldTransform<P> extends PositionTransform<P> {

	private World world;

	public World world() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
