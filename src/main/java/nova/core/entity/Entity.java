package nova.core.entity;

import nova.core.block.components.Stateful;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

public abstract class Entity implements Identifiable, Stateful {
	private final int uniqueId;
	private World world;
	private Vector3d position;
	private boolean valid;

	public Entity(int uniqueId, World world, Vector3d position) {
		this.uniqueId = uniqueId;
		setWorld(world);
		setPosition(position);
	}

	public void initialize() {

	}

	public void validate() {
		valid = true;
	}

	public void invalidate() {
		valid = false;
	}

	public boolean isValid() {
		return valid;
	}

	public int getUniqueID() {
		return uniqueId;
	}

	public World getWorld() {
		return world;
	}

	public Vector3d getPosition() {
		return position;
	}

	public boolean setWorld(World world) {
		this.world = world;
		return true;
	}

	public boolean setPosition(Vector3d position) {
		this.position = position;
		return true;
	}
}
