package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.render.model.Model;
import nova.core.util.Identifiable;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

public abstract class Entity implements Identifiable, Stateful {

	/**
	 * The wrapper is injected from EntityFactory.
	 * The wrapper may be null in cases where a backward wrapper is created for native entities. 
	 */
	public final EntityWrapper wrapper = null;

	/**
	 * The rigid body handler. This will never be null.
	 */
	public final RigidBody rigidBody = null;

	/**
	 * Renders the entity. Translation and rotation are already applied.
	 *
	 * @param model - The model object to render.
	 */
	public void render(Model model) {

	}

	/**
	 * Check if this entity is valid.
	 *
	 * @return Validity state (true or false).
	 */
	public boolean isValid() {
		return wrapper.isValid();
	}

	/**
	 * Gets the world of this entity.
	 *
	 * @return The {@link nova.core.world.World} that this entity is in.
	 */
	public World world() {
		return wrapper.world();
	}

	/**
	 * Gets position of this entity.
	 *
	 * @return {@link nova.core.util.transform.Vector3d} containing the position in the world of this entity.
	 */
	public Vector3d position() {
		return wrapper.position();
	}

	public Quaternion rotation() {
		return wrapper.rotation();
	}

	/**
	 * Sets the world of this entity.
	 *
	 * @param world World to set.
	 * @return {@code true} if successful.
	 */
	public Entity setWorld(World world) {
		wrapper.setWorld(world);
		return this;
	}

	/**
	 * Sets the position of this entity.
	 *
	 * @param position Position to set.
	 * @return {@code true} if successful.
	 */
	public Entity setPosition(Vector3d position) {
		wrapper.setPosition(position);
		return this;
	}

	/**
	 * Sets the rotation of this entity.
	 *
	 * @param rotation Position to set.
	 * @return {@code true} if successful.
	 */
	public Entity setRotation(Quaternion rotation) {
		wrapper.setRotation(rotation);
		return this;
	}

}
