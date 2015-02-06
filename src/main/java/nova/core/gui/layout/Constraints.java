package nova.core.gui.layout;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public abstract class Constraints<O extends Constraints<O>> implements Cloneable {

	public static class BorderConstraints extends Constraints<BorderConstraints> {

		public Anchor region;

		public BorderConstraints(Anchor region) {
			this.region = region;
		}

		public BorderConstraints() {
			this.region = Anchor.CENTER;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Constraints<T>> T createConstraints(Class<T> clazz, Object... parameters) {

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
