package nova.testutils;

import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import org.junit.Before;
import se.jbee.inject.Injector;

public abstract class WithInjector {
	protected Injector injector;

	@Before
	public void prepare() {
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		diep.init();
		injector = diep.getInjector();
	}

}
