package nova.core.ai;

import java.util.function.Function;
import java.util.function.Supplier;

import nova.core.entity.Entity;

import com.google.common.base.Objects;

@FunctionalInterface
public interface Value<T> {

	public T get(AI<?> ai);

	public default Condition is(Function<T, Boolean> mapper) {
		return ai -> mapper.apply(get(ai));
	}

	public default Condition is(Object other) {
		return ai -> Objects.equal(get(ai), other);
	}

	public default Condition is(Value<?> other) {
		return ai -> Objects.equal(get(ai), other.get(ai));
	}

	public default <U> Value<U> map(Function<T, U> mapper) {
		return ai -> mapper.apply(get(ai));
	}

	public static <T> Value<T> of(T value) {
		return ai -> value;
	}

	public static <T extends Iterable<E>, E> IterableValue<T, E> ofIterable(T value) {
		return ai -> value;
	}

	public static <T> Value<T> of(Supplier<T> supplier) {
		return ai -> supplier.get();
	}

	public static <T extends Iterable<E>, E> IterableValue<T, E> ofIterable(Supplier<T> supplier) {
		return ai -> supplier.get();
	}

	@SuppressWarnings("unchecked")
	public static <T, E extends Entity> Value<T> of(Function<E, T> supplier) {
		return ai -> {
			try {
				return supplier.apply((E) ai.entity);
			} catch (ClassCastException e) {
				throw new RuntimeException("Tried to recieve value " + supplier + " from non matching Entity of type " + ai.entity.getClass());
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T, E extends Entity, U extends Iterable<S>, S> IterableValue<U, S> ofIterable(Function<E, U> supplier) {
		return ai -> {
			try {
				return supplier.apply((E) ai.entity);
			} catch (ClassCastException e) {
				throw new RuntimeException("Tried to recieve value " + supplier + " from non matching Entity of type " + ai.entity.getClass());
			}
		};
	}

	public static interface IterableValue<E extends Iterable<T>, T> extends Value<E> {

		public default Condition areAll(Function<T, Boolean> mapper) {
			return ai -> {
				E value = get(ai);
				for (T sub : value) {
					if (mapper.apply(sub))
						return true;
				}
				return false;
			};
		}
	}
}