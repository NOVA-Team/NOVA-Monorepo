package nova.internal.tick;

import nova.core.component.Updater;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * The update ticker is responsible for ticking Update objects.
 *
 * @author Calclavia
 */
public class UpdateTicker {

	/**
	 * A set of Updater that will be ticked.
	 */
	private final Set<Updater> updaters = Collections.newSetFromMap(new WeakHashMap<>());

	private final Set<Runnable> preEvents = Collections.newSetFromMap(new WeakHashMap<>());

	/**
	 * The last update time.
	 */
	private long last;

	private long deltaTime;

	public UpdateTicker() {
		last = System.currentTimeMillis();
	}

	public void add(Updater ticker) {
		synchronized (updaters) {
			updaters.add(ticker);
		}
	}

	public void remove(Updater ticker) {
		synchronized (updaters) {
			updaters.remove(ticker);
		}
	}

	/**
	 * Queues an event to be executed.
	 *
	 * @param func Event to be executed.
	 */
	public void preQueue(Runnable func) {
		synchronized (preEvents) {
			preEvents.add(func);
		}
	}

	public void update() {

		synchronized (preEvents) {
			preEvents.forEach(Runnable::run);
			preEvents.clear();
		}

		long current = System.currentTimeMillis();
		//The time in milliseconds between the last update and this one.
		deltaTime = (last - current) / 1000;
		synchronized (updaters) {
			updaters.parallelStream().forEach(t -> t.update(deltaTime));
		}
		last = current;

	}

	public double getDeltaTime() {
		return deltaTime;
	}

	/**
	 * A synchronized ticker ticks using the game's update loop.
	 */
	public static class SynchronizedTicker extends UpdateTicker {
	}

	/**
	 * A thread ticker ticks using the NOVA's update loop.
	 */
	public static class ThreadTicker extends UpdateTicker {
	}

	/**
	 * A thread ticker ticks independent on the game's update loop.
	 */
	public static class TickingThread extends Thread {
		public final UpdateTicker ticker;
		public final int tps;
		public final long sleepMillis;
		public boolean pause = false;

		public TickingThread(UpdateTicker ticker, int tps) {
			setName("Nova Thread");
			setPriority(Thread.MIN_PRIORITY);
			this.ticker = ticker;
			this.tps = tps;
			this.sleepMillis = 1 / tps * 1000;
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (!pause) {
						ticker.update();
					}

					Thread.sleep(sleepMillis);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
