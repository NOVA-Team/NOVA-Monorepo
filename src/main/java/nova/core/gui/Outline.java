package nova.core.gui;

import nova.core.util.transform.Rectangle;
import nova.core.util.transform.Vector2i;

/**
 * A {@link Rectangle} that resembles the outline of a {@link GuiElement}.
 * 
 * @author Vic Nightfall
 *
 */
public class Outline extends Rectangle<Vector2i> {

	private Outline(Vector2i min, Vector2i max) {
		super(min, max);
	}

	/**
	 * Creates a new Outline with the specified position and dimension.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Outline(int x, int y, int width, int height) {
		super(new Vector2i(x, y), new Vector2i(x + width, y + height));
	}

	public int getWidth() {
		return x2i() - x1i();
	}

	public int getHeight() {
		return y2i() - y1i();
	}

	public Outline setHeight(int height) {
		return new Outline(min, new Vector2i(y1i(), y1i() + height));
	}

	public Outline setWidth(int width) {
		return new Outline(new Vector2i(x1i(), x1i() + width), max);
	}
}
