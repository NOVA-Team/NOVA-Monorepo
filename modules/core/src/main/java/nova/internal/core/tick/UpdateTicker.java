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
 */

package nova.internal.core.tick;

import nova.core.component.Updater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Stream;

/**
 * The update ticker is responsible for ticking Update objects.
 * @author Calclavia
 */
public class UpdateTicker {

	/**
	 * A set of Updater that will be ticked.
	 */
	private final Set<Updater> updaters = Collections.newSetFromMap(new WeakHashMap<>());

	private final List<Runnable> preEvents = new ArrayList<>();

	/**
	 * The last update time.
	 */
	private long last;

	private double deltaTime;

	public UpdateTicker() {
		last = System.nanoTime();
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

		long current = System.nanoTime();
		//The time in milliseconds between the last update and this one.
		deltaTime = (current - last) / 1_000_000_000d;
		synchronized (updaters) {
			//TODO: Check the threshold
			Stream<Updater> stream = updaters.size() > 1000 ? updaters.parallelStream() : updaters.stream();
			stream.forEach(t -> t.update(deltaTime));
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
				//noinspection InfiniteLoopStatement
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
