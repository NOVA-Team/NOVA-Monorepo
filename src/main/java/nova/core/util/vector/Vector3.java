package nova.core.util.vector;

/**
 * An abstract Vector3 class that is extended by both Vector3i and Vector3d
 *
 * @author Calclavia
 */
public abstract class Vector3 implements VectorOperator<Vector3d>
{
	/**
	 * Integer coordinate values
	 */
	public abstract int xi();

	public abstract int yi();

	public abstract int zi();

	/**
	 * Double coordinate values
	 */
	public abstract double xd();

	public abstract double yd();

	public abstract double zd();
}
