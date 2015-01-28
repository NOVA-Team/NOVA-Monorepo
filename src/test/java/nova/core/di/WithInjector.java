package nova.core.di;

import nova.bootstrap.DependencyInjectionEntryPoint;

import org.junit.Before;

import se.jbee.inject.Injector;

public abstract class WithInjector {
	protected Injector injector;
	
	@Before
	public  void prepare(){
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		diep.preInit();
		diep.init();
		diep.postInit();
		injector = diep.getInjector().get();
	}
	
}
