package nova.core.util.vector;

/**
 * @author Calclavia
 */
public interface VectorOperator<V extends VectorOperator<V>>
{
	V add(V other);

	V add(double other);

	default V subtract(V other)
	{
		return add(other.inverse());
	}

	default V subtract(double other)
	{
		return add(-other);
	}

	V multiply(V other);

	V multiply(double other);

	default V divide(V other)
	{
		return multiply(other.reciprocal());
	}

	default V divide(double other)
	{
		return multiply(1 / other);
	}

	V reciprocal();

	default V inverse()
	{
		return multiply(-1);
	}

	/**
	 * Returns the dot product between this vector and the other.
	 */
	double dot(V other);

	default double distance(V other)
	{
		return other.subtract((V) this).magnitude();
	}

	default V normalize()
	{
		return divide(magnitude());
	}

	default double magnitude()
	{
		return Math.sqrt(magnitudeSquared());
	}

	default double magnitudeSquared()
	{
		return dot((V) this);
	}

	/**
	 * Gets the angle between this vector and the other.
	 */
	default double angle(V other)
	{
		return Math.acos(dot(other) / (magnitude() * other.magnitude()));
	}

	/**
	 * Finds the midpoint between this vector and another.
	 */
	default V midpoint(V other)
	{
		return add(other).divide(2);
	}
}
