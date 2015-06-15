package nova.internal.core.launch;


import nova.core.game.GameStatusEventBus;
import nova.internal.core.bootstrap.DependencyInjectionEntryPoint;
import nova.core.loader.Loadable;
import nova.internal.core.Game;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Calclavia
 */
public class ModLoader<ANNOTATION extends Annotation> implements Loadable {

	protected final DependencyInjectionEntryPoint diep;

	/**
	 * The type of the annotation
	 */
	protected final Class<ANNOTATION> annotationType;

	/**
	 * Mod Java classes
	 */
	protected final Map<ANNOTATION, Class<? extends Loadable>> javaClasses;

	/**
	 * Mod Scala classes
	 */
	protected final Map<ANNOTATION, Class<?>> scalaClasses;

	/**
	 * Holds the instances of mods
	 */
	protected Map<ANNOTATION, Loadable> mods;

	protected List<Loadable> orderedMods;

	public ModLoader(Class<ANNOTATION> annotationType, DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		this.diep = diep;
		this.annotationType = annotationType;

		/**
		 * Final Java Classes
		 */
		javaClasses = modClasses.stream()
			.filter(Loadable.class::isAssignableFrom)
			.map(clazz -> (Class<? extends Loadable>) clazz.asSubclass(Loadable.class))
			.filter(clazz -> clazz.getAnnotation(annotationType) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(annotationType), Function.identity()));

		/**
		 * Find Scala Singleton Classes
		 */
		scalaClasses = modClasses.stream()
			.filter(c -> !Loadable.class.isAssignableFrom(c))
			.filter(c -> {
				try {
					Class.forName((c.getCanonicalName() + "$"));
					return true;
				} catch (Exception e) {
					return false;
				}
			})
			.collect(Collectors.toMap(c -> c.getAnnotation(annotationType),
					c -> {
						try {
							return Class.forName(c.getCanonicalName() + "$");
						} catch (Exception e) {
							throw new ExceptionInInitializerError("Failed to load NOVA mod: " + c);
						}
					}
				)
			);
	}

	public <T> T makeObjectWithDep(Class<T> classToConstruct) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		Stream<Constructor<?>> candidates = Arrays.stream(classToConstruct.getConstructors());

		//get constructor withPriority most parameters.
		Optional<Constructor<?>> ocons = candidates.max(Comparator.comparingInt((constructor) -> constructor.getParameterTypes().length));

		@SuppressWarnings("unchecked")
		Constructor<T> cons = (Constructor<T>) ocons.get();

		Object[] parameters = Arrays.stream(cons.getParameterTypes())
			.map(clazz -> (Object) diep.getInjector().resolve(se.jbee.inject.Dependency.dependency(clazz)))
			.collect(Collectors.toList()).toArray();

		//noinspection unchecked
		return (T) cons.newInstance(parameters);

	}

	public void load() {

		mods = new HashMap<>();

		/**
		 * Instantiate Java mods
		 */
		mods.putAll(
			javaClasses.entrySet().stream()
				.collect(Collectors.<Map.Entry<ANNOTATION, Class<? extends Loadable>>, ANNOTATION, Loadable>toMap(Map.Entry::getKey,
						entry -> {
							try {
								return makeObjectWithDep((Class<Loadable>) entry.getValue());
							} catch (Exception ex) {
								System.out.println("Failed to load NOVA Java mod: " + entry);
								throw new ExceptionInInitializerError(ex);
							}
						}
					)
				)
		);

		/**
		 * Instantiate Scala singleton mods
		 */
		mods.putAll(
			scalaClasses.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey,
						entry -> {
							try {
								Field field = entry.getValue().getField("MODULE$");
								Loadable loadable = (Loadable) field.get(null);

								//Inject dependencies to Scala singleton variables
								//TODO: Does not work recursively for all hierarchy
								Field[] fields = loadable.getClass().getDeclaredFields();

								for (Field f : fields) {
									f.setAccessible(true);
									if (f.get(loadable) == null) {
										try {
											f.set(loadable, diep.getInjector().resolve(se.jbee.inject.Dependency.dependency(f.getType())));
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									f.setAccessible(false);
								}

								return loadable;
							} catch (Exception ex) {
								System.out.println("Failed to load NOVA Scala mod: " + entry);
								throw new ExceptionInInitializerError(ex);
							}
						}
					)
				)
		);

		orderedMods = new ArrayList<>();
		orderedMods.addAll(mods.values());
	}

	@Override
	public void preInit() {
		Game.gameStatusEventBus().publish(new GameStatusEventBus.PreInit());
		Game.diep().flush();
		orderedMods.stream().forEachOrdered(mod -> {
			try {
				mod.preInit();
			} catch (Throwable t) {
				Game.logger().error("Critical error caught during pre initialization phase", t);
				throw new InitializationException(t);
			}
		});
	}

	@Override
	public void init() {
		Game.gameStatusEventBus().publish(new GameStatusEventBus.Init());
		Game.diep().flush();
		orderedMods.stream().forEachOrdered(mod -> {
			try {
				mod.init();
			} catch (Throwable t) {
				Game.logger().error("Critical error caught during initialization phase", t);
				throw new InitializationException(t);
			}
		});
	}

	@Override
	public void postInit() {
		Game.gameStatusEventBus().publish(new GameStatusEventBus.PostInit());
		Game.diep().flush();
		orderedMods.stream().forEachOrdered(mod -> {
			try {
				mod.postInit();
			} catch (Throwable t) {
				Game.logger().error("Critical error caught during post initialization phase", t);
				throw new InitializationException(t);
			}
		});
	}

	public Set<ANNOTATION> getLoadedMods() {
		return mods.keySet();
	}

	public Map<ANNOTATION, Loadable> getLoadedModMap() {
		return new HashMap<>(mods);
	}

	public Map<ANNOTATION, Class<? extends Loadable>> getModClasses() {
		return new HashMap<>(javaClasses);
	}

	public Map<ANNOTATION, Class<?>> getScalaClassesMap() {
		return new HashMap<>(scalaClasses);
	}
}
