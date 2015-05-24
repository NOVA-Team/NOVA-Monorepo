package nova.core.util.transform;

import nova.core.util.component.ComponentProvider;
import nova.core.util.transform.vector.Vector2d;

/**
 * A 2D transform
 * @author Calclavia
 */
public class Transform2d extends Transform<Transform2d, Vector2d, Integer> {

	public Transform2d(ComponentProvider provider) {
		super(provider, Vector2d.zero, 0, Vector2d.one);
	}
}
