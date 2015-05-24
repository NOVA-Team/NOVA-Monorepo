package nova.core.util.transform;

import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;

/**
 * An interface applied to objects that can act as vector transformers.
 * @author Calclavia
 */
@FunctionalInterface
public interface Transform {
	/**
	 * Called to transform a vector.
	 * @param vec - The vector being transformed
	 * @return The transformed vector.
	 */
	Vector3d transform(Vector3<?> vec);
}
