package nova.core.util;

import nova.core.event.EventBus;
import nova.core.game.GameStatusEventBus;
import nova.internal.core.Game;
import nova.internal.core.di.NovaScopes;
import se.jbee.inject.Name;
import se.jbee.inject.Type;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.bootstrap.Inspect;
import se.jbee.inject.util.Scoped;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Calclavia
 */
public abstract class Manager<T extends Buildable> {
	private final Registry<Factory<T>> registry;
	private final Class<T> classOfBinding;

	public Manager(Registry<Factory<T>> registry, GameStatusEventBus gseb, Class<T> classOfBinding) {
		this.registry = registry;
		this.classOfBinding = classOfBinding;
		//noinspection Convert2MethodRef
		gseb.on(GameStatusEventBus.Init.class).withPriority(EventBus.PRIORITY_HIGH).bind(event -> Game.diep().add(() -> new ManagerModule()));
	}

	public final List<Factory<T>> register(Class<? extends T> registerType) {
		@SuppressWarnings("unchecked")
		Set<Factory<T>> set = Buildable.factoriesFor((Class<T>) registerType);
		return set.stream().map(this::register).collect(Collectors.toList());
	}

	public final Factory<T> register(Factory<T> factory) {
		factory = beforeRegister(factory);

		registry.register(factory);
		return factory;
	}

	protected Factory<T> beforeRegister(Factory<T> factory) {
		return factory;
	}

	public Optional<T> make(String name) {
		Optional<Factory<T>> factory = getFactory(name);
		return factory.map(f -> f.make());
	}

	public Optional<T> make(String name, Object... instanceArguments) {
		Optional<Factory<T>> factory = getFactory(name);
		return factory.map(f -> f.make(instanceArguments));
	}

	public Optional<Factory<T>> getFactory(String name) {
		return registry.get(name);
	}

	public boolean contains(String name) {
		return registry.contains(name);
	}

	public Set<Factory<T>> all() {
		return registry.values();
	}

	public class ManagerModule extends BinderModule {

		@Override
		protected void declare() {

			registry.stream().forEach((tFactory -> {
				ReflectionUtil.forEachSuperClassUpTo(tFactory.clazz, classOfBinding, aClass -> {
					per(Scoped.APPLICATION).bind(Name.named(tFactory.getID()), Type.raw(Factory.class).parametized(aClass)).to(tFactory);
				});

				per(NovaScopes.MULTIPLE_INSTANCES).bind(Name.named(tFactory.getID()), tFactory.clazz).toConstructor();//.bind(Inspect.DEFAULT.namedBy(Buildable.WithID.class)).in(tFactory.clazz);
			}));
		}
	}

}
