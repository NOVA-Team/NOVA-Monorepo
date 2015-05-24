package nova.core.util.transform;

import nova.core.util.component.ComponentProvider;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * A 3D transform
 * @author Calclavia
 */
public class Transform3d extends Transform<Transform3d, Vector3d, Quaternion> {

	public Transform3d(ComponentProvider provider) {
		super(provider, Vector3d.zero, Quaternion.identity, Vector3d.one);
	}
}
