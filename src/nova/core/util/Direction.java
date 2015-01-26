package nova.core.core;

public enum Direction {
	DOWN,
	UP,
	NORTH,
	SOUTH,
	WEST,
	EAST,
	UNKNOWN;

	public static final Direction[] DIRECTIONS = new Direction[] {
			DOWN, UP, NORTH, SOUTH, WEST, EAST
	};

	private static final Direction[] values = Direction.values();

	public static Direction getOrientation(int i) {
		return Direction.values[i % Direction.values.length];
	}
}
