package nova.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import nova.bootstrap.WithInjector;

import org.junit.Test;

import com.google.inject.Key;

public class RegistryTest extends WithInjector{
	

	@Test
	public void testRegistry() throws Exception {
			
		Registry<Identifiable> registry = injector.getInstance(new Key<Registry<Identifiable>>(){});

		Identifiable id1 = new MockIdentifiable("ID1");
		Identifiable id2 = new MockIdentifiable("ID2");

		registry.register(id1);
		registry.register(id2);

		assertThat(registry.contains("ID1")).isTrue();
		assertThat(registry.contains("ID2")).isTrue();

		assertThat(registry.get("ID1").get().getID()).isEqualTo("ID1");
		assertThat(registry.get("ID2").get().getID()).isEqualTo("ID2");

		assertThat(registry.get("ID1").get()).isEqualTo(id1);
		assertThat(registry.get("ID2").get()).isEqualTo(id2);

		assertThat(registry.iterator()).containsOnly(id1, id2);

		assertThat(registry.get("None").isPresent()).isFalse();

	}
}
