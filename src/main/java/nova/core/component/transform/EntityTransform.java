package nova.core.component.transform;

import nova.core.util.transform.matrix.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A 3D transform.
 *
 * Note that the implemented Transform3d for entities require a constructor with type ComponentProvider.
 * @author Calclavia
 */
public class EntityTransform extends WorldTransform<Vector3D> {

	//The rotation of the transform. Can never be null.
	private Rotation rotation;

	//The scale of the transform. Can never be null.
	private Vector3D scale;

	//The center of rotation.
	private Vector3D pivot;

	public Vector3D scale() {
		return scale;
	}

	public Vector3D pivot() {
		return pivot;
	}

	public void setPivot(Vector3D pivot) {
		this.pivot = pivot;
	}

	public void setScale(Vector3D scale) {
		this.scale = scale;
	}

	public Rotation rotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
}
