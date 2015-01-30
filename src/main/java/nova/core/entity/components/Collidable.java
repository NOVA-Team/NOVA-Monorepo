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
	 * This method is used to get box of this entity
	 *
	 * @return {@link Collection} of {@link Cuboid Cuboids} making up collision box
	 */
	Collection<Cuboid> getCollisionBoxes();

	/**
	 * Called when this entity collided with another
	 *
	 * @param collidedWith Another entity
	 */
	void onCollide(Entity collidedWith);
}
