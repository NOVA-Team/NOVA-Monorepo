package nova.test.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import static org.junit.Assert.assertThat;

public class OptinalDependency {
	Injector injector;
	@Before
	public  void prepare(){
		injector = Guice.createInjector(new OptionalTestModule());
	}
	@Test
	public void should_inject_Optional(){
		Optional<Map> om = injector.getInstance(Key.get(new TypeLiteral<Optional<Map>>() {}));
		
	}
	
	
	class OptionalTestModule extends AbstractModule{
		@Override
		protected void configure() {
			bind(Map.class).to(HashMap.class);
			binder().bindListener(new OptionalMatcher(), new OptionalListener());
			
		}
	}
	class OptionalListener implements TypeListener{

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
			System.out.println("tre");
		}
	}
	class OptionalMatcher extends AbstractMatcher<TypeLiteral<?>>{

		@Override
		public boolean matches(TypeLiteral<?> t) {
			System.out.println(t);
			return t.getRawType().equals(Optional.class);
		}
	}
}
