package nova.core.entity;

import nova.core.util.vector.Vector3i;
import nova.core.world.World;

public interface CollidableEntity {
	void onCollision(Entity collidedWith);
}
