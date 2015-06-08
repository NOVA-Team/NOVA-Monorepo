package nova.internal.core.di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Supplier;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

/**
 * @author Kubuxu
 */
public class LoggerModule extends BinderModule {

	public LoggerModule() {
		super(Scoped.DEPENDENCY_TYPE);
	}


	@Override
	protected void declare() {
		bind(Logger.class).toSupplier(LoggerSupplier.class);
	}

	public static class LoggerSupplier implements Supplier<Logger>{

		@Override
		public Logger supply(Dependency<? super Logger> dependency, Injector injector) {
			if (dependency.isUntargeted()) {
				return LoggerFactory.getLogger("General");
			} else {
				return LoggerFactory.getLogger(dependency.target().getType().getRawType());
			}
		}
	}
}
