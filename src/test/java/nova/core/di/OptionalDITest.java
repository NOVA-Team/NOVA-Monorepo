package nova.core.di;

import nova.core.util.MockIdentifiable;
import nova.core.util.Registry;
import org.junit.Before;
import org.junit.Test;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.Parameter;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.util.Scoped;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalDITest {
	Injector injector;

	@Before
	public void prepare() {
		injector = Bootstrap.injector(OptionalBundle.class);
	}

	@Test
	public void should_inject_Optional() {

		TestManager m = injector.resolve(Dependency
			.dependency(TestManager.class));

		assertThat(m.map).isPresent();
		assertThat(m.set).isEmpty();
		assertThat(m.map2).isNotSameAs(m.map);
		assertThat(m.map2.get()).isNotSameAs(m.map.get());
	}

	public static class TestManager {
		Optional<Map<Integer, Integer>> map;

		Optional<Registry<MockIdentifiable>> set;
		Optional<Map<Integer, Integer>> map2;

		public TestManager(Optional<Map<Integer, Integer>> map,
		                   Optional<Registry<MockIdentifiable>> set, Optional<Map<Integer, Integer>> map2) {
			this.map = map;
			this.set = set;
			this.map2 = map2;
		}
	}

	public static class MapModule extends BinderModule {
		public MapModule() {
			super(Scoped.INJECTION);
		}

		@Override
		protected void declare() {

			bind(TestManager.class).toConstructor();
			try {
				starbind(Map.class).to(
					HashMap.class.getConstructor(new Class[] {}), (Parameter[]) null);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

	}

	public static class OptionalBundle extends BootstrapperBundle {

		@Override
		protected void bootstrap() {
			install(MapModule.class);
			install(OptionalModule.class);
		}
	}

}
