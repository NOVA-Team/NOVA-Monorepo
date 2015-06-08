package nova.core.util.math;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Random;

/**
 * An extension of Apache Common's Vector3D class
 *
 * @author Calclavia
 */
public class VectorUtil extends Vector3D {

	public static final Vector3D ONE = new Vector3D(1, 1, 1);
	public static final Vector3D CENTER = new Vector3D(0.5, 0.5, 0.5);

	public VectorUtil(double x, double y, double z) {
		super(x, y, z);
	}

	public VectorUtil(double[] v) throws DimensionMismatchException {
		super(v);
	}

	public VectorUtil(double alpha, double delta) {
		super(alpha, delta);
	}

	public VectorUtil(double a, Vector3D u) {
		super(a, u);
	}

	public VectorUtil(double a1, Vector3D u1, double a2, Vector3D u2) {
		super(a1, u1, a2, u2);
	}

	public VectorUtil(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3) {
		super(a1, u1, a2, u2, a3, u3);
	}

	public VectorUtil(double a1, Vector3D u1, double a2, Vector3D u2, double a3, Vector3D u3, double a4, Vector3D u4) {
		super(a1, u1, a2, u2, a3, u3, a4, u4);
	}

	/**
	 * @return Creates a random unit vector
	 */
	public static Vector3D random() {
		Random random = new Random();
		return new Vector3D(random.nextDouble(), random.nextDouble(), random.nextDouble()).scalarMultiply(2).subtract(ONE);
	}

	public static Vector3D max(Vector3D a, Vector3D b) {
		return new Vector3D(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
	}

	public static Vector3D min(Vector3D a, Vector3D b) {
		return new Vector3D(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
	}

	public static Vector3D xCross(Vector3D vec) {
		return new Vector3D(0, vec.getZ(), -vec.getY());
	}

	public static Vector3D zCross(Vector3D vec) {
		return new Vector3D(-vec.getY(), vec.getX(), 0);
	}

	public static Vector3D midpoint(Vector3D a, Vector3D b) {
		return a.add(b).scalarMultiply(0.5);
	}

	public static Vector3D reciprocal(Vector3D vec) {
		return new Vector3D(1 / vec.getX(), 1 / vec.getY(), 1 / vec.getZ());
	}

	public static Vector3D perpendicular(Vector3D vec) {
		if (vec.getZ() == 0) {
			return zCross(vec);
		}

		return xCross(vec);
	}

	public static Vector3D round(Vector3D vec) {
		return new Vector3D(Math.round(vec.getX()), Math.round(vec.getY()), Math.round(vec.getZ()));
	}

	public static Vector3D floor(Vector3D vec) {
		return new Vector3D((int) vec.getX(), (int) vec.getY(), (int) vec.getZ());
	}
}
