package nova.core.util;

import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector3d;
import nova.core.util.transform.Vector3i;

/**
 * Defines basic directions in world.
 */
public enum Direction {
	DOWN(0, -1, 0, Quaternion.fromAxis(Vector3d.xAxis, -90)),
	UP(0, 1, 0, Quaternion.fromAxis(Vector3d.xAxis, 90)),
	NORTH(0, 0, -1, Quaternion.fromAxis(Vector3d.yAxis, 90)),
	SOUTH(0, 0, 1, Quaternion.fromAxis(Vector3d.yAxis, -90)),
	WEST(-1, 0, 0, Quaternion.fromAxis(Vector3d.yAxis, -180)),
	EAST(1, 0, 0, Quaternion.fromAxis(Vector3d.yAxis, 0)),
	UNKNOWN(0, 0, 0, Quaternion.identity);

	public static final Direction[] DIRECTIONS = new Direction[] {
		DOWN, UP, NORTH, SOUTH, WEST, EAST
	};
	private static final Direction[] values = Direction.values();
	public final int x, y, z;
	public final Quaternion rotation;
	private final Vector3i vector;

	Direction(int x, int y, int z, Quaternion rotation) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.vector = new Vector3i(x, y, z);
		this.rotation = rotation;
	}

	/**
	 * Turns direction number into Direction.
	 *
	 * @param directionID Direction ID / number.
	 * @return Resulting Direction.
	 * @throws IllegalArgumentException if the direction ID is invalid (greater than {@code 6} or less than {@code 0})
	 */
	public static Direction fromOrdinal(int directionID) {
		if (directionID < 0 || directionID >= Direction.values.length) {
			throw new IllegalArgumentException("Direction ID is invalid! The direction ID " + directionID + " is must be between " + (Direction.values.length - 1) + " and 0 inclusive");
		}
		return Direction.values[directionID];
	}

	/**
	 * @return Direction opposite to this.
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
