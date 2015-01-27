package nova.core.util.vector;

/**
 * An integer implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 */
public class Vector3i extends Vector3<Vector3i>
{
	public final int x, y, z;

	public Vector3i()
	{
		this(0, 0, 0);
	}

	public Vector3i(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i add(Vector3 other)
	{
		return new Vector3i(x + other.xi(), y + other.yi(), z + other.zi());
	}

	@Override
	public Vector3i add(double other)
	{
		return new Vector3i(x + (int) other, y + (int) other, z + (int) other);
	}

	@Override
	public Vector3i multiply(Vector3 other)
	{
		return new Vector3i(x * other.xi(), y * other.yi(), z * other.zi());
	}

	@Override
	public Vector3i multiply(double other)
	{
		return new Vector3i(x * (int) other, y * (int) other, z * (int) other);
	}

	@Override
	public Vector3i reciprocal()
	{
		return new Vector3i(1 / x, 1 / y, 1 / z);
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
		long hash = (x ^ (x >>> 32));
		hash = 31 * hash + y ^ (y >>> 32);
		hash = 31 * hash + z ^ (z >>> 32);
		return (int) hash;
	}

	@Override
	public int xi()
	{
		return x;
	}

	@Override
	public int yi()
	{
		return y;
	}

	@Override
	public int zi()
	{
		return z;
	}

	@Override
	public double xd()
	{
		return x;
	}

	@Override
	public double yd()
	{
		return y;
	}

	@Override
	public double zd()
	{
		return z;
	}

	public Vector3d toDouble()
	{
		return new Vector3d(xd(), yd(), zd());
	}
}
