package nova.core.util;

import static org.assertj.core.api.Assertions.*;
import static nova.core.util.ReflectionUtil.*;

import java.lang.reflect.Constructor;

import org.junit.Test;


public class ReflectionTest {
	
	static Constructor<?> con_ddd = find(double.class, double.class, double.class);
	static Constructor<?> con_DDD = find(Double.class, Double.class, Double.class);
	static Constructor<?> con_ddi = find(double.class, double.class, int.class);
	static Constructor<?> con_f_Var = find(float[].class);
	static Constructor<?> con_O_Var = find(Object[].class);
	
	static Constructor<?> find(Class<?>... parameterTypes) {
		try {
			return TestClass.class.getConstructor(parameterTypes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("ALL")
	static class TestClass {
		public TestClass(double a, double b, double c) {}

		public TestClass(Double a, Double b, Double c) {}

		public TestClass(double a, double b, int c) {}

		public TestClass(float... f) {}

		public TestClass(Object... a) {}
	}
	
	@Test
	public void testFindConstructor() {
		
		Constructor<?> constr = null;
		
		// Ambigious constructor, should fail.
		assertThatThrownBy(() -> findMatchingConstructor(TestClass.class).get());
		
		assertThat(constr = findMatchingConstructor(TestClass.class, double.class, double.class, int.class).get())
			.isEqualTo(con_ddi);
		assertThat(newInstanceMatching(constr, 1D, 1D, 1)).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, Double.class, Double.class, Integer.class).get())
			.isEqualTo(con_ddi);
		assertThat(newInstanceMatching(constr, Double.valueOf(1), Double.valueOf(1), Integer.valueOf(1))).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, int.class, int.class, int.class).get())
			.isEqualTo(con_ddi);
		assertThat(newInstanceMatching(constr, 1, 1, 1)).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, float[].class).get())
			.isEqualTo(con_f_Var);
		assertThat(newInstanceMatching(constr, new float[5])).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, String.class).get())
			.isEqualTo(con_O_Var);
		assertThat(newInstanceMatching(constr, "Test")).isNotNull();

		assertThat(constr = findMatchingConstructor(TestClass.class, String.class, Object.class, float.class).get())
			.isEqualTo(con_O_Var);
		assertThat(newInstanceMatching(constr, "Test", new Object(), 1F)).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, Object[].class).get())
			.isEqualTo(con_O_Var);
		assertThat(newInstanceMatching(constr, new Object[5])).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, float.class, float.class, float.class).get())
			.isEqualTo(con_ddd);
		assertThat(newInstanceMatching(constr, 1F, 1F, 1F)).isNotNull();
		
		assertThat(constr = findMatchingConstructor(TestClass.class, Double.class, Double.class, Double.class).get())
			.isEqualTo(con_DDD);
		assertThat(newInstanceMatching(constr, Double.valueOf(1), Double.valueOf(1), Double.valueOf(1))).isNotNull();
	}

	public static class WithField {
		private String string;
	}

	@Test
	public void testInjectField() {
		WithField withField = new WithField();
		ReflectionUtil.injectField("string", withField, "injected");
		assertThat(withField.string).isEqualTo("injected");
	}
}
