package nova.core.util;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Factories are immutable object builders that create objects.
 * @param <S> The self type
 * @param <T> Type of produced object
 * @author Calclavia
 */
public abstract class Factory<S extends Factory<S, T>, T extends Identifiable> implements Identifiable {
	protected final String id;
	protected final Supplier<T> constructor;
	protected final Function<T, T> processor;

	/**
	 * Creates a new factory with a constructor function that instantiates the factory object,
	 * and with a processor that is capable of mutating the instantiated object after its initialization.
	 *
	 * A factory's processor may be modified to allow specific customization of instantiated objects before it is used.
	 * @param constructor The construction function
	 * @param processor The processor function
	 */
	public Factory(Supplier<T> constructor, Function<T, T> processor) {
		this.constructor = constructor;
		this.processor = processor;

		//TODO: Do blocks really need to store its ID inside? Or should it be stored in the factory? This prevents generating dummies. Consider @Identifiable
		id = build().getID();
	}

	public Factory(Supplier<T> constructor) {
		this(constructor, obj -> obj);
	}

	public S process(Function<T, T> processor) {
		return selfConstructor(constructor, this.processor.compose(processor));
	}

	public abstract S selfConstructor(Supplier<T> constructor, Function<T, T> processor);

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
