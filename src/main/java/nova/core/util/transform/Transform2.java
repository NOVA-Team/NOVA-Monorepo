package nova.core.util.transform;

import nova.core.util.transform.vector.Vector2d;

/**
 * A 2D transform
 * @author Calclavia
 */
public class Transform2 extends Transform<Transform2, Vector2d, Integer> {

	public Transform2() {
		super(Vector2d.zero, 0, Vector2d.one);
	}
}
