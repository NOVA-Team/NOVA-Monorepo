package nova.core.util;

import nova.core.game.Game;

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

	public Profiler start() {
		time = System.currentTimeMillis();
		return this;
	}

	public double end() {
		Game.logger().info(toString());
		return elapsed() / 1000d;
	}

	public long elapsed() {
		return System.currentTimeMillis() - time;
	}

	public double average() {
		return lapped.stream().mapToDouble(value -> value / 1000d).sum() / lapped.size();
	}

	public Profiler lap() {
		lapped.add(elapsed());
		return start();
	}

	@Override
	public String toString() {
		return name + " took " + (elapsed() / 1000d) + " seconds";
	}
}
