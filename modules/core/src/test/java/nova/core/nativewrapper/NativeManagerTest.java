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

package nova.core.nativewrapper;

import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

public class NativeManagerTest {
	@Test
	public void testConverters() {
		NativeManager manager = new NativeManager();
		TestConverter converter = new TestConverter();
		Type1 t1 = new Type1(10);
		Type2 t2 = new Type2(10);
		manager.registerConverter(converter);
		assertThat((Type2) manager.toNative(t1)).isEqualTo(t2);
		assertThat((Type1) manager.toNova(t2)).isEqualTo(t1);
	}

	public static class Type1 {
		public int val;

		public Type1(int v) {
			val = v;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Type1 && ((Type1) obj).val == val;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 67 * hash + this.val;
			return hash;
		}
	}

	public static class Type2 {
		public int val;

		public Type2(int v) {
			val = v;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Type2 && ((Type2) obj).val == val;
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 67 * hash + this.val;
			return hash;
		}
	}

	public static class TestConverter implements NativeConverter<Type1, Type2> {
		@Override
		public Type2 toNative(Type1 o) {
			return new Type2(o.val);
		}

		@Override
		public Type1 toNova(Type2 o) {
			return new Type1(o.val);
		}

		@Override
		public Class<Type1> getNovaSide() {
			return Type1.class;
		}

		@Override
		public Class<Type2> getNativeSide() {
			return Type2.class;
		}
	}
}
