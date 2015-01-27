package nova.core.util.transform;

/**
 * A double implementation of Vector2. Vector2 is an immutable quantity that holds an x, y and z value.
 *
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public class Vector2d extends Vector2<Vector2d>
{
	public final double x, y;

	public Vector2d()
	{
		this(0, 0);
	}

	public Vector2d(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public Vector2d add(Vector2 other)
	{
		return new Vector2d(x + other.xd(), y + other.yd());
	}

	@Override
	public Vector2d add(double other)
	{
		return new Vector2d(x + other, y + other);
	}

	@Override
	public Vector2d multiply(Vector2 other)
	{
		return new Vector2d(x * other.xd(), y * other.yd());
	}

	@Override
	public Vector2d multiply(double other)
	{
		return new Vector2d(x * other, y * other);
	}

	@Override
	public Vector2d reciprocal()
	{
		return new Vector2d(1 / x, 1 / y);
	}

	@Override
	public Vector2d max(Vector2 other)
	{
		return new Vector2d(Math.max(xd(), other.xd()), Math.max(yd(), other.yd()));
	}

	@Override
	public Vector2d min(Vector2 other)
	{
		return new Vector2d(Math.min(xd(), other.xd()), Math.min(yd(), other.yd()));
	}

	public double slope(Vector2 other)
	{
		return (y - other.yd()) / (x - other.xd());
	}

	@Override
	public int hashCode()
	{
		long x = Double.doubleToLongBits(this.x);
		long y = Double.doubleToLongBits(this.y);
		long hash = (x ^ (x >>> 32));
		hash = 31 * hash + y ^ (y >>> 32);
		return (int) hash;
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
	public double xd()
	{
		return x;
	}

	@Override
	public double yd()
	{
		return y;
	}
}
