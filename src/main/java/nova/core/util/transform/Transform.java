package nova.core.util.transform;

import nova.core.util.components.Component;

import java.util.Optional;

/**
 * An object that handles the transformation of an object
 * @author Calclavia
 */
public abstract class Transform<S extends Transform<S, V, R>, V, R> implements Component {

	//The parent transform.
	public Optional<S> parent;

	//The position of the transform. Can never be null.
	public V position;

	//The rotation of the transform. Can never be null.
	public R rotation;

	//The scale of the transform. Can never be null.
	public V scale;

	/**
	 * Default values of transform
	 * @param position The default position
	 * @param rotation The default rotation
	 * @param scale The default scale
	 */
	public Transform(V position, R rotation, V scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
}
