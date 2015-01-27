package nova.core.entity;

import nova.core.util.vector.Vector3d;
import nova.core.world.World;

public interface Entity {
	World getWorld();
	Vector3d getPosition();
	boolean setWorld(World world);
	boolean setPosition(Vector3d position);
}
