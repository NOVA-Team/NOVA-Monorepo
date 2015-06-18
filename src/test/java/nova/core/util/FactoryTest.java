package nova.core.util;

import org.junit.Test;

import static nova.testutils.NovaAssertions.*;

public class FactoryTest {
	@Test
	public void testClassOfAFactory() {
		assertThat(Factory.of(TestClass.class).clazz).isEqualTo(TestClass.class);
	}

	public static class TestClass implements Buildable<TestClass> {
		@SuppressWarnings("unused")
		private String ID;
		@Override
		public final String getID() {
			return ID;
		}
		@Override
		public final Factory<? extends TestClass> factory() {
			return Factory.of(getClass()).ID(getID());
		}
	}
}