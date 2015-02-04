package nova.core.entity.components;

import nova.core.entity.Entity;
import nova.core.util.transform.Cuboid;

import java.util.Collection;

/**
 * Implement this on a Block or Entity which can collide with
 * another entity.
 */
public interface Collidable {
	/**
	 * This method is used to get box of this entity.
	 *
	 * @return {@link java.util.Collection} of {@link nova.core.util.transform.Cuboid}s making up the collision box.
	 */
	Collection<Cuboid> getCollisionBoxes();

	/**
	 * Called when this entity has collided with another.
	 *
	 * @param collidedWith The entity that this has collided with.
	 */
	void onCollide(Entity collidedWith);
}
