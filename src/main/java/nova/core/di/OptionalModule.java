package nova.core.di;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;

import java.util.Optional;

public class OptionalModule extends BinderModule {

	public OptionalModule() {
		super(NovaScopes.MULTIPLE_INSTANCES);
	}

	@Override
	protected void declare() {
		starbind(Optional.class).to(new OptionalSupplier());
	}

	public static class OptionalSupplier implements Supplier<Optional<?>> {

		@Override
		public Optional<?> supply(Dependency<? super Optional<?>> dependency,
		                          Injector injector) {
			try {
				return Optional.of(injector.resolve(Dependency.dependency(dependency.getType().getParameters()[0])));
			} catch (Throwable t) {
				return Optional.empty();
			}
		}

	}

}
