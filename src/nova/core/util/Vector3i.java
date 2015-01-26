package nova.core.util;

/**
 * An integer implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 *
 */
public class Vector3i implements Comparable
{
	public final int x, y, z;

	public Vector3i(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
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
}
