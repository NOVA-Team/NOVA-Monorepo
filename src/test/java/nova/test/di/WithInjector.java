package nova.test.di;

import java.util.Optional;

import nova.bootstrap.DependencyInjectionEntryPoint;

import org.junit.Before;

import com.google.inject.Injector;

public abstract class WithInjector {
	protected Injector injector;
	
	@Before
	public  void prepare(){
		DependencyInjectionEntryPoint diep = new DependencyInjectionEntryPoint();
		diep.preInit();
		diep.init(Optional.empty());
		diep.postInit(Optional.empty());
		injector = diep.getInjector().get();
	}
	
}
