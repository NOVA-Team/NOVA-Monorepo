package nova.core.util;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Calclavia
 */
public abstract class Manager<T extends Identifiable, F extends Factory<T>> {
	public final Registry<F> registry;

	public Manager(Registry<F> registry) {
		this.registry = registry;
	}

	public F register(Class<? extends T> registerType) {
		return register((args) -> ReflectionUtil.newInstance(registerType, args));
	}

	public abstract F register(Function<Object[], T> constructor);

	public abstract F register(F factory);

	public Optional<T> get(String name) {
		Optional<F> factory = getFactory(name);

		if (factory.isPresent()) {
			return Optional.of(factory.get().getDummy());
		}

		return Optional.empty();
	}

	public Optional<F> getFactory(String name) {
		return registry.get(name);
	}
}
