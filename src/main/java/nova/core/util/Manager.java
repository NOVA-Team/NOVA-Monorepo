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
public abstract class Manager<T extends Buildable<T>> {
	private final Registry<Factory<? extends T>> registry;
	private final Class<T> classOfBinding;

	public Manager(Registry<Factory<? extends T>> registry, GameStatusEventBus gseb, Class<T> classOfBinding) {
		this.registry = registry;
		this.classOfBinding = classOfBinding;
		//noinspection Convert2MethodRef
		gseb.on(GameStatusEventBus.Init.class).withPriority(EventBus.PRIORITY_HIGH).bind(event -> Game.diep().add(() -> new ManagerModule()));
	}

	public final List<Factory<? extends T>> register(Class<? extends T> registerType) {
		Buildable.factoriesFor(classOfBinding);
		Set<Factory<? extends T>> set = Buildable.factoriesFor(registerType);
		return set.stream().map(this::register).collect(Collectors.toList());
	}

	public final Factory<? extends T> register(Factory<? extends T> factory) {
		factory = beforeRegister(factory);

		registry.register(factory);
		return factory;
	}

	protected Factory<? extends T> beforeRegister(Factory<? extends T> factory) {
		return factory;
	}

	public Optional<? extends T> make(String name) {
		Optional<Factory<? extends T>> factory = getFactory(name);
		return factory.map(f -> f.make());
	}

	public Optional<? extends T> make(String name, Object... instanceArguments) {
		Optional<Factory<? extends T>> factory = getFactory(name);
		return factory.map(f -> f.make(instanceArguments));
	}

	public Optional<Factory<? extends T>> getFactory(String name) {
		return registry.get(name);
	}

	public boolean contains(String name) {
		return registry.contains(name);
	}

	public Set<Factory<? extends T>> all() {
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
