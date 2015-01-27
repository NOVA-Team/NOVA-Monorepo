package nova.core.util.vector;

/**
 * An integer implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 */
public class Vector3i extends Vector3 implements Comparable
{
	public final int x, y, z;

	public Vector3i(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i add(Vector3i other)
	{
		return new Vector3i(x + other.x, y + other.y, z + other.z);
	}

	@Override
	public Vector3i add(double other)
	{
		return new Vector3i(x + (int) other, y + (int) other, z + (int) other);
	}

	@Override
	public Vector3i multiply(Vector3i other)
	{
		return new Vector3i(x * other.x, y * other.y, z * other.z);
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

	@Override
	public double dot(Vector3i other)
	{
		return x * other.x + y * other.y + z * other.z;
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
	public int compareTo(Object o)
	{
		if (o instanceof Vector3i)
		{
			Vector3i other = (Vector3i) o;

			if (x < other.x || y < other.y || z < other.z)
				return -1;

			if (x > other.x || y > other.y || z > other.z)
				return 1;
		}
		return 0;
	}

	public Vector3d toDouble()
	{
		return new Vector3d(x, y, z);
	}
}
