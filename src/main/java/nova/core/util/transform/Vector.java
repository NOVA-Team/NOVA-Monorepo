package nova.core.util.transform;

/**
 * Represents an abstract vector with its mathematical operators.
 * Vector3 and Vector2 may extend this. Requires the Vector self type as a generic parameter.
 * @author Calclavia
 * @param <I> -describeme-
 * @param <O> -describeme-
 */
@SuppressWarnings("unchecked")
public abstract class Vector<I extends Vector<I, O>, O extends I> extends Operator<I, O> {
	/**
	 * Returns the dot product between this vector and the other.
	 * @param other The other vector
	 * @return The dot product
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
	 * @param other The other vector
	 * @return Angle
	 */
	public final double angle(I other) {
		return Math.acos(dot(other) / (magnitude() * other.magnitude()));
	}

	/**
	 * Finds the midpoint between this vector and another.
	 * @param other The other vector
	 * @return The midpoint
	 */
	public final O midpoint(I other) {
		return add(other).divide(2);
	}

	/**
	 * Returns the maximum vector of this vector and the other vector.
	 * @param other The other vector
	 * @return The maximum vector
	 */
	public abstract O max(I other);

	/**
	 * Returns the minimum vector of this vector and the other vector.
	 * @param other The other vector
	 * @return The minimum vector
	 */
	public abstract O min(I other);

	//Forces re-implemtation of equals()
	public abstract boolean equals(Object obj);

	public final O $tilde() {
		return normalize();
	}

	public O unary_$tilde() {
		return normalize();
	}

	public double $dot$times(I v) {
		return dot(v);
	}
}
