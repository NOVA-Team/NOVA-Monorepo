package nova.core.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Calclavia
 */
public abstract class Manager<T extends Identifiable, F extends Factory<T>> {
	public final Registry<F> registry;

	public Manager(Registry<F> registry) {
		this.registry = registry;
	}

	public F register(Class<? extends T> registerType) {
		return register(() -> ReflectionUtil.newInstance(registerType));
	}

	/**
	 * Register a new object construction factory.
	 * @param constructor Instance supplier {@link Supplier}
	 * @return The factory
	 */
	public abstract F register(Supplier<T> constructor);

	public F register(F factory) {
		registry.register(factory);
		return factory;
	}

	public Optional<F> get(String name) {
		return registry.get(name);
	}
}
