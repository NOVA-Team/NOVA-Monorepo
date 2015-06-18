package nova.core.util.math;

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
	 * @param a
	 * @param b
	 * @return min
	 */
	public static int min(int a, int b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param b
	 * @return max
	 */
	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param b
	 * @return min
	 */
	public static double min(double a, double b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param b
	 * @return max
	 */
	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param b
	 * @return min
	 */
	public static float min(float a, float b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param b
	 * @return max
	 */
	public static float max(float a, float b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * @param a
	 * @param b
	 * @param c
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
	 * @param a
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static int clamp(int a, int min, int max) {
		return min(max(a, min), max);
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * @param a
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static double clamp(double a, double min, double max) {
		return min(max(a, min), max);
	}

	/**
	 * Linear interpolates isBetween point a and point b
	 * @param f A percentage value isBetween 0 to 1
	 * @return The interpolated value
	 */
	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	public static Vector3D lerp(Vector3D a, Vector3D b, float f) {
		return a.add((b.subtract(a)).scalarMultiply(f));
	}

	/**
	 * Clamps a value isBetween -bounds to +bounds
	 * @return A value capped isBetween two bounds.
	 */
	public static double absClamp(double value, double bounds) {
		return min(max(value, -bounds), bounds);
	}

	public static float absClamp(int value, int bounds) {
		return min(max(value, -bounds), bounds);
	}

	public static double truncate(double value, int truncation) {
		return Math.floor(value * truncation) / truncation;
	}

	public static int log(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	public static double log(double x, double base) {
		return Math.log(x) / Math.log(base);
	}

	/**
	 * @return True if x is greater than a and less than b
	 */
	public static boolean isBetween(double a, double x, double b) {
		return a <= x && x <= b;
	}

}
