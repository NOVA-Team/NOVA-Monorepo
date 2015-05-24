package nova.core.world;

import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;

/**
 * Applied to any object that can have a position in the world.
 * @author Calclavia
 */
public class Positioned<W extends PositionedWrapper<V>, V extends Vector3> implements PositionedWrapper<V> {
	/**
	 * The wrapper is injected from positioned objectFactory.
	 * The wrapper may be null in cases where a backward wrapper is created for native entities.
	 */
	public final W wrapper = null;

	/**
	 * Gets the world of this positioned object.
	 * @return The {@link nova.core.world.World} that this positioned object is in.
	 */
	public World world() {
		return wrapper.world();
	}

	/**
	 * Gets position of this positioned object.
	 * @return {@link Vector3d} containing the position in the world of this positioned object.
	 */
	public V position() {
		return wrapper.position();
	}

	/**
	 * Sets the world of this positioned object.
	 * @param world World to set.
	 */
	public void setWorld(World world) {
		wrapper.setWorld(world);
	}

	/**
	 * Sets the position of this positioned object.
	 * @param position Position to set.
	 */
	public void setPosition(Vector3d position) {
		wrapper.setPosition(position);
	}
}
