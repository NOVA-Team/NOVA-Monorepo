package nova.core.util;

public enum Direction {
	DOWN(0, -1, 0),
	UP(0, 1, 0),
	NORTH(0, 0, -1),
	SOUTH(0, 0, 1),
	WEST(-1, 0, 0),
	EAST(1, 0, 0),
	UNKNOWN(0, 0, 0);

	public final int offsetX, offsetY, offsetZ;

	Direction(int ox, int oy, int oz) {
		offsetX = ox;
		offsetY = oy;
		offsetZ = oz;
	}

	public static final Direction[] DIRECTIONS = new Direction[] {
			DOWN, UP, NORTH, SOUTH, WEST, EAST
	};

	private static final Direction[] values = Direction.values();

	public static Direction fromOrdinal(int i) {
		return Direction.values[i % Direction.values.length];
	}

	public Direction opposite() {
		if (this == Direction.UNKNOWN) {
			return this;
		} else {
			return DIRECTIONS[this.ordinal() ^ 1];
		}
	}
}
