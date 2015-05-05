package nova.core.world;

import nova.core.util.transform.Vector3;
import nova.core.util.transform.Vector3d;

/**
 * @author Calclavia
 */
public interface PositionedWrapper<V extends Vector3> {

	World world();

	V position();

	void setWorld(World world);

	void setPosition(Vector3d position);
}
