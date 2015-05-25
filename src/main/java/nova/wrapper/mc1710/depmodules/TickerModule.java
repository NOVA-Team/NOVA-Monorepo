package nova.wrapper.mc1710.depmodules;

import nova.internal.tick.UpdateTicker;
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
