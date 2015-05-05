package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.render.model.Model;
import nova.core.util.Identifiable;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.Positioned;

public abstract class Entity extends Positioned<EntityWrapper, Vector3d> implements Identifiable, Stateful {

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

	public Quaternion rotation() {
		return wrapper.rotation();
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
