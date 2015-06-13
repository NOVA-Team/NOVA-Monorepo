package nova.core.gui.layout;

import nova.core.gui.Outline;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * An anchor, specifies on which side of a rectangle something is bound to.
 * 
 * @author Vic Nightfall
 */
public enum Anchor {
	WEST(1, new Vector2D(1, 0)), NORTH(2, new Vector2D(0, 1)), EAST(1, new Vector2D(-1, 0)), SOUTH(2, new Vector2D(0, -1)), CENTER(3, Vector2D.ZERO);

	public final int axis;
	public final Vector2D offset;

	Anchor(int axis, Vector2D offset) {
		this.axis = axis;
		this.offset = offset;
	}

	public Vector2D getStart(Outline outline) {
		switch (this) {
			default:
			case WEST:
			case NORTH:
				return new Vector2D(outline.minXi(), outline.minYi());
			case EAST:
				return new Vector2D(outline.maxXi(), outline.minYi());
			case SOUTH:
				return new Vector2D(outline.minXi(), outline.maxYi());
		}
	}

	public Vector2D offset(Vector2D position, int factor) {
		return position.add(offset.scalarMultiply(factor));
	}
}
