package nova.core.gui.layout;

import java.lang.reflect.Constructor;
import java.util.stream.IntStream;

import nova.core.gui.layout.BorderLayout.EnumBorderRegion;

public abstract class LayoutConstraints {

	public static class BorderLayoutConstraints extends LayoutConstraints {

		public final EnumBorderRegion region;

		public BorderLayoutConstraints(EnumBorderRegion region) {
			this.region = region;
		}

		public BorderLayoutConstraints() {
			this.region = EnumBorderRegion.CENTER;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends LayoutConstraints> T createConstraints(Class<T> clazz, Object... parameters) {

		for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length != parameters.length)
				continue;

			if (IntStream.range(0, parameters.length).allMatch((index) -> {
				return parameterTypes[index].isInstance(parameters[index]);
			})) {
				try {
					return constructor.newInstance((Object[]) parameterTypes);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalArgumentException();
	}
}
