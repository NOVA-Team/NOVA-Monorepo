package nova.core.gui;

import nova.core.util.transform.shape.Rectangle;
import nova.core.util.transform.vector.Vector2i;

/**
 * A {@link Rectangle} that resembles the outline of a {@link GuiComponent}.
 *
 * @author Vic Nightfall
 */
public class Outline extends Rectangle<Vector2i> {

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
		super(new Vector2i(x, y), new Vector2i(x + width, y + height));
	}

	public Outline(Vector2i position, Vector2i dimension) {
		super(position, position.add(dimension));
	}

	public int getWidth() {
		return x2i() - x1i();
	}

	public Outline setWidth(int width) {
		return new Outline(new Vector2i(x1i(), x1i() + width), max);
	}

	public int getHeight() {
		return y2i() - y1i();
	}

	public Outline setHeight(int height) {
		return new Outline(min, new Vector2i(y1i(), y1i() + height));
	}

	public Vector2i getDimension() {
		return new Vector2i(getWidth(), getHeight());
	}

	public Outline setDimension(Vector2i dimension) {
		return new Outline(min, dimension);
	}

	public Vector2i getPosition() {
		return getMin();
	}

	public Outline setPosition(Vector2i position) {
		return new Outline(position, position.add(getMax()));
	}
}
