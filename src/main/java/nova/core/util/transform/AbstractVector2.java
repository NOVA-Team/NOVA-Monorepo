package nova.core.util.transform;

/**
 * An abstract Vector3 class that is extended by both Vector3i and Vector3d
 *
 * @author Calclavia
 */
public abstract class AbstractVector2<I extends AbstractVector2<I, O>, O extends I> extends Vector<I, O> implements Comparable
{
	/**
	 * Integer coordinate values
	 */
	public abstract int xi();

	public abstract int yi();

	/**
	 * Double coordinate values
	 */
	public abstract double xd();

	public abstract double yd();

	@Override
	public double dot(I other)
	{
		return xd() * other.xd() + yd() * other.yd();
	}

	@Override
	public int compareTo(Object o)
	{
		if (o instanceof AbstractVector2)
		{
			AbstractVector2 other = (AbstractVector2) o;

			if (xd() < other.xd() || yd() < other.yd())
			{
				return -1;
			}

			if (xd() > other.xd() || yd() > other.yd())
			{
				return 1;
			}
		}
		return 0;
	}
}
