package nova.core.util.transform.shape;

import nova.core.util.Direction;
import nova.core.util.transform.vector.Transformer;
import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Consumer;

/**
 * A cuboid is a shape that represents a cube.
 * @author Calclavia
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Cuboid extends Shape<Cuboid, Cuboid> {
	public static final Cuboid zero = new Cuboid(Vector3d.zero, Vector3d.zero);
	public static final Cuboid one = new Cuboid(Vector3d.zero, Vector3d.one);
	public final Vector3d min;
	public final Vector3d max;

	public Cuboid(Vector3d min, Vector3d max) {
		this.min = min;
		this.max = max;
	}

	public Cuboid(Vector3i min, Vector3i max) {
		this(min.toDouble(), max.toDouble());
	}

	public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this(new Vector3d(minX, minY, minZ), new Vector3d(maxX, maxY, maxZ));
	}

	@Override
	public Cuboid add(Cuboid other) {
		return new Cuboid(min.add(other.min), max.add(other.max));
	}

	public Cuboid add(Vector3<?> other) {
		return new Cuboid(min.add(other), max.add(other));
	}

	@Override
	public Cuboid add(double other) {
		return new Cuboid(min.add(other), max.add(other));
	}

	public Cuboid $plus(Vector3<?> other) {
		return add(other);
	}

	public Cuboid subtract(Vector3 other) {
		return new Cuboid(min.subtract(other), max.subtract(other));
	}

	public Cuboid $minus(Vector3<?> other) {
		return subtract(other);
	}

	@Override
	public Cuboid multiply(Cuboid other) {
		return new Cuboid(min.multiply(other.min), max.multiply(other.max));
	}

	public Cuboid multiply(Vector3 other) {
		return new Cuboid(min.multiply(other), max.multiply(other));
	}

	@Override
	public Cuboid multiply(double other) {
		return new Cuboid(min.multiply(other), max.multiply(other));
	}

	@Override
	public Cuboid reciprocal() {
		return new Cuboid(min.reciprocal(), max.reciprocal());
	}

	/**
	 * Expands the cuboid by a certain vector.
	 * @param other Given vector
	 * @return New cuboid
	 */
	public Cuboid expand(Vector3 other) {
		return new Cuboid(min.subtract(other), max.add(other));
	}

	/**
	 * Expands the cuboid by a certain amount.
	 * @param other The amount
	 * @return New cuboid
	 */
	public Cuboid expand(double other) {
		return new Cuboid(min.subtract(other), max.add(other));
	}

	/**
	 * Returns if this cuboid is a cube.
	 * @return If this cuboid is a cube.
	 */
	public boolean isCube() {
		return size().x == size().y && size().y == size().z;
	}

	public Vector3d size() {
		return max.subtract(min);
	}

	public Vector3d center() {
		return max.midpoint(min);
	}

	public double volume() {
		return size().x * size().y * size().z;
	}

	public double surfaceArea() {
		return (2 * size().x * size().z) + (2 * size().x * size().y) + (2 * size().z * size().y);
	}

	/**
	 * Checks if another cuboid is within this cuboid
	 * @param other Cuboid to check
	 * @return Result of the check
	 */
	public boolean intersects(Cuboid other) {
		return (other.max.x >= min.x && other.min.x < max.x) ? ((other.max.y >= min.y && other.min.y < max.y) ? other.max.z >= min.z && other.min.z < max.z : false) : false;
	}

	/**
	 * Checks if a vector is within this cuboid.
	 * @param other Vector to check
	 * @return Result of the check
	 */
	public boolean intersects(Vector3<?> other) {
		return other.xd() >= this.min.x && other.xd() < this.max.x ? (other.yd() >= this.min.y && other.yd() < this.max.y ? other.zd() >= this.min.z && other.zd() < this.max.z : false) : false;
	}

	public Cuboid transform(Transformer transform) {
		Vector3d transMin = transform.transform(min);
		Vector3d transMax = transform.transform(max);
		return new Cuboid(transMin.min(transMax), transMax.max(transMin));
	}

	public void forEach(Consumer<Vector3i> consumer) {
		forEach(vector3d -> consumer.accept(vector3d.toInt()), 1);
	}

	public void forEach(Consumer<Vector3d> consumer, double step) {
		for (double x = min.x; x < max.x; x += step)
			for (double y = min.y; y < max.y; y += step)
				for (double z = min.z; z < max.z; z += step)
					consumer.accept(new Vector3d(x, y, z));
	}

	public Direction sideOf(Vector3d position) {
		return Direction.fromVector(position.subtract(center()).normalize());
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Cuboid[" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont) + "] -> [" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + "]";
	}
}
