package nova.internal.tick;

import nova.core.util.components.Updater;

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
			preEvents.forEach(e -> e.run());
			preEvents.clear();
		}

		long current = System.currentTimeMillis();
		//The time in milliseconds between the last update and this one.
		double deltaTime = (last - current) / 1000;
		synchronized (updaters) {
			updaters.parallelStream().forEach(t -> t.update(deltaTime));
		}
		last = current;

	}

	public static class ThreadTicker extends Thread {

		private final UpdateTicker ticker = new UpdateTicker();
		public boolean pause = false;

		public ThreadTicker() {
			setName("Nova Thread");
			setPriority(Thread.MIN_PRIORITY);
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (!pause) {
						ticker.update();
					}

					//TODO: Allow a variable tick rate based on. Temporarily, it is based on 20 ticks per second.
					Thread.sleep(50);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
