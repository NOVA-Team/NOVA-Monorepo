package nova.core.util;

import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple stop watch profiler.
 * @author Calclavia
 */
public class Profiler {

	public final String name;
	private final List<Long> lapped = new ArrayList<>();
	private long time;

	public Profiler(String name) {
		this.name = name;
	}

	/**
	 * Start's the profiler
	 *
	 * @return The started profiler
	 */
	public Profiler start() {
		time = System.currentTimeMillis();
		return this;
	}

	/**
	 * Stops the profiler
	 *
	 * @return How much time elapsed (in seconds)
	 */
	public double end() {
		Game.logger().info(toString());
		return elapsed() / 1000d;
	}

	/**
	 * @return How much time elapsed since the profiler was started (in miliseconds)
	 */
	public long elapsed() {
		return System.currentTimeMillis() - time;
	}

	/**
	 * @return The average time of the laps (in seconds)
	 */
	public double average() {
		return lapped.stream().mapToDouble(value -> value / 1000d).sum() / lapped.size();
	}

	/**
	 * Starts a new lap
	 * @return The profiler
	 */
	public Profiler lap() {
		lapped.add(elapsed());
		return start();
	}

	@Override
	public String toString() {
		return name + " took " + (elapsed() / 1000d) + " seconds";
	}
}
