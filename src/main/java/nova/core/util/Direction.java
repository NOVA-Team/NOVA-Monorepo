package nova.core.util;

import nova.core.util.transform.Vector3i;

/**
 * defines basic directions in world
 */
public enum Direction {
	DOWN(0, -1, 0),
	UP(0, 1, 0),
	NORTH(0, 0, -1),
	SOUTH(0, 0, 1),
	WEST(-1, 0, 0),
	EAST(1, 0, 0),
	UNKNOWN(0, 0, 0);

	public static final Direction[] DIRECTIONS = new Direction[] {
		DOWN, UP, NORTH, SOUTH, WEST, EAST
	};
	private static final Direction[] values = Direction.values();
	public final int x, y, z;
	private final Vector3i vector;

	Direction(int ox, int oy, int oz) {
		x = ox;
		y = oy;
		z = oz;
		vector = new Vector3i(x, y, z);
	}

	//FIXME: This method may return null!!
	/**
	 * Turns direction number into Direction
	 * @param is Direction id
	 * @return Resulting Direction
	 */
	public static Direction fromOrdinal(int is) {
		return Direction.values[is % Direction.values.length];
	}

	/**
	 * @return Direction opposite to this
	 */
	public Direction opposite() {
		if (this == Direction.UNKNOWN) {
			return this;
		} else {
			return DIRECTIONS[this.ordinal() ^ 1];
		}
	}

	/**
	 * @return This Direction represented as {@link Vector3i}
	 */
	public Vector3i toVector() {
		return vector;
	}
}
