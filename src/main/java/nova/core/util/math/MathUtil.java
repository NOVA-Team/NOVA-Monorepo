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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Utility class for everything related to numbers.
 * @author Vic Nightfall
 */
public class MathUtil {

	private MathUtil() {

	}

	/**
	 * Returns the smaller number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return min
	 */
	public static int min(int a, int b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return min
	 */
	public static int min(int a, int b, int c) {
		return min(min(a, b), c);
	}

	/**
	 * Returns the smallest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static int min(int... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		int min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min) {
				min = numbers[i];
			}
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return max
	 */
	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return max
	 */
	public static int max(int a, int b, int c) {
		return max(max(a, b), c);
	}

	/**
	 * Returns the biggest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static int max(int... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		int max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max) {
				max = numbers[i];
			}
		}
		return max;
	}

	/**
	 * Returns the smaller number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return min
	 */
	public static long min(long a, long b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return min
	 */
	public static long min(long a, long b, long c) {
		return min(min(a, b), c);
	}

	/**
	 * Returns the smallest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static long min(long... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		long min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min) {
				min = numbers[i];
			}
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return max
	 */
	public static long max(long a, long b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return max
	 */
	public static long max(long a, long b, long c) {
		return max(max(a, b), c);
	}

	/**
	 * Returns the biggest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static long max(long... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		long max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max) {
				max = numbers[i];
			}
		}
		return max;
	}

	/**
	 * Returns the smaller number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return min
	 */
	public static double min(double a, double b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return min
	 */
	public static double min(double a, double b, double c) {
		return min(min(a, b), c);
	}

	/**
	 * Returns the smallest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static double min(double... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		double min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min) {
				min = numbers[i];
			}
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return max
	 */
	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return max
	 */
	public static double max(double a, double b, double c) {
		return max(max(a, b), c);
	}

	/**
	 * Returns the biggest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static double max(double... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		double max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max) {
				max = numbers[i];
			}
		}
		return max;
	}

	/**
	 * Returns the smaller number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return min
	 */
	public static float min(float a, float b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return min
	 */
	public static float min(float a, float b, float c) {
		return min(min(a, b), c);
	}

	/**
	 * Returns the smallest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static float min(float... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		float min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min) {
				min = numbers[i];
			}
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * @param a value.
	 * @param b value.
	 * @return max
	 */
	public static float max(float a, float b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a value.
	 * @param b value.
	 * @param c value.
	 * @return max
	 */
	public static float max(float a, float b, float c) {
		return max(max(a, b), c);
	}

	/**
	 * Returns the biggest number contained in the provided array.
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static float max(float... numbers) {
		if (numbers.length < 1) {
			throw new IllegalArgumentException();
		}
		float max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max) {
				max = numbers[i];
			}
		}
		return max;
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * @param a value.
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static int clamp(int a, int min, int max) {
		return min(max(a, min), max);
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * @param a value.
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static long clamp(long a, long min, long max) {
		return min(max(a, min), max);
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * @param a value
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static double clamp(double a, double min, double max) {
		return min(max(a, min), max);
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * @param a value.
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static float clamp(float a, float min, float max) {
		return min(max(a, min), max);
	}

	/**
	 * Linear interpolates isBetween point a and point b
	 * @param a value.
	 * @param b value.
	 * @param f A percentage value isBetween 0 to 1
	 * @return The interpolated value
	 */
	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	/**
	 * Linear interpolates isBetween point a and point b
	 * @param a value.
	 * @param b value.
	 * @param f A percentage value isBetween 0 to 1
	 * @return The interpolated value
	 */
	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	/**
	 * Linear interpolates isBetween point a and point b
	 * @param a value.
	 * @param b value.
	 * @param f A percentage value isBetween 0 to 1
	 * @return The interpolated value
	 */
	public static Vector3D lerp(Vector3D a, Vector3D b, float f) {
		return a.add((b.subtract(a)).scalarMultiply(f));
	}

	/**
	 * Clamps a value between -bounds to +bounds.
	 *
	 * @param value The value
	 * @param bounds The maximum distance from 0
	 * @return A value clamped between two bounds
	 */
	public static int absClamp(int value, int bounds) {
		return clamp(value, -bounds, bounds);
	}

	/**
	 * Clamps a value between -bounds to +bounds.
	 *
	 * @param value The value
	 * @param bounds The maximum distance from 0
	 * @return A value clamped between two bounds
	 */
	public static long absClamp(long value, long bounds) {
		return clamp(value, -bounds, bounds);
	}

	/**
	 * Clamps a value between -bounds to +bounds.
	 *
	 * @param value The value
	 * @param bounds The maximum distance from 0
	 * @return A value clamped between two bounds
	 */
	public static double absClamp(double value, double bounds) {
		return clamp(value, -bounds, bounds);
	}

	/**
	 * Clamps a value between -bounds to +bounds.
	 *
	 * @param value The value
	 * @param bounds The maximum distance from 0
	 * @return A value clamped between two bounds
	 */
	public static float absClamp(float value, float bounds) {
		return clamp(value, -bounds, bounds);
	}

	public static double truncate(double value, int truncation) {
		return Math.floor(value * truncation) / truncation;
	}

	public static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	public static long log(long x, long base) {
		return (long) (Math.log(x) / Math.log(base));
	}

	public static double log(double x, double base) {
		return Math.log(x) / Math.log(base);
	}

	public static float log(float x, float base) {
		return (float) (Math.log(x) / Math.log(base));
	}

	/**
	 * @param a value.
	 * @param x value.
	 * @param b value.
	 * @return True if x is greater than a and less than b
	 */
	public static boolean isBetween(double a, double x, double b) {
		return a <= x && x <= b;
	}

	/**
	 * Rounds a number to a specific number place places
	 *
	 * @param d - the number
	 * @param decimalPlaces The decimal places
	 * @return The rounded number
	 */
	public static double roundDecimals(double d, int decimalPlaces) {
		double pow = Math.pow(10, decimalPlaces);
		long i = Math.round(d * pow);
		return i / pow;
	}

	public static String toString(double d, boolean removeTrailingZeroes) {
		if (removeTrailingZeroes && d % 1 == 0)
			return Long.toString((long) d);
		return Double.toString(d);
	}
}
