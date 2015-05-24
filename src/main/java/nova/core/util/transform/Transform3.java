package nova.core.util.transform;

import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * A 3D transform
 * @author Calclavia
 */
public class Transform3 extends Transform<Transform3, Vector3d, Quaternion> {

	public Transform3() {
		super(Vector3d.zero, Quaternion.identity, Vector3d.one);
	}
}
