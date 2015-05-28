package nova.wrappertests.depmodules;

import nova.internal.tick.UpdateTicker;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeTickerModule extends BinderModule {

	@Override
	protected void declare() {
		bind(UpdateTicker.SynchronizedTicker.class).toSupplier(SynchronizedTickerSupplier.class);
		bind(UpdateTicker.ThreadTicker.class).toSupplier(ThreadTickerSupplier.class);
	}

	public static class SynchronizedTickerSupplier implements Supplier<UpdateTicker.SynchronizedTicker> {
		@Override
		public UpdateTicker.SynchronizedTicker supply(Dependency<? super UpdateTicker.SynchronizedTicker> dependency, Injector injector) {
			return new FakeSyncTicker();
		}
	}

	public static class ThreadTickerSupplier implements Supplier<UpdateTicker.ThreadTicker> {
		@Override
		public UpdateTicker.ThreadTicker supply(Dependency<? super UpdateTicker.ThreadTicker> dependency, Injector injector) {
			return new FakeThreadTicker();
		}
	}

	public static class FakeSyncTicker extends UpdateTicker.SynchronizedTicker {

	}

	public static class FakeThreadTicker extends UpdateTicker.ThreadTicker {

	}
}
