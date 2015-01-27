package nova.core.entity;

import nova.core.util.vector.Vector3d;
import nova.core.world.World;

public class Entity {
	protected World world;
	protected Vector3d pos;

	public World getWorld() {
		return world;
	}

	public Vector3d getPosition() {
		return pos;
	}
}
