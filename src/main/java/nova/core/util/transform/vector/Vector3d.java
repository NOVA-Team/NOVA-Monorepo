package nova.core.util.transform.vector;

import com.google.common.math.DoubleMath;
import nova.core.retention.Storable;
import nova.core.retention.Stored;
import nova.core.util.math.MathUtil;
import nova.core.util.transform.matrix.Matrix;

/**
 * A double implementation of Vector3. Vector3 is an immutable quantity that
 * holds an x, y and z value.
 * @author Calclavia
 */
@SuppressWarnings("rawtypes")
public class Vector3d extends Vector3<Vector3d> implements Storable {
	public static final Vector3d zero = new Vector3d(0, 0, 0);
	public static final Vector3d center = new Vector3d(0.5, 0.5, 0.5);
	public static final Vector3d one = new Vector3d(1, 1, 1);
	public static final Vector3d xAxis = new Vector3d(1, 0, 0);
	public static final Vector3d yAxis = new Vector3d(0, 1, 0);
	public static final Vector3d zAxis = new Vector3d(0, 0, 1);

	@Stored
	public final double x, y, z;

	public Vector3d() {
		this(0, 0, 0);
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3d add(Vector3 other) {
		return new Vector3d(x + other.xd(), y + other.yd(), z + other.zd());
	}

	@Override
	public Vector3d add(double other) {
		return new Vector3d(x + other, y + other, z + other);
	}

	@Override
	public Vector3d multiply(Vector3 other) {
		return new Vector3d(y * other.zd() - z * other.yd(), z * other.xd() - x * other.zd(), x * other.yd() - y * other.xd());
	}

	public Vector3d scale(Vector3 other) {
		return new Vector3d(x * other.xd(), y * other.yd(), z * other.zd());
	}

	@Override
	public Vector3d multiply(double other) {
		return new Vector3d(x * other, y * other, z * other);
	}

	@Override
	public Vector3d reciprocal() {
		return new Vector3d(1 / x, 1 / y, 1 / z);
	}

	public Vector3d perpendicular() {
		if (z == 0) {
			return zCross();
		}

		return xCross();
	}

	public Vector3d xCross() {
		return new Vector3d(0, z, -y);
	}

	public Vector3d zCross() {
		return new Vector3d(-y, x, 0);
	}

	public Vector3d max(Vector3d other) {
		return new Vector3d(Math.max(xd(), other.xd()), Math.max(yd(), other.yd()), Math.max(zd(), other.zd()));
	}

	@Override
	public Vector3d max(Vector3 other) {
		return new Vector3d(Math.max(xd(), other.xd()), Math.max(yd(), other.yd()), Math.max(zd(), other.zd()));
	}

	public Vector3d min(Vector3d other) {
		return new Vector3d(Math.min(xd(), other.xd()), Math.min(yd(), other.yd()), Math.min(zd(), other.zd()));
	}

	@Override
	public Vector3d min(Vector3 other) {
		return new Vector3d(Math.min(xd(), other.xd()), Math.min(yd(), other.yd()), Math.min(zd(), other.zd()));
	}

	@Override
	public int hashCode() {
		long x = Double.doubleToLongBits(this.x);
		long y = Double.doubleToLongBits(this.y);
		long z = Double.doubleToLongBits(this.z);
		long hash = (x ^ (x >>> 32));
		hash = 31 * hash + y ^ (y >>> 32);
		hash = 31 * hash + z ^ (z >>> 32);
		return (int) hash;
	}

	@Override
	public int xi() {
		return (int) x;
	}

	@Override
	public int yi() {
		return (int) y;
	}

	@Override
	public int zi() {
		return (int) z;
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

	public Vector3i round() {
		return new Vector3i((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
	}

	public Vector3i ceil() {
		return new Vector3i((int) Math.ceil(x), (int) Math.ceil(y), (int) Math.ceil(z));
	}

	public Vector3i floor() {
		return new Vector3i((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
	}

	public Vector3i toInt() {
		return new Vector3i(xi(), yi(), zi());
	}

	public Vector3d abs() {
		return new Vector3d(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public double distance(Vector3i other) {
		return distance(other.toDouble());
	}

	public Vector3d YZintercept(Vector3d end, double px) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dx == 0) {
			return null;
		}

		double d = (px - x) / dx;
		if (MathUtil.isBetween(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtil.isBetween(0, d, 1)) {
			return null;
		}

		return new Vector3d(px, y + d * dy, z + d * dz);
	}

	public Vector3d XZintercept(Vector3d end, double py) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dy == 0) {
			return null;
		}

		double d = (py - y) / dy;
		if (MathUtil.isBetween(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtil.isBetween(0, d, 1)) {
			return null;
		}

		return new Vector3d(x + d * dx, py, z + d * dz);
	}

	public Vector3d XYintercept(Vector3d end, double pz) {
		double dx = end.x - x;
		double dy = end.y - y;
		double dz = end.z - z;

		if (dz == 0) {
			return null;
		}

		double d = (pz - z) / dz;
		if (MathUtil.isBetween(-1E-5, d, 1E-5)) {
			return this;
		}

		if (!MathUtil.isBetween(0, d, 1)) {
			return null;
		}

		return new Vector3d(x + d * dx, y + d * dy, pz);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3) {
			Vector3 other = ((Vector3) obj);
			return DoubleMath.fuzzyEquals(this.xd(), other.xd(), 0.000001) && DoubleMath.fuzzyEquals(this.yd(), other.yd(), 0.000001) && DoubleMath.fuzzyEquals(this.zd(), other.zd(), 0.000001);
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
