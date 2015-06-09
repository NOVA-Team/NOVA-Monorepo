package nova.core.util.shape;

import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Immutable, generic rectangle that defines the area between two coordinates in
 * 2D space.
 *
 * @author Vic Nightfall
 */
public class Rectangle extends Shape<Rectangle, Rectangle> {

	public final Vector2D min;
	public final Vector2D max;

	public Rectangle(Vector2D min, Vector2D max) {
		this.min = min;
		this.max = max;
	}

	public Vector2D getMin() {
		return min;
	}

	public Rectangle setMin(Vector2D min) {
		return new Rectangle(min, max);
	}

	public Vector2D getMax() {
		return max;
	}

	public Rectangle setMax(Vector2D max) {
		return new Rectangle(min, max);
	}

	public int x1i() {
		return (int) min.getX();
	}

	public int x2i() {
		return (int) max.getX();
	}

	public int y1i() {
		return (int) min.getY();
	}

	public int y2i() {
		return (int) max.getY();
	}

	public double x1d() {
		return min.getX();
	}

	public double x2d() {
		return max.getX();
	}

	public double y1d() {
		return min.getY();
	}

	public double y2d() {
		return max.getY();
	}

	public boolean contains(Vector2D point) {
		return contains(point.getX(), point.getY());
	}

	public boolean contains(double x, double y) {
		return x >= x1d() && x <= x2d() && y >= y1d() && y <= y2d();
	}

	@Override
	public Rectangle add(Rectangle other) {
		return new Rectangle(min.add(other.min), max.add(other.max));
	}

	@Override
	public Rectangle add(double other) {
		return new Rectangle(min.add(Vector2DUtil.ONE.scalarMultiply(other)), max.add(Vector2DUtil.ONE.scalarMultiply(other)));
	}

	@Override
	public Rectangle multiply(double other) {
		return new Rectangle(min.scalarMultiply(other), max.scalarMultiply(other));
	}

	@Override
	public Rectangle reciprocal() {
		return new Rectangle(Vector2DUtil.reciprocal(min), Vector2DUtil.reciprocal(max));
	}

	@Override
	public String toString() {
		return "[Rectangle] " + getMin() + ", " + getMax();
	}
}
