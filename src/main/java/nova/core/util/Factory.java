package nova.core.util;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factories are immutable object builders that create objects.
 * @param <T> Type of produced object
 * @author Calclavia
 */
public class Factory<T extends Identifiable> implements Identifiable {
	protected final Supplier<T> constructor;
	protected final String id;
	protected Function<T, T> processor = obj -> obj;

	public Factory(Supplier<T> constructor) {
		this.constructor = constructor;

		//TODO: Do blocks really need to store its ID inside? Or should it be stored in the factory? This prevents generating dummies.
		id = build().getID();
	}

	public Factory<T> process(Function<T, T> processor) {
		this.processor.compose(processor);
		return this;
	}

	/**
	 * @return A new instance of T based on the construction method
	 */
	public T build() {
		return processor.apply(constructor.get());
	}

	public String getID() {
		return id;
	}
}
