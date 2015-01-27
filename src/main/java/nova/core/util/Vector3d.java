package nova.core.util;

/**
 * A double implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 *
 * @author Calclavia
 */
public class Vector3d implements Comparable<Vector3d>
{
	public final double x, y, z;

	public Vector3d(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d add(Vector3d other)
	{
		return new Vector3d(x + other.x, y + other.y, z + other.z);
	}

	public Vector3d add(Vector3i other)
	{
		return new Vector3d(x + other.x, y + other.y, z + other.z);
	}

	public Vector3d add(double other)
	{
		return new Vector3d(x + other, y + other, z + other);
	}

	public Vector3d subtract(Vector3d other)
	{
		return new Vector3d(x - other.x, y - other.y, z - other.z);
	}

	public Vector3d subtract(Vector3i other)
	{
		return new Vector3d(x - other.x, y - other.y, z - other.z);
	}

	public Vector3d subtract(double other)
	{
		return add(-other);
	}

	public Vector3d multiply(Vector3d other)
	{
		return new Vector3d(x * other.x, y * other.y, z * other.z);
	}

	public Vector3d multiply(double other)
	{
		return new Vector3d(x * other, y * other, z * other);
	}

	public Vector3d divide(Vector3d other)
	{
		return multiply(other.reciprocal());
	}

	public Vector3d divide(double other)
	{
		return multiply(1 / other);
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
	 * Returns the inverse of this vector.
	 */
	public Vector3d inverse()
	{
		return multiply(-1);
	}

	public Vector3d midpoint(Vector3d other)
	{
		return add(other).divide(2);
	}

	public double distance(Vector3d other)
	{
		return other.subtract(this).magnitude();
	}

	public Vector3d normalize()
	{
		return divide(magnitude());
	}

	public double magnitude()
	{
		return Math.sqrt(magnitudeSquared());
	}

	public double magnitudeSquared()
	{
		return dot(this);
	}

	/**
	 * Returns the dot product between this vector and the other.
	 */
	public double dot(Vector3d other)
	{
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Gets the angle between this vector and the other.
	 */
	public double angle(Vector3d other)
	{
		return Math.acos(dot(other) / (magnitude() * other.magnitude()));
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
	public int compareTo(Vector3d o)
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
}
