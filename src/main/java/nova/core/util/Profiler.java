package nova.core.util;

import nova.internal.core.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple stop watch profiler. All results are in microseconds.
 */
public final class Profiler {

	/**
	 * Name of this profiler
	 */
	public final String name;

	private final List<Double> lapped = new ArrayList<>();
	private long time = -1;
	private double lastTime = 0;

	/**
	 * Creates new profiler.
	 * @param name of this profiler.
	 */
	public Profiler(String name) {
		this.name = name;
	}

	/**
	 * Starts this profiler.
	 * @throws IllegalStateException if profiler is currently running. See {@link Profiler#isRunning()}
	 * @return {@code this} for chaining.
	 */
	public Profiler start() {
		assureRightState("start", false);

		time = System.nanoTime();
		return this;
	}

	/**
	 * Ends current profiling session. Saves result for average calculations.
	 * @throws IllegalStateException if profiler is not currently running. See {@link Profiler#isRunning()}
	 * @return time elapsed since last start or lap measured in microseconds.
	 */
	public double end() {
		assureRightState("end", true);

		lastTime = elapsed();
		lapped.add(lastTime);
		time = -1;
		return lastTime;
	}

	/**
	 * Helper method for loop time measurements.
	 * Class {@link Profiler#end()} and then {@link Profiler#start()}
	 * @throws IllegalStateException if profiler is not currently running. See {@link Profiler#isRunning()}
	 * @return time in microseconds previous lap took.
	 */
	public double lap() {
		assureRightState("lap", true);

		end();
		start();
		return lastTime;
	}

	/**
	 * States whether profiler is currently performing time measurements.
	 * @return {@code true} if profiler is running.
	 */
	public boolean isRunning() {
		return time != -1;
	}

	/**
	 * Measures time since last (lap)start of this profiler.
	 * @throws IllegalStateException if profiler is not currently running. See {@link Profiler#isRunning()}
	 * @return time elapsed since last start or lap measured in microseconds.
	 */
	public double elapsed() {
		assureRightState("elapsed", true);

		return (System.nanoTime() - time) / 1000d;
	}

	/**
	 * Getter for last cycle time.
	 * @return time in microseconds between last start-lap/lap-lap/lap-end or start-end cycle.
	 */
	public double lastTime() {
		return lastTime;
	}

	/**
	 * Calculates average time between cycles.
	 * @return average time in microseconds.
	 */
	public double average() {
		return lapped.stream().mapToDouble(Double::doubleValue).sum() / lapped.size();
	}

	/**
	 * Getter for lap timing.
	 * @return unmodifiable list of elapsed times between cycles.
	 */
	public List<Double> results() {
		return Collections.unmodifiableList(lapped);
	}

	/**
	 * Clears saved results for average computations.
	 * @return this for chaining.
	 */
	public Profiler clearResults() {
		lapped.clear();
		return this;
	}

	@Override
	public String toString() {
		return name + " took " + (lastTime / 1e6d) + " seconds";
	}

	private void assureRightState(String method, boolean shouldBeRunning) {
		if (shouldBeRunning ^ isRunning()) {
			throw new IllegalStateException(String.format("The profiler's method: <%s> was called while it %s running.", method, isRunning() ? "was" : "wasn't"));
		}
	}
}
