package nova.core.world;

import nova.core.block.BlockAccess;
import nova.core.util.Identifiable;
import nova.core.util.transform.Vector3i;

/**
 * A in-game world
 *
 * @see BlockAccess
 */
public abstract class World implements Identifiable, BlockAccess {

	/**
	 * Marks a position to render static.
	 *
	 * @param position - The position to perform the static re-rendering.
	 */
	public abstract void markStaticRender(Vector3i position);
}
