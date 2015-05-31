package nova.core.util;

import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;

import java.util.stream.IntStream;

/**
 * Defines basic directions in world.
 */
public enum Direction {
	DOWN(0, -1, 0, Quaternion.fromAxis(Vector3d.xAxis, -Math.PI / 2)),
	UP(0, 1, 0, Quaternion.fromAxis(Vector3d.xAxis, Math.PI / 2)),
	NORTH(0, 0, -1, Quaternion.fromAxis(Vector3d.yAxis, -Math.PI)),
	SOUTH(0, 0, 1, Quaternion.fromAxis(Vector3d.yAxis, 0)),
	WEST(-1, 0, 0, Quaternion.fromAxis(Vector3d.yAxis, Math.PI / 2)),
	EAST(1, 0, 0, Quaternion.fromAxis(Vector3d.yAxis, -Math.PI / 2)),
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
	 * @param unitVector The unit vector representing the direction.
	 * @return The direction based on a unit vector
	 */
	public static Direction fromVector(Vector3<?> unitVector) {
		return fromOrdinal(
			IntStream.range(0, 6)
				.boxed()
				.sorted((o1, o2) -> Double.compare(fromOrdinal(o2).toVector().dot(unitVector), fromOrdinal(o1).toVector().dot(unitVector)))
				.findFirst()
				.get()
		);
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
