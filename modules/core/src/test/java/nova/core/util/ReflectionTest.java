/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.util;

import org.junit.Test;

import java.lang.reflect.Constructor;

import static nova.core.util.ReflectionUtil.findMatchingConstructor;
import static nova.core.util.ReflectionUtil.newInstanceMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


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
}
