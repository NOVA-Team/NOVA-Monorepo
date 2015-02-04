package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.util.Identifiable;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

public abstract class Entity implements Identifiable, Stateful {
	private final int uniqueId;
	private World world;
	private Vector3d position;
	private Quaternion rotation;
	private boolean valid;

	public Entity(int uniqueId, World world, Vector3d position) {
		this.uniqueId = uniqueId;
		setWorld(world);
		setPosition(position);
	}

	/**
	 * Initializes this Entity.
	 */
	public void awake() {

	}

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
	public int getUniqueID() {
		return uniqueId;
	}

	/**
	 * Gets the world of this entity.
	 *
	 * @return The {@link nova.core.world.World} that this entity is in.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets position of this entity.
	 *
	 * @return {@link nova.core.util.transform.Vector3d} containing the position in the world of this entity.
	 */
	public Vector3d getPosition() {
		return position;
	}

	public Quaternion getRotation() {
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
