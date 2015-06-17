package nova.core.util.shape;

import nova.core.util.math.Vector2DUtil;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

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

	/**
	 * Get the min coords vector
	 *
	 * @return The min vector
	 */
	public Vector2D getMin() {
		return min;
	}

	/**
	 * Sets the min vector
	 *
	 * @param min The new min vector
	 * @return New Rectangle with the updated vector
	 */
	public Rectangle setMin(Vector2D min) {
		return new Rectangle(min, max);
	}

	/**
	 * Get the max vector
	 *
	 * @return The max vector
	 */
	public Vector2D getMax() {
		return max;
	}

	/**
	 * Sets the max vector
	 *
	 * @param max The new max vector
	 * @return New Rectangle with the updated vector
	 */
	public Rectangle setMax(Vector2D max) {
		return new Rectangle(min, max);
	}

	public int minXi() {
		return (int) FastMath.floor(min.getX());
	}

	public int maxXi() {
		return (int) FastMath.floor(max.getX());
	}

	public int minYi() {
		return (int) FastMath.floor(min.getY());
	}

	public int maxYi() {
		return (int) max.getY();
	}

	public double minXd() {
		return min.getX();
	}

	public double maxXd() {
		return max.getX();
	}

	public double minYd() {
		return min.getY();
	}

	public double maxYd() {
		return max.getY();
	}

	/**
	 * Checks if the point is contained in the Rectangle
	 *
	 * @param point The point to check
	 * @return If the point is in the rectangle or not
	 */
	public boolean contains(Vector2D point) {
		return contains(point.getX(), point.getY());
	}

	/**
	 * Checks if the point is contained in the Rectangle
	 *
	 * @param x x coord of the point
	 * @param y y coord of the point
	 * @return If the point is in the rectangle or not
	 */
	public boolean contains(double x, double y) {
		return x >= minXd() && x <= maxXd() && y >= minYd() && y <= maxYd();
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
