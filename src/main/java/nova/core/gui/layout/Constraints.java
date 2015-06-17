package nova.core.gui.layout;

import nova.core.util.ReflectionUtil;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
 * @param <O> Self reference
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

	public static class FlowLayoutConstraints extends Constraints<FlowLayoutConstraints> {

	}

	public static class GridLayoutConstraints extends Constraints<GridLayoutConstraints> {

		public Anchor anchor;
		public Fill fill;
		public int x;
		public int y;
		public int sizeX;
		public int sizeY;
		public int weightX;
		public int weightY;

		public GridLayoutConstraints(int x, int y) {
			this(x, y, Anchor.CENTER);
		}

		public GridLayoutConstraints(int x, int y, Anchor anchor) {
			this(x, y, anchor, Fill.NONE);
		}

		public GridLayoutConstraints(int x, int y, Anchor anchor, Fill fill) {
			this(x, y, 0, 0, anchor, fill);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY) {
			this(x, y, sizeX, sizeY, Anchor.CENTER);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY, Anchor anchor) {
			this(x, y, sizeX, sizeY, anchor, Fill.NONE);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY, Anchor anchor, Fill fill) {
			this(x, y, sizeX, sizeY, 0, 0, anchor, fill);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY, int weightX, int weightY) {
			this(x, y, sizeX, sizeY, weightX, weightY, Anchor.CENTER);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY, int weightX, int weightY, Anchor anchor) {
			this(x, y, sizeX, sizeY, weightX, weightY, anchor, Fill.NONE);
		}

		public GridLayoutConstraints(int x, int y, int sizeX, int sizeY, int weightX, int weightY, Anchor anchor, Fill fill) {
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.weightX = weightX;
			this.weightY = weightY;
			this.anchor = anchor;
			this.fill = fill;
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
	public static <T extends Constraints<T>> T createConstraints(Class<T> clazz, Object... parameters) throws IllegalArgumentException {
		try {
			return ReflectionUtil.newInstanceMatching(clazz, parameters);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"No matching constructor was found for constraint class " + clazz.getClass() +
						" with the arguments " + Arrays.toString(parameters));
		}
	}

	/**
	 * Checks if all public fields of the given constraints object are provided
	 * at runtime, e.g none of the fields have a {@code null} pointer.
	 *
	 * @param constraints
	 * @throws NullPointerException if any of the fields are not supplied
	 */
	public static void assertNonNull(Object constraints) {
		Stream.of(constraints.getClass().getDeclaredFields())
			.filter((field) -> Modifier.isPublic(field.getModifiers()))
			.forEach(Objects::requireNonNull);
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
	 * @throws NullPointerException if the Constraints object contains any null
	 *         value after completion.
	 */
	@SuppressWarnings("unchecked")
	public O of(Consumer<O> consumer) {
		consumer.accept((O) this);
		assertNonNull(this);
		return (O) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public O clone() {
		try {
			return (O) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
