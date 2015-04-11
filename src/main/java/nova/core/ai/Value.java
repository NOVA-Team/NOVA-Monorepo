package nova.core.ai;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Value<T> extends Supplier<T> {

	public default void set(Consumer<T> consumer) {
		consumer.accept(get());
	}

	public default Condition is(Function<T, Boolean> mapper) {
		return ai -> mapper.apply(get());
	}
}