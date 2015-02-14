package nova.core.gui.layout;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * <p>
 * Constraints are mutable objects that represent a set of properties. Every
 * public field of the constraints has to be either immutable or implement
 * {@link Cloneable}.
 * </p>
 * 
 * <p>
 * You can use a lambda consumer with {@link #of(Consumer)} to change the
 * constraints inline. <b>In order to use a contraints object, use
 * {@link #clone()} first!</b>
 * </p>
 * 
 * @author Vic Nightfall
 * @param <O> -Describe me-
 */
public abstract class Constraints<O extends Constraints<O>> implements Cloneable {

	/**
	 * Constraints object for the {@link BorderLayout}. Has an optional
	 * {@link Anchor} field.
	 * 
	 * @author Vic Nightfall
	 */
	public static class BorderConstraints extends Constraints<BorderConstraints> {

		public Anchor region;

		public BorderConstraints(Anchor region) {
			this.region = region;
		}

		public BorderConstraints() {
			this.region = Anchor.CENTER;
		}
	}

	/**
	 * Creates a new constraint based on an array of properties. It will try to
	 * choose a valid constructor for the passed arguments and invoke it.
	 * 
	 * @param <T> constraints type
	 * @param clazz constraints class
	 * @param parameters array of object passed to a constructor of the class
	 * @return instanced constraints object
	 * 
	 * @throws IllegalArgumentException if there is no matching constructor for
	 *         the given set of arguments
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Constraints<T>> T createConstraints(Class<T> clazz, Object... parameters) 
		throws IllegalArgumentException {

		for (Constructor<T> constructor : (Constructor<T>[]) clazz.getConstructors()) {
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			if (parameterTypes.length != parameters.length)
				continue;

			if (IntStream.range(0, parameters.length).allMatch((index) -> {
				return parameterTypes[index].isInstance(parameters[index]);
			})) {
				try {
					return constructor.newInstance((Object[]) parameters);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Allows to modify this constraints object from a passed lambda consumer,
	 * inline.
	 * 
	 * <pre>
	 * <code>
	 * constraints.of(c -&gt; {
	 * 	c.fielda = "Field A";
	 * 	c.fieldb = 5;
	 * 	c.fieldc = Enum.CONSTANT;
	 * })
	 * </code>
	 * </pre>
	 * 
	 * @param consumer consumer that accepts this constrains object
	 * @return modified constraints.
	 */
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
