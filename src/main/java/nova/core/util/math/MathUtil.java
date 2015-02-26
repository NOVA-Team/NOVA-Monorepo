package nova.core.util.math;

/**
 * Utility class for everything related to numbers.
 * 
 * @author Vic Nightfall
 */
public class MathUtil {

	/**
	 * Returns the smaller number of a and b.
	 * 
	 * @param a
	 * @param b
	 * @return min
	 */
	public static int min(int a, int b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * 
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
	 * 
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static int min(int... numbers) {
		if (numbers.length < 1)
			throw new IllegalArgumentException();
		int min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min)
				min = numbers[i];
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * 
	 * @param a
	 * @param b
	 * @return max
	 */
	public static int max(int a, int b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * 
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
	 * 
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static int max(int... numbers) {
		if (numbers.length < 1)
			throw new IllegalArgumentException();
		int max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max)
				max = numbers[i];
		}
		return max;
	}

	/**
	 * Returns the smaller number of a and b.
	 * 
	 * @param a
	 * @param b
	 * @return min
	 */
	public static double min(double a, double b) {
		return a < b ? a : b;
	}

	/**
	 * Returns the smaller number of a, b and c.
	 * 
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
	 * 
	 * @param numbers Array of numbers
	 * @return min
	 */
	public static double min(double... numbers) {
		if (numbers.length < 1)
			throw new IllegalArgumentException();
		double min = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] < min)
				min = numbers[i];
		}
		return min;
	}

	/**
	 * Returns the bigger number of a and b.
	 * 
	 * @param a
	 * @param b
	 * @return max
	 */
	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	/**
	 * Returns the bigger number of a, b and c.
	 * 
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
	 * 
	 * @param numbers Array of numbers
	 * @return max
	 */
	public static double max(double... numbers) {
		if (numbers.length < 1)
			throw new IllegalArgumentException();
		double max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max)
				max = numbers[i];
		}
		return max;
	}

	/**
	 * Clamps the given number so that {@code min <= a <= max}
	 * 
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
	 * 
	 * @param a
	 * @param min lower limit
	 * @param max upper limit
	 * @return {@code min <= a <= max}
	 */
	public static double clamp(double a, double min, double max) {
		return min(max(a, min), max);
	}
}
