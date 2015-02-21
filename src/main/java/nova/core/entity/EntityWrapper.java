package nova.core.entity;

import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.world.World;

/**
 * Used only by wrappers. Entity method calls will forward to this wrapper.
 *
 * @author Calclavia
 */
public interface EntityWrapper {

	boolean isValid();

	World world();

	Vector3d position();

	Quaternion rotation();

	void setWorld(World world);

	void setPosition(Vector3d position);

	void setRotation(Quaternion rotation);
}