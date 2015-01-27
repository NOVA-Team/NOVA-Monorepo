package nova.core.entity.components;

import nova.core.entity.Entity;
import nova.core.util.transform.Cuboid;

import java.util.Collection;

/**
 * Implement this on a Block or Entity which can collide with
 * another entity.
 */
public interface Collidable {
	Collection<Cuboid> getCollisionBoxes();
	void onCollide(Entity collidedWith);
}
