package nova.core.util;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Calclavia
 */
public abstract class Manager<T extends Buildable> {
	public final Registry<Factory<T> > registry;

	public Manager(Registry<Factory<T>> registry) {
		this.registry = registry;
	}

	public final List<Factory<T>> register(Class<T> registerType) {
		Set<Factory<T>> set = Buildable.factoriesFor(registerType);
		return set.stream().map(this::register).collect(Collectors.toList());
	}

	public Factory<T> register(Factory<T> factory) {
		registry.register(factory);
		return factory;
	}

	public Optional<T> get(String name) {
		Optional<Factory<T>> factory = getFactory(name);
		return factory.map(f -> f.resolve());
	}

	public Optional<T> get(String name, Object ... instanceArguments) {
		Optional<Factory<T>> factory = getFactory(name);
		return factory.map(f -> f.resolve(instanceArguments));
	}

	public Optional<Factory<T>> getFactory(String name) {
		return registry.get(name);
	}
}
