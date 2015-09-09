package nova.core.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public abstract class Manager<T extends Identifiable, F extends Factory<F, T>> {
	public final Registry<F> registry;

	public Manager(Registry<F> registry) {
		this.registry = registry;
	}

	/**
	 * Register a new object construction factory.
	 *
	 * Note that you make use: register(BlockStone::new), passing a method reference.
	 * @param constructor Instance supplier {@link Supplier}
	 * @return The factory
	 */
	public abstract F register(Supplier<T> constructor);

	/**
	 * Register a new object construction factory.
	 * @param factory The construction factory
	 * @return The factory
	 */
	public F register(F factory) {
		registry.register(factory);
		return factory;
	}

	/**
	 * Gets an object by its registered name.
	 * @param name Registered name
	 * @return The object
	 */
	public Optional<F> get(String name) {
		return registry.get(name);
	}
}
