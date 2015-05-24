package nova.core.gui.flexible;

import nova.core.component.ComponentProvider;
import nova.core.util.transform.Transform2d;
import nova.core.util.transform.vector.Vector2d;

/**
 * The transform used for UI Elements
 *
 * The anchor is a unit vector representing the anchoring of the UI object relative to the parent.
 *
 * Anchors have a fixed relationship with their transform.
 * Anchors have a flexible relationship with their parent transform, which they are anchored to.
 *
 * The distance between anchors and the corner of the transform is a fixed value.
 * The distance between the anchors and the corners of the parent transform is relative.
 *
 * Min anchor associates with the bottom left corner of the parent.
 * Max anchor associates with the top right corner of the parent.
 * @author Calclavia
 */
public class TransformUI extends Transform2d {

	public Vector2d minAnchor = Vector2d.zero;

	public Vector2d maxAnchor = Vector2d.one;

	public TransformUI(ComponentProvider provider) {
		super(provider);
	}
}
