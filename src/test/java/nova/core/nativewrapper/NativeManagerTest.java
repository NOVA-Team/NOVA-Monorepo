package nova.core.nativewrapper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NativeManagerTest {
	public static class Type1 {
		public Type1(int v) { val = v; }

		public int val;

		public boolean equals(Object obj) {
			return obj instanceof Type1 && ((Type1) obj).val == val;
		}
	}

	public static class Type2 {
		public Type2(int v) { val = v;}

		public int val;

		public boolean equals(Object obj) {
			return obj instanceof Type2 && ((Type2) obj).val == val;
		}
	}

	public static class TestConverter implements NativeConverter<Type1, Type2> {
		public Type2 toNative(Type1 o) {
			return new Type2(o.val);
		}

		public Type1 toNova(Type2 o) {
			return new Type1(o.val);
		}

		public Class<Type1> getNovaSide() {
			return Type1.class;
		}

		public Class<Type2> getNativeSide() {
			return Type2.class;
		}
	}

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
}
