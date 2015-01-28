package nova.core.util.transform;

/**
 * Represents an abstract vector with its mathematical operators.
 * Vector3 and Vector2 may extend this. Requires the Vector self type as a generic parameter.
 * @author Calclavia
 */
@SuppressWarnings("unchecked")
public abstract class Vector<I extends Vector<I, O>, O extends I> extends Operator<I, O> {
	/**
	 * Returns the dot product between this vector and the other.
	 */
	public abstract double dot(I other);

	public final double distance(I other) {
		return other.subtract((I) this).magnitude();
	}

	public final O normalize() {
		return divide(magnitude());
	}

	public final double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}

	public final double magnitudeSquared() {
		return dot((I) this);
	}

	/**
	 * Gets the angle between this vector and the other.
	 */
	public final double angle(I other) {
		return Math.acos(dot(other) / (magnitude() * other.magnitude()));
	}

	/**
	 * Finds the midpoint between this vector and another.
	 */
	public final O midpoint(I other) {
		return add(other).divide(2);
	}

	/**
	 * Returns the maximum vector of this vector and the other vector.
	 */
	public abstract O max(I other);

	/**
	 * Returns the maximum vector of this vector and the other vector.
	 */
	public abstract O min(I other);

	//Forces re-implemtation of equals()
	public abstract boolean equals(Object obj);
}
