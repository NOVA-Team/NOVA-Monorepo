package nova.core.util.transform;

import nova.core.util.collection.TreeNode;
import nova.core.world.component.Component;
import nova.core.world.component.ComponentProvider;

/**
 * An object that handles the transformation of an object
 * @author Calclavia
 */
public abstract class Transform<S extends Transform<S, V, R>, V, R> extends TreeNode<S> implements Component {

	public final ComponentProvider provider;

	//The position of the transform. Can never be null.
	private V position;

	//The rotation of the transform. Can never be null.
	private R rotation;

	//The scale of the transform. Can never be null.
	private V scale;

	//The center of rotation.
	private V pivot;

	/**
	 * Default values of transform
	 * @param position The default position
	 * @param rotation The default rotation
	 * @param scale The default scale
	 */
	public Transform(ComponentProvider provider, V position, R rotation, V scale) {
		this.provider = provider;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public V getPosition() {
		return position;
	}

	public void setPosition(V position) {
		this.position = position;
	}

	public V getScale() {
		return scale;
	}

	public void setScale(V scale) {
		this.scale = scale;
	}

	public V getPivot() {
		return pivot;
	}

	public void setPivot(V pivot) {
		this.pivot = pivot;
	}

	public R getRotation() {
		return rotation;
	}

	public void setRotation(R rotation) {
		this.rotation = rotation;
	}

	@Override
	public final ComponentProvider provider() {
		return provider;
	}

	/**
	 * Scala syntactic sugar coating
	 */
	public final V position() {
		return getPosition();
	}

	public final V scale() {
		return getScale();
	}

	public final V pivot() {
		return getPivot();
	}

	public final R rotation() {
		return getRotation();
	}

	public final void position_$eq(V position) {
		setPosition(position);
	}

	public final void rotation_$eq(R rotation) {
		setRotation(rotation);
	}

	public final void scale_$eq(V scale) {
		setScale(scale);
	}

	public final void pivot_$eq(V pivot) {
		setPivot(pivot);
	}
}
