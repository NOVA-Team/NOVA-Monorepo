package nova.core.util.transform;

/**
 * An abstract Vector3 class that is extended by both Vector3i and Vector3d
 *
 * @param <O> -describeme-
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public abstract class Vector3<O extends Vector3<O>> extends Vector<Vector3<O>, O> implements Comparable {
	/**
	 * Integer coordinate values
	 *
	 * @return -describeme-
	 */
	public abstract int xi();

	public abstract int yi();

	public abstract int zi();

	public final float xf() {
		return (float) xd();
	}

	public final float yf() {
		return (float) yd();
	}

	public final float zf() {
		return (float) zd();
	}

	/**
	 * Double coordinate values
	 *
	 * @return -describeme-
	 */
	public abstract double xd();

	public abstract double yd();

	public abstract double zd();

	@Override
	public final double dot(Vector3 other) {
		return xd() * other.xd() + yd() * other.yd() + zd() * other.zd();
	}

	/**
	 * Returns the cross product between this vector and the other.
	 * Calculated by finding the determinant of a 3x3 matrix.
	 *
	 * @param other Other vector
	 * @return A vector representing the normal, perpendicular to these two vectors
	 */
	public abstract O cross(Vector3<?> other);

	public O $times(Vector3<?> v) {
		return cross(v);
	}

	public O transform(Transform transform) {
		return (O) transform.transform(this);
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Vector3) {
			Vector3 other = (Vector3) o;

			if (xd() < other.xd() || yd() < other.yd() || zd() < other.zd()) {
				return -1;
			}

			if (xd() > other.xd() || yd() > other.yd() || zd() > other.zd()) {
				return 1;
			}
		}
		return 0;
	}
}
