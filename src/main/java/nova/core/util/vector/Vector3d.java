package nova.core.util.vector;

/**
 * A double implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 *
 * @author Calclavia
 */
public class Vector3d extends Vector3 implements Comparable
{
	public final double x, y, z;

	public Vector3d(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3d add(Vector3d other)
	{
		return new Vector3d(x + other.x, y + other.y, z + other.z);
	}

	public Vector3d add(Vector3i other)
	{
		return new Vector3d(x + other.x, y + other.y, z + other.z);
	}

	@Override
	public Vector3d add(double other)
	{
		return new Vector3d(x + other, y + other, z + other);
	}

	public Vector3d subtract(Vector3i other)
	{
		return new Vector3d(x - other.x, y - other.y, z - other.z);
	}

	@Override
	public Vector3d multiply(Vector3d other)
	{
		return new Vector3d(x * other.x, y * other.y, z * other.z);
	}

	@Override
	public Vector3d multiply(double other)
	{
		return new Vector3d(x * other, y * other, z * other);
	}

	/**
	 * Gets the reciprocal of this vector.
	 * Any value of zero will cause a division by zero error.
	 */
	public Vector3d reciprocal()
	{
		return new Vector3d(1 / x, 1 / y, 1 / z);
	}

	/**
	 * Returns the dot product between this vector and the other.
	 */
	@Override
	public double dot(Vector3d other)
	{
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Returns the cross product between this vector and the other.
	 * Calculated by finding the determinant of a 3x3 matrix.
	 *
	 * @return A vector representing the normal, perpendicular to these two vectors
	 */
	public Vector3d cross(Vector3d other)
	{
		return new Vector3d(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public Vector3d perpendicular()
	{
		if (z == 0)
			return zCross();

		return xCross();
	}

	public Vector3d xCross()
	{
		return new Vector3d(0, z, -y);
	}

	public Vector3d zCross()
	{
		return new Vector3d(-y, x, 0);
	}

	@Override
	public int hashCode()
	{
		long x = Double.doubleToLongBits(this.x);
		long y = Double.doubleToLongBits(this.y);
		long z = Double.doubleToLongBits(this.z);
		long hash = (x ^ (x >>> 32));
		hash = 31 * hash + y ^ (y >>> 32);
		hash = 31 * hash + z ^ (z >>> 32);
		return (int) hash;
	}

	@Override
	public int compareTo(Object o)
	{
		if (o instanceof Vector3d)
		{
			Vector3d other = (Vector3d) o;

			if (x < other.x || y < other.y || z < other.z)
				return -1;

			if (x > other.x || y > other.y || z > other.z)
				return 1;
		}
		return 0;
	}

	public Vector3i toInt()
	{
		return new Vector3i((int) x, (int) y, (int) z);
	}

	@Override
	public int xi()
	{
		return (int) x;
	}

	@Override
	public int yi()
	{
		return (int) y;
	}

	@Override
	public int zi()
	{
		return (int) z;
	}

	@Override
	public double xd()
	{
		return x;
	}

	@Override
	public double yd()
	{
		return 0;
	}

	@Override
	public double zd()
	{
		return 0;
	}
}
