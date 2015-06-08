package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Random;

/**
 * An extension of Apache Common's Vector2D class
 * @author Calclavia
 */
public class Vector2DUtil {

	public static final Vector2D ONE = new Vector2D(1, 1);
	public static final Vector2D CENTER = new Vector2D(0.5, 0.5);

	/**
	 * @return Creates a random unit vector
	 */
	public static Vector2D random() {
		Random random = new Random();
		return new Vector2D(random.nextDouble(), random.nextDouble()).scalarMultiply(2).subtract(ONE);
	}

	public static Vector2D max(Vector2D a, Vector2D b) {
		return new Vector2D(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()));
	}

	public static Vector2D min(Vector2D a, Vector2D b) {
		return new Vector2D(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()));
	}

	public static Vector2D midpoint(Vector2D a, Vector2D b) {
		return a.add(b).scalarMultiply(0.5);
	}

	public static Vector2D reciprocal(Vector2D vec) {
		return new Vector2D(1 / vec.getX(), 1 / vec.getY());
	}

	public static Vector2D perpendicular(Vector2D vec) {
		return reciprocal(vec).negate();
	}

	public static Vector2D round(Vector2D vec) {
		return new Vector2D(Math.round(vec.getX()), Math.round(vec.getY()));
	}

	public static Vector2D floor(Vector2D vec) {
		return new Vector2D((int) vec.getX(), (int) vec.getY());
	}
}
