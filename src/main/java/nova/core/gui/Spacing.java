package nova.core.gui;

import nova.core.util.shape.Rectangle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * <p>
 * A {@link Rectangle} that resembles spacing for a {@link GuiComponent}.
 * </p>
 * <p>
 * Similar to the CSS padding or margin property.
 * </p>
 * @author Vic Nightfall
 */
public class Spacing extends Rectangle {

	public static final Spacing empty = new Spacing(0);

	/**
	 * <p>
	 * Defines a new spacing:
	 * </p>
	 *
	 * <p>
	 * <span style='color: blue'>top</span> - <span style='color:
	 * green'>right</span> - <span style='color: orange'>bottom</span> - <span
	 * style='color: red'>left</span>
	 * </p>
	 *
	 * <div style='width: 100px; height: 50px; display: block; border: 5px
	 * solid; border-color: blue green orange red;'></div>
	 * @param top distance on top
	 * @param right distance on the right
	 * @param bottom distance on bottom
	 * @param left distance on the left
	 */
	public Spacing(int top, int right, int bottom, int left) {
		super(new Vector2D(left, top), new Vector2D(right, bottom));
	}

	/**
	 * <p>
	 * Defines a new spacing:
	 * </p>
	 *
	 * <p>
	 * <span style='color: blue'>top</span> - <span style='color: red'>x</span>
	 * - <span style='color: orange'>bottom</span>
	 * </p>
	 *
	 * <div style='width: 100px; height: 50px; display: block; border: 5px
	 * solid; border-color: blue red orange;'></div>
	 * @param top distance on top
	 * @param x distance on the left &amp; right
	 * @param bottom distance on bottom
	 */
	public Spacing(int top, int x, int bottom) {
		this(top, x, bottom, x);
	}

	/**
	 * <p>
	 * Defines a new spacing:
	 * </p>
	 *
	 * <p>
	 * <span style='color: red'>x</span> - <span style='color: blue'>y</span>
	 * </p>
	 *
	 * <div style='width: 100px; height: 50px; display: block; border: 5px
	 * solid; border-color: blue red;'></div>
	 * @param x distance on the left &amp; right
	 * @param y distance on top &amp; bottom
	 */
	public Spacing(int x, int y) {
		this(x, y, x, y);
	}

	/**
	 * <p>
	 * Defines a new spacing:
	 * </p>
	 *
	 * <p style='color: red'>
	 * spacing
	 * </p>
	 *
	 * <div style='width: 100px; height: 50px; display: block; border: 5px
	 * solid; border-color: red;'></div>
	 * @param spacing Spacing on all sides
	 */
	public Spacing(int spacing) {
		this(spacing, spacing, spacing, spacing);
	}

	public Spacing combine(Spacing second) {
		return new Spacing(
			Math.max(second.top(), top()),
			Math.max(second.right(), right()),
			Math.max(second.bottom(), bottom()),
			Math.max(second.left(), left()));
	}

	public int left() {
		return (int) min.getX();
	}

	public int right() {
		return (int) max.getX();
	}

	public int top() {
		return (int) min.getY();
	}

	public int bottom() {
		return (int) max.getY();
	}
}
