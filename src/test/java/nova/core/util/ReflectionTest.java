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
	
	static class TestClass {	
		public TestClass(double a, double b, double c) {};	
		public TestClass(Double a, Double b, Double c) {};
		public TestClass(double a, double b, int c) {};
		public TestClass(float... f) {};
		public TestClass(Object... a) {};
	}
	
	@Test
	public void testFindConstructor() {
		
		assertThat(findMatchingConstructor(TestClass.class, double.class, double.class, int.class).get())
			.isEqualTo(con_ddi);
		
		assertThat(findMatchingConstructor(TestClass.class, Double.class, Double.class, Integer.class).get())
			.isEqualTo(con_ddi);
		
		assertThat(findMatchingConstructor(TestClass.class, int.class, int.class, int.class).get())
			.isEqualTo(con_ddi);
		
		assertThat(findMatchingConstructor(TestClass.class, float[].class).get())
			.isEqualTo(con_f_Var);
		
		assertThat(findMatchingConstructor(TestClass.class, String.class).get())
			.isEqualTo(con_O_Var);
		
		assertThat(findMatchingConstructor(TestClass.class).get())
			.isEqualTo(con_O_Var);
		
		assertThat(findMatchingConstructor(TestClass.class, String.class, Object.class, float.class).get())
			.isEqualTo(con_O_Var);
		
		assertThat(findMatchingConstructor(TestClass.class, Object[].class).get())
			.isEqualTo(con_O_Var);
		
		assertThat(findMatchingConstructor(TestClass.class, float.class, float.class, float.class).get())
			.isEqualTo(con_ddd);
		
		assertThat(findMatchingConstructor(TestClass.class, Double.class, Double.class, Double.class).get())
			.isEqualTo(con_DDD);
	}
}
