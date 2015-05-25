package nova.core.component.transform;

import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;

/**
 * A 3D transform.
 *
 * Note that the implemented Transform3d for entities require a constructor with type ComponentProvider.
 * @author Calclavia
 */
public class EntityTransform extends WorldTransform<Vector3d> {

	//The rotation of the transform. Can never be null.
	private Quaternion rotation;

	//The scale of the transform. Can never be null.
	private Vector3d scale;

	//The center of rotation.
	private Vector3d pivot;

	public Vector3d scale() {
		return scale;
	}

	public Vector3d pivot() {
		return pivot;
	}

	public void setPivot(Vector3d pivot) {
		this.pivot = pivot;
	}

	public Quaternion rotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
}
