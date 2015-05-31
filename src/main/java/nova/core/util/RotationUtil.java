package nova.core.util;

/**
 * A rotation utility class.
 * @author Calclavia
 */
public class RotationUtil {

	public static int[][] relativeMatrix = new int[][] {
		{ 3, 2, 1, 0, 5, 4 },
		{ 4, 5, 0, 1, 2, 3 },
		{ 0, 1, 3, 2, 5, 4 },
		{ 0, 1, 2, 3, 4, 5 },
		{ 0, 1, 4, 5, 3, 2 },
		{ 0, 1, 5, 4, 2, 3 }
	};

	public static int[] sideRotMap = new int[] {
		3, 4, 2, 5,
		3, 5, 2, 4,
		1, 5, 0, 4,
		1, 4, 0, 5,
		1, 2, 0, 3,
		1, 3, 0, 2 };

	public static int[] rotSideMap = new int[] {
		-1, -1, 2, 0, 1, 3,
		-1, -1, 2, 0, 3, 1,
		2, 0, -1, -1, 3, 1,
		2, 0, -1, -1, 1, 3,
		2, 0, 1, 3, -1, -1,
		2, 0, 3, 1, -1, -1 };

	/**
	 * Rotate pi/2 * this offset for [side] about y axis before rotating to the side for the rotation indicies to line up
	 */
	public static int[] sideRotOffsets = new int[] { 0, 2, 2, 0, 1, 3 };

	/**
	 * Rotates a relative side into a Direction global size.
	 * @param s - The current face we are on (0-6)
	 * @param r - The rotation to be applied (0-3)
	 * @return The Direction ordinal from 0-5.
	 */
	public static int rotateSide(int s, int r) {
		return sideRotMap[s << 2 | r];
	}

	/**
	 * Reverse of rotateSide
	 */
	public static int rotationTo(int s1, int s2) {
		if ((s1 & 6) == (s2 & 6)) {
			throw new IllegalArgumentException("Faces " + s1 + " and " + s2 + " are opposites");
		}
		return rotSideMap[s1 * 6 + s2];
	}

	/**
	 * Finds the direction relative to a base direction.
	 * @param front The direction in which this block is facing/front. Use a number between 0 and
	 * 5. Default is 3.
	 * @param side The side you are trying to find. A number between 0 and 5.
	 * @return The side relative to the facing direction.
	 */
	public static Direction getRelativeSide(Direction front, Direction side) {
		if (front != Direction.UNKNOWN && side != Direction.UNKNOWN) {
			return Direction.fromOrdinal(relativeMatrix[front.ordinal()][side.ordinal()]);
		}
		return Direction.UNKNOWN;
	}
}
