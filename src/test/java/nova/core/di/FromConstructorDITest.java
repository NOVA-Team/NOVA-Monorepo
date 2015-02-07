package nova.core.di;

import static org.junit.Assert.assertNotNull;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;

import org.junit.Test;

import se.jbee.inject.Dependency;

public class FromConstructorDITest extends WithInjector{

	
	@Test
	public void shouldAutomaticallyDetectNovaModModConstructor(){
		
		Class<? extends Loadable> modClass = TestMod.class;
		
		Loadable instance = injector.resolve(Dependency.dependency(modClass));
		
		assertNotNull(instance);
		
	}
	@NovaMod(id = "", name = "", novaVersion = "", version = "")
	public static class TestMod implements Loadable{
		public TestMod(){}
	}

}

