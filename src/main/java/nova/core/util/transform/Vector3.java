package nova.core.util.transform;

/**
 * An abstract Vector3 class that is extended by both Vector3i and Vector3d
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public abstract class Vector3<O extends Vector3<O>> extends AbstractVector2<Vector3<O>, O> implements Comparable
{

	/**
	 * Integer coordinate values
	 */
	public abstract int zi();

	/**
	 * Double coordinate values
	 */
	public abstract double zd();

	@Override
	public final double dot(Vector3 other) {
		return xd() * other.xd() + yd() * other.yd() + zd() * other.zd();
	}

	/**
	 * Returns the cross product between this vector and the other.
	 * Calculated by finding the determinant of a 3x3 matrix.
	 *
	 * @return A vector representing the normal, perpendicular to these two vectors
	 */
	public abstract O cross(Vector3<?> other);

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
