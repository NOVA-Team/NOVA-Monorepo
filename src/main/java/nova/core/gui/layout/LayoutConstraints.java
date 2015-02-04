package nova.core.gui.layout;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import nova.core.gui.layout.BorderLayout.EnumBorderRegion;

public abstract class LayoutConstraints<O extends LayoutConstraints<O>> implements Cloneable {

	public static class BorderLayoutConstraints extends LayoutConstraints<BorderLayoutConstraints> {

		public EnumBorderRegion region;

		public BorderLayoutConstraints(EnumBorderRegion region) {
			this.region = region;
		}

		public BorderLayoutConstraints() {
			this.region = EnumBorderRegion.CENTER;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends LayoutConstraints<T>> T createConstraints(Class<T> clazz, Object... parameters) {

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

	@SuppressWarnings("unchecked")
	public O of(Consumer<O> consumer) {
		return (O) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected O clone() {
		try {
			return (O) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
