/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */package nova.core.util.math;

import nova.core.util.Direction;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.util.FastMath;

/**
 * A rotation utility class.
 * @author Calclavia
 */
public class RotationUtil {

	private RotationUtil() {

	}

	/**
	 * Rotation order that is used by defualt in Nova.
	 */
	public static final RotationOrder DEFAULT_ORDER = RotationOrder.YXZ;

	private static int[][] relativeMatrix = new int[][] {
		{ 3, 2, 1, 0, 5, 4 },
		{ 4, 5, 0, 1, 2, 3 },
		{ 0, 1, 3, 2, 5, 4 },
		{ 0, 1, 2, 3, 4, 5 },
		{ 0, 1, 4, 5, 3, 2 },
		{ 0, 1, 5, 4, 2, 3 }
	};

	private static int[] sideRotMap = new int[] {
		3, 4, 2, 5,
		3, 5, 2, 4,
		1, 5, 0, 4,
		1, 4, 0, 5,
		1, 2, 0, 3,
		1, 3, 0, 2 };

	private static int[] rotSideMap = new int[] {
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
	 * Rotates a Direction global size into a relative side.
	 * @param s1 Side 1
	 * @param s2 Side 2
	 * @return The Direction ordinal from 0-5.
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

	/**
	 * Wrapper function that simply calls {@code slerp(a, b, t, true)}.
	 * <p>
	 * See {@link #slerp(Rotation, Rotation, double, boolean)} for details.
	 *
	 * @param a the first Rotation
	 * @param b the second Rotation
	 * @param t the temporal interpolation parameter
	 * @return The slerp interpolation of Rotations {@code a} and {@code b}, at time {@code t}.
	 */
	public static Rotation slerp(Rotation a, Rotation b, double t) {
		return slerp(a, b, t, true);
	}

	/**
	 * Returns the slerp interpolation of Rotations {@code a} and {@code b}, at
	 * time {@code t}.
	 * <p>
	 * {@code t} should range in {@code [0,1]}. Result is a when {@code t=0 } and
	 * {@code b} when {@code t=1}.
	 * <p>
	 * When {@code allowFlip} is true (default) the slerp interpolation will
	 * always use the "shortest path" between the Rotations' orientations, by
	 * "flipping" the source Rotation if needed.
	 * @param a the first Rotation
	 * @param b the second Rotation
	 * @param t the temporal interpolation parameter
	 * @param allowFlip tells whether or not the interpolation allows axis flip
	 * @return The slerp interpolation of Rotations {@code a} and {@code b}, at time {@code t}.
	 */
	public static Rotation slerp(Rotation a, Rotation b, double t, boolean allowFlip) {
		// TODO: this method should not normalize the Rotation
		double cosAngle = dotProduct(a, b);

		double c1, c2;
		// Linear interpolation for close orientations
		if ((1.0 - FastMath.abs(cosAngle)) < 0.01) {
			c1 = 1.0f - t;
			c2 = t;
		} else {
			// Spherical interpolation
			double angle = FastMath.acos(FastMath.abs(cosAngle));
			double sinAngle = FastMath.sin(angle);
			c1 = FastMath.sin(angle * (1.0f - t)) / sinAngle;
			c2 = FastMath.sin(angle * t) / sinAngle;
		}

		// Use the shortest path
		if (allowFlip && (cosAngle < 0.0)) {
			c1 = -c1;
		}

		return new Rotation(c1 * a.getQ1() + c2 * b.getQ1(), c1 * a.getQ2() + c2 * b.getQ2(), c1 * a.getQ3()
			+ c2 * b.getQ3(), c1 * a.getQ0() + c2 * b.getQ0(), false);
	}

	/**
	 * Returns the "dot" product of this Quaternion and {@code b}:
	 * <p>
	 * {@code this.x * b.x + this.y * b.y + this.z * b.z + this.w * b.w}
	 * @param a This
	 * @param b the Quaternion
	 * @return The dot product
	 */
	public static double dotProduct(Rotation a, Rotation b) {
		return a.getQ0() * b.getQ0() + a.getQ1() * b.getQ1() + a.getQ2() * b.getQ2() + a.getQ3() * b.getQ3();
	}
}
