package nova.core.gui.layout;

/**
 * An anchor, specifies on which side of a rectangle something is bound to.
 * 
 * @author Vic Nightfall
 */
public enum Anchor {
	WEST(1), NORTH(2), EAST(1), SOUTH(2), CENTER(3);

	public final int axis;

	private Anchor(int axis) {
		this.axis = axis;
	}
}
