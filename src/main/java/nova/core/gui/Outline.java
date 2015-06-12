package nova.core.gui;

import nova.core.util.shape.Rectangle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A {@link Rectangle} that resembles the outline of a {@link GuiComponent}.
 *
 * @author Vic Nightfall
 */
public class Outline extends Rectangle {

	public static Outline empty = new Outline(0, 0, 0, 0);

	/**
	 * Creates a new Outline with the specified position and dimension.
	 *
	 * @param x X position
	 * @param y Y position
	 * @param width Width
	 * @param height Height
	 */
	public Outline(int x, int y, int width, int height) {
		super(new Vector2D(x, y), new Vector2D(x + width, y + height));
	}

	public Outline(Vector2D position, Vector2D dimension) {
		super(position, position.add(dimension));
	}

	public int getWidth() {
		return maxXi() - minXi();
	}

	public Outline setWidth(int width) {
		return new Outline(new Vector2D(minXi(), minXi() + width), max);
	}

	public int getHeight() {
		return maxYi() - minYi();
	}

	public Outline setHeight(int height) {
		return new Outline(min, new Vector2D(minYi(), minYi() + height));
	}

	public Vector2D getDimension() {
		return new Vector2D(getWidth(), getHeight());
	}

	public Outline setDimension(Vector2D dimension) {
		return new Outline(min, dimension);
	}

	public Vector2D getPosition() {
		return getMin();
	}

	public Outline setPosition(Vector2D position) {
		return new Outline(position, position.add(getMax()));
	}
}
