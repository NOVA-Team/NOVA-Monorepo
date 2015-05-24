package nova.core.util.transform.vector;

/**
 * An abstract Vector3 class that is extended by both Vector3i and Vector3d
 *
 * @param <O> -describeme-
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public abstract class Vector2<O extends Vector2<O>> extends Vector<Vector2<O>, O> implements Comparable {
	/**
	 * Integer coordinate values
	 *
	 * @return -describeme-
	 */
	public abstract int xi();

	public abstract int yi();

	/**
	 * Double coordinate values
	 *
	 * @return -describeme-
	 */
	public abstract double xd();

	public abstract double yd();

	@Override
	public double dot(Vector2 other) {
		return xd() * other.xd() + yd() * other.yd();
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Vector2) {
			Vector2 other = (Vector2) o;

			if (xd() < other.xd() || yd() < other.yd()) {
				return -1;
			}

			if (xd() > other.xd() || yd() > other.yd()) {
				return 1;
			}
		}
		return 0;
	}
}
