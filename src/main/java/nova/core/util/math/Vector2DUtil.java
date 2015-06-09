package nova.core.util.math;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

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
		return new Vector2D(FastMath.max(a.getX(), b.getX()), FastMath.max(a.getY(), b.getY()));
	}

	public static Vector2D min(Vector2D a, Vector2D b) {
		return new Vector2D(FastMath.min(a.getX(), b.getX()), FastMath.min(a.getY(), b.getY()));
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
		return new Vector2D(FastMath.round(vec.getX()), FastMath.round(vec.getY()));
	}

	public static Vector2D ceil(Vector2D vec) {
		return new Vector2D(FastMath.ceil(vec.getX()), FastMath.ceil(vec.getY()));
	}

	public static Vector2D floor(Vector2D vec) {
		return new Vector2D(FastMath.floor(vec.getX()), FastMath.floor(vec.getY()));
	}
}
