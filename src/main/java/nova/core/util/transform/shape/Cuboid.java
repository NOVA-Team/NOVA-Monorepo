package nova.core.util.transform.shape;

import nova.core.util.Direction;
import nova.core.util.math.VectorUtil;
import nova.core.util.transform.vector.Transformer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

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
	public static final Cuboid zero = new Cuboid(Vector3D.ZERO, Vector3D.ZERO);
	public static final Cuboid one = new Cuboid(Vector3D.ZERO, VectorUtil.ONE);
	public final Vector3D min;
	public final Vector3D max;

	public Cuboid(Vector3D min, Vector3D max) {
		this.min = min;
		this.max = max;
	}

	public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
	}

	@Override
	public Cuboid add(Cuboid other) {
		return new Cuboid(min.add(other.min), max.add(other.max));
	}

	public Cuboid add(Vector3D other) {
		return new Cuboid(min.add(other), max.add(other));
	}

	@Override
	public Cuboid add(double other) {
		return new Cuboid(min.add(VectorUtil.ONE.scalarMultiply(other)), max.add(VectorUtil.ONE.scalarMultiply(other)));
	}

	public Cuboid $plus(Vector3D other) {
		return add(other);
	}

	public Cuboid subtract(Vector3D other) {
		return new Cuboid(min.subtract(other), max.subtract(other));
	}

	public Cuboid $minus(Vector3D other) {
		return subtract(other);
	}

	@Override
	public Cuboid multiply(double other) {
		return new Cuboid(min.scalarMultiply(other), max.scalarMultiply(other));
	}

	@Override
	public Cuboid reciprocal() {
		return new Cuboid(VectorUtil.reciprocal(min), VectorUtil.reciprocal(max));
	}

	/**
	 * Expands the cuboid by a certain vector.
	 * @param other Given vector
	 * @return New cuboid
	 */
	public Cuboid expand(Vector3D other) {
		return new Cuboid(min.subtract(other), max.add(other));
	}

	/**
	 * Expands the cuboid by a certain amount.
	 * @param other The amount
	 * @return New cuboid
	 */
	public Cuboid expand(double other) {
		return new Cuboid(min.subtract(VectorUtil.ONE.scalarMultiply(other)), max.add(VectorUtil.ONE.scalarMultiply(other)));
	}

	/**
	 * Returns if this cuboid is a cube.
	 * @return If this cuboid is a cube.
	 */
	public boolean isCube() {
		return size().getX() == size().getY() && size().getY() == size().getZ();
	}

	public Vector3D size() {
		return max.subtract(min);
	}

	public Vector3D center() {
		return VectorUtil.midpoint(max, min);
	}

	public double volume() {
		return size().getX() * size().getY() * size().getZ();
	}

	public double surfaceArea() {
		return (2 * size().getX() * size().getZ()) + (2 * size().getX() * size().getY()) + (2 * size().getZ() * size().getY());
	}

	/**
	 * Checks if another cuboid is within this cuboid
	 * @param other Cuboid to check
	 * @return Result of the check
	 */
	public boolean intersects(Cuboid other) {
		return (other.max.getX() >= min.getX() && other.min.getX() < max.getX()) ?
			((other.max.getY() >= min.getY() && other.min.getY() < max.getY()) ? other.max.getZ() >= min.getZ() && other.min.getZ() < max.getZ() : false) : false;
	}

	/**
	 * Checks if a vector is within this cuboid.
	 * @param other Vector to check
	 * @return Result of the check
	 */
	public boolean intersects(Vector3D other) {
		return other.getX() >= this.min.getX() && other.getX() < this.max.getX() ?
			(other.getY() >= this.min.getY() && other.getY() < this.max.getY() ? other.getZ() >= this.min.getZ() && other.getZ() < this.max.getZ() : false) : false;
	}

	public Cuboid transform(Transformer transform) {
		Vector3D transMin = transform.apply(min);
		Vector3D transMax = transform.apply(max);
		return new Cuboid(VectorUtil.min(transMin, transMax), VectorUtil.min(transMax, transMin));
	}

	public void forEach(Consumer<Vector3D> consumer) {
		forEach(vector3d -> consumer.accept(vector3d), 1);
	}

	public void forEach(Consumer<Vector3D> consumer, double step) {
		for (double x = min.getX(); x < max.getX(); x += step)
			for (double y = min.getY(); y < max.getY(); y += step)
				for (double z = min.getZ(); z < max.getZ(); z += step)
					consumer.accept(new Vector3D(x, y, z));
	}

	public Direction sideOf(Vector3D position) {
		return Direction.fromVector(position.subtract(center()).normalize());
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Cuboid[" + new BigDecimal(min.getX(), cont) + ", " + new BigDecimal(min.getY(), cont) + ", " + new BigDecimal(min.getZ(), cont) + "] -> [" + new BigDecimal(max.getX(), cont) + ", " + new BigDecimal(max.getY(), cont) + ", " + new BigDecimal(max.getZ(), cont) + "]";
	}
}
