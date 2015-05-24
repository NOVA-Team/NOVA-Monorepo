package nova.core.gui.layout;

import nova.core.gui.Outline;
import nova.core.util.transform.vector.Vector2i;

/**
 * An anchor, specifies on which side of a rectangle something is bound to.
 * 
 * @author Vic Nightfall
 */
public enum Anchor {
	WEST(1, new Vector2i(1, 0)), NORTH(2, new Vector2i(0, 1)), EAST(1, new Vector2i(-1, 0)), SOUTH(2, new Vector2i(0, -1)), CENTER(3, Vector2i.zero);

	public final int axis;
	public final Vector2i offset;

	private Anchor(int axis, Vector2i offset) {
		this.axis = axis;
		this.offset = offset;
	}

	public Vector2i getStart(Outline outline) {
		switch (this) {
			default:
			case WEST:
			case NORTH:
				return new Vector2i(outline.x1i(), outline.y1i());
			case EAST:
				return new Vector2i(outline.x2i(), outline.y1i());
			case SOUTH:
				return new Vector2i(outline.x1i(), outline.y2i());
		}
	}

	public Vector2i offset(Vector2i position, int factor) {
		return position.add(offset.multiply(factor));
	}
}
