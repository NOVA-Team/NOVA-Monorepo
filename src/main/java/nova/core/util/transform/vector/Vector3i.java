package nova.core.util.transform.vector;

import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.transform.matrix.Matrix;

/**
 * An integer implementation of Vector3. Vector3 is an immutable quantity that holds an x, y and z value.
 */
@SuppressWarnings("rawtypes")
public class Vector3i extends Vector3<Vector3i> implements Storable {
	public static final Vector3i zero = new Vector3i(0, 0, 0);
	public static final Vector3i one = new Vector3i(1, 1, 1);
	public static final Vector3i xAxis = new Vector3i(1, 0, 0);
	public static final Vector3i yAxis = new Vector3i(0, 1, 0);
	public static final Vector3i zAxis = new Vector3i(0, 0, 1);

	@Stored
	public final int x, y, z;

	public Vector3i() {
		this(0, 0, 0);
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i add(Vector3 other) {
		return new Vector3i(x + other.xi(), y + other.yi(), z + other.zi());
	}

	@Override
	public Vector3i add(double other) {
		return new Vector3i(x + (int) other, y + (int) other, z + (int) other);
	}

	@Override
	public Vector3i multiply(Vector3 other) {
		return new Vector3i(y * other.zi() - z * other.yi(), z * other.xi() - x * other.zi(), x * other.yi() - y * other.xi());
	}

	public Vector3i scale(Vector3 other) {
		return new Vector3i(x * other.xi(), y * other.yi(), z * other.zi());
	}

	@Override
	public Vector3i multiply(double other) {
		return new Vector3i(x * (int) other, y * (int) other, z * (int) other);
	}

	@Override
	public Vector3i reciprocal() {
		return new Vector3i(1 / x, 1 / y, 1 / z);
	}

	public Vector3i perpendicular() {
		if (z == 0) {
			return zCross();
		}

		return xCross();
	}

	public Vector3i xCross() {
		return new Vector3i(0, z, -y);
	}

	public Vector3i zCross() {
		return new Vector3i(-y, x, 0);
	}

	@Override
	public Vector3i max(Vector3 other) {
		return new Vector3i(Math.max(xi(), other.xi()), Math.max(yi(), other.yi()), Math.max(zi(), other.zi()));
	}

	@Override
	public Vector3i min(Vector3 other) {
		return new Vector3i(Math.min(xi(), other.xi()), Math.min(yi(), other.yi()), Math.min(zi(), other.zi()));
	}

	public Vector3i max(Vector3i other) {
		return max(other);
	}

	public Vector3i min(Vector3i other) {
		return min(other);
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
	public int zi() {
		return z;
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
	public double zd() {
		return z;
	}

	public Vector3d toDouble() {
		return new Vector3d(xd(), yd(), zd());
	}

	public Vector3i abs() {
		return new Vector3i(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public double distance(Vector3d other) {
		return toDouble().distance(other);
	}

	@Override
	public int hashCode() {
		long hash = x;
		hash = 31 * hash + y;
		hash = 31 * hash + z;
		return (int) hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3) {
			Vector3 v = ((Vector3) obj);
			return xi() == v.xi() && yi() == v.yi() && zi() == v.zi();
		}
		return this == obj;
	}

	public Matrix toMatrix() {
		return new Matrix(new double[][] { { x }, { y }, { z } });
	}

	@Override
	public String toString() {
		return "[Vector3d] " + x + ", " + y + ", " + z;
	}
}
