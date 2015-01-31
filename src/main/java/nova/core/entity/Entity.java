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
	 * Initializes this Entity
	 */
	public void initialize() {

	}

	/**
	 * Marks this entity as valid
	 */
	public void validate() {
		valid = true;
	}

	/**
	 * Marks this entity as invalid
	 */
	public void invalidate() {
		valid = false;
	}

	/**
	 * Specifies whether this entity is valid
	 * @return Validity state
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Returns unique id of this entity
	 * @return Unique ID
	 */
	public int getUniqueID() {
		return uniqueId;
	}

	/**
	 * Gets world of this entity
	 * @return The {@link World}
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets position of this entity
	 * @return {@link Vector3d} containing position in world of this entity
	 */
	public Vector3d getPosition() {
		return position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	/**
	 * Sets world of this entity
	 * @param world World to set
	 * @return Whether succeed
	 */
	public boolean setWorld(World world) {
		this.world = world;
		return true;
	}

	/**
	 * Sets position of this entity
	 * @param position Position to set
	 * @return Whether succeed
	 */
	public boolean setPosition(Vector3d position) {
		this.position = position;
		return true;
	}

	public boolean setRotation(Quaternion rotation) {
		this.rotation = rotation;
		return true;
	}

}
