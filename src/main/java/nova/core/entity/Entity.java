package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.util.Identifiable;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

public abstract class Entity implements Identifiable, Stateful {

	/**
	 * These values are injected from EntityFactory.
	 */
	private int id;
	private World world;
	private Vector3d position;
	private Quaternion rotation;
	private boolean valid;

	/**
	 * Marks this entity as valid.
	 */
	public void load() {
		valid = true;
	}

	/**
	 * Marks this entity as invalid.
	 */
	public void unload() {
		valid = false;
	}

	/**
	 * Check if this entity is valid.
	 *
	 * @return Validity state (true or false).
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Gets the unique id of this entity.
	 *
	 * @return Unique ID of this entity.
	 */
	public int id() {
		return id;
	}

	/**
	 * Gets the world of this entity.
	 *
	 * @return The {@link nova.core.world.World} that this entity is in.
	 */
	public World world() {
		return world;
	}

	/**
	 * Gets position of this entity.
	 *
	 * @return {@link nova.core.util.transform.Vector3d} containing the position in the world of this entity.
	 */
	public Vector3d position() {
		return position;
	}

	public Quaternion rotation() {
		return rotation;
	}

	/**
	 * Sets the world of this entity.
	 *
	 * @param world World to set.
	 * @return {@code true} if successful.
	 */
	public boolean setWorld(World world) {
		this.world = world;
		return true;
	}

	/**
	 * Sets the position of this entity.
	 *
	 * @param position Position to set.
	 * @return {@code true} if successful.
	 */
	public boolean setPosition(Vector3d position) {
		this.position = position;
		return true;
	}

	/**
	 * Sets the rotation of this entity.
	 *
	 * @param rotation Position to set.
	 * @return {@code true} if successful.
	 */
	public boolean setRotation(Quaternion rotation) {
		this.rotation = rotation;
		return true;
	}

}
