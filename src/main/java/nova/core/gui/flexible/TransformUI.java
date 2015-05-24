package nova.core.gui.flexible;

import nova.core.util.transform.Transform2;
import nova.core.util.transform.vector.Vector2d;

/**
 * The transform used for user interfaces
 * @author Calclavia
 */
public class TransformUI extends Transform2 {

	/**
	 * The anchor is a unit vector representing the anchoring of the UI object.
	 */
	public Vector2d minAnchor = Vector2d.zero;

	public Vector2d maxAnchor = Vector2d.one;
}
