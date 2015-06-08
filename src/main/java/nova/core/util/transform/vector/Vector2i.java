package nova.core.util.transform.vector;

import nova.core.retention.Storable;
import nova.core.retention.Store;
import nova.core.util.transform.matrix.Matrix;

/**
 * A double implementation of Vector2. Vector2 is an immutable quantity that
 * holds an x, y and z value.
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public class Vector2i extends Vector2<Vector2i> implements Storable {
	public static final Vector2i zero = new Vector2i(0, 0);
	public static final Vector2i one = new Vector2i(1, 1);
	public static final Vector2i xAxis = new Vector2i(1, 0);
	public static final Vector2i yAxis = new Vector2i(0, 1);
	public static final Vector2i max = new Vector2i(Integer.MAX_VALUE, Integer.MAX_VALUE);

	@Store
	public final int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Vector2i add(Vector2 other) {
		return new Vector2i(x + (int) other.xd(), y + (int) other.yd());
	}

	@Override
	public Vector2i add(double other) {
		return new Vector2i(x + (int) other, y + (int) other);
	}

	@Override
	public Vector2i multiply(double other) {
		return new Vector2i((int) (x * other), (int) (y * other));
	}

	@Override
	public Vector2i reciprocal() {
		return new Vector2i(1 / x, 1 / y);
	}

	@Override
	public Vector2i max(Vector2 other) {
		return new Vector2i(Math.max(xi(), other.xi()), Math.max(yi(), other.yi()));
	}

	@Override
	public Vector2i min(Vector2 other) {
		return new Vector2i(Math.min(xi(), other.xi()), Math.min(yi(), other.yi()));
	}

	public double slope(Vector2 other) {
		return (y - other.yd()) / (x - other.xd());
	}

	@Override
	public int hashCode() {
		long x = Double.doubleToLongBits(this.x);
		long y = Double.doubleToLongBits(this.y);
		long hash = (x ^ (x >>> 32));
		hash = 31 * hash + y ^ (y >>> 32);
		return (int) hash;
	}

	@Override
	public int xi() {
		return x;
	}

	@Override
	public int yi() {
		return y;
	}

	@Override
	public double xd() {
		return x;
	}

	@Override
	public double yd() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2) {
			Vector2 v = ((Vector2) obj);
			return xd() == v.xd() && yd() == v.yd();
		}
		return this == obj;
	}

	public Matrix toMatrix() {
		return new Matrix(new double[][] { { x }, { y } });
	}

	@Override
	public String toString() {
		return "[Vector2d] " + x + ", " + y;
	}
}
