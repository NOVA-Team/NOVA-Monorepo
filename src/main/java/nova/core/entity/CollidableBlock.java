package nova.core.entity;

import nova.core.util.vector.Vector3i;
import nova.core.world.World;

public interface CollidableBlock {
	void onCollision(World world, Vector3i position, Entity collidedWith);
}
