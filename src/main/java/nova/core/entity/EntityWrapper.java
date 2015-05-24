package nova.core.entity;

import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;
import nova.core.world.PositionedWrapper;

/**
 * Used only by wrappers. Entity method calls will forward to this wrapper.
 * @author Calclavia
 */
public interface EntityWrapper extends PositionedWrapper<Vector3d>, RigidBody {

	void setRotation(Quaternion rotation);

	Quaternion rotation();
}