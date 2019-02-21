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

package nova.core.wrapper.mc.forge.v1_8.depmodules;

import nova.internal.core.tick.UpdateTicker;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

/**
 * @author Calclavia
 */
public class TickerModule extends BinderModule {
	public static final UpdateTicker.SynchronizedTicker synchronizedTicker = new UpdateTicker.SynchronizedTicker();
	public static final UpdateTicker.ThreadTicker threadTicker = new UpdateTicker.ThreadTicker();
	public static UpdateTicker.TickingThread tickingThread;

	public TickerModule() {
		super(Scoped.DEPENDENCY_TYPE);
	}

	@Override
	protected void declare() {
		bind(UpdateTicker.SynchronizedTicker.class).toSupplier(SynchronizedTickerSupplier.class);
		bind(UpdateTicker.ThreadTicker.class).toSupplier(ThreadTickerSupplier.class);

		/**
		 * Initiated threaded ticker
		 */
		tickingThread = new UpdateTicker.TickingThread(threadTicker, 20);
		tickingThread.start();
	}

	public static class SynchronizedTickerSupplier implements Supplier<UpdateTicker.SynchronizedTicker> {
		@Override
		public UpdateTicker.SynchronizedTicker supply(Dependency<? super UpdateTicker.SynchronizedTicker> dependency, Injector injector) {
			return synchronizedTicker;
		}
	}

	public static class ThreadTickerSupplier implements Supplier<UpdateTicker.ThreadTicker> {
		@Override
		public UpdateTicker.ThreadTicker supply(Dependency<? super UpdateTicker.ThreadTicker> dependency, Injector injector) {
			return threadTicker;
		}
	}
}
