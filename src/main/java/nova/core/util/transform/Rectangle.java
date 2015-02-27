package nova.core.util.transform;

/**
 * Immutable, generic rectangle that defines the area between two coordinates in
 * 2D space.
 *
 * @param <T> -Describe me-
 * @author Vic Nightfall
 */
public class Rectangle<T extends Vector2<T>> extends Shape<Rectangle<T>, Rectangle<T>> {

	public final T min;
	public final T max;

	public Rectangle(T min, T max) {
		this.min = min;
		this.max = max;
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

	public Rectangle<T> setMin(T min) {
		return new Rectangle<T>(min, max);
	}

	public Rectangle<T> setMax(T max) {
		return new Rectangle<T>(min, max);
	}

	public int x1i() {
		return min.xi();
	}

	public int x2i() {
		return max.xi();
	}

	public int y1i() {
		return min.yi();
	}

	public int y2i() {
		return max.yi();
	}

	public double x1d() {
		return min.xd();
	}

	public double x2d() {
		return max.xd();
	}

	public double y1d() {
		return min.yd();
	}

	public double y2d() {
		return max.yd();
	}

	public boolean contains(T point) {
		return contains(point.xd(), point.yd());
	}

	public boolean contains(double x, double y) {
		return x >= x1d() && x <= x2d() && y >= y1d() && y <= y2d();
	}

	@Override
	public Rectangle<T> add(Rectangle<T> other) {
		return new Rectangle<T>(min.add(other.min), max.add(other.max));
	}

	@Override
	public Rectangle<T> add(double other) {
		return new Rectangle<T>(min.add(other), max.add(other));
	}

	@Override
	public Rectangle<T> multiply(Rectangle<T> other) {
		return new Rectangle<T>(min.multiply(other.min), max.multiply(other.max));
	}

	@Override
	public Rectangle<T> multiply(double other) {
		return new Rectangle<T>(min.multiply(other), max.multiply(other));
	}

	@Override
	public Rectangle<T> reciprocal() {
		return new Rectangle<T>(min.reciprocal(), max.reciprocal());
	}

	@Override
	public String toString() {
		return "[Rectangle] " + getMin() + ", " + getMax();
	}
}
