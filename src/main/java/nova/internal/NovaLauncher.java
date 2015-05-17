package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.deps.Dependencies;
import nova.core.deps.Dependency;
import nova.core.deps.MavenDependency;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.util.exception.NovaException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class that launches NOVA mods.
 *
 * Correct order to call the methods is this:
 * <ol>
 *     <li>{@link #generateDependencies()}</li>
 *     <li>{@link #preInit()}</li>
 *     <li>{@link #init()}</li>
 *     <li>{@link #postInit()}</li>
 * </ol>
 *
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher implements Loadable {

	private final DependencyInjectionEntryPoint diep;

	private Map<NovaMod, Loadable> mods;

	private Map<NovaMod, List<MavenDependency>> neededDeps;

	private ArrayList<Loadable> orderedMods;
	private Map<NovaMod, Class<? extends Loadable>> classesMap;
	private Map<NovaMod, Class<?>> scalaClassesMap;
	// TODO: A lot of work and clean up has to be done to ensure this class is
	// not susceptible to
	// other classes touching it.

	/**
	 * Creates NovaLauncher.
	 * @param modClasses mods to instantialize.
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		this.diep = diep;

		classesMap = modClasses.stream()
			.filter(Loadable.class::isAssignableFrom)
			.map(clazz -> (Class<? extends Loadable>) clazz.asSubclass(Loadable.class))
			.filter(clazz -> clazz.getAnnotation(NovaMod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(NovaMod.class), Function.identity())); //Map<NovaMod, Class<? extends Loadable>>

		classesMap.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(diep::install);

		/**
		 * Find scala singleton classes
		 */
		scalaClassesMap = modClasses.stream()
			.filter(c -> !Loadable.class.isAssignableFrom(c))
			.filter(c -> {
				try {
					Class.forName((c.getCanonicalName() + "$"));
					return true;
				} catch (Exception e) {
					return false;
				}
			})
			.collect(Collectors.toMap(c -> c.getAnnotation(NovaMod.class),
				c -> {
					try {
						return Class.forName(c.getCanonicalName() + "$");
					} catch (Exception e) {
						throw new ExceptionInInitializerError("Failed to load NOVA mod: " + c);
					}
				}
			));

	}

	@Override
	public void preInit() {

		// Test integrity of the GuiFactory
		Game.instance.guiComponentFactory.validate();

		/**
		 * Create instances.
		 */
		mods = classesMap.entrySet().stream()
			.collect(Collectors.toMap(Entry::getKey, ((entry) -> {
				try {
					Stream<Constructor<?>> candidates = Arrays.stream(entry.getValue().getConstructors());

					//get constructor with most parameters.
					Optional<Constructor<?>> ocons = candidates.max(Comparator.comparingInt((constructor) -> constructor.getParameterTypes().length));

					Constructor<?> cons = ocons.get();
					Object[] parameters = Arrays.stream(cons.getParameterTypes())
												.map(clazz -> (Object) diep.getInjector().get().resolve(se.jbee.inject.Dependency.dependency(clazz)))
												.collect(Collectors.toList()).toArray();
					return (Loadable) cons.newInstance(parameters);
				} catch (Exception e) {
					System.out.println("Failed to load NOVA Java mod: " + entry.getKey().name());
					throw new ExceptionInInitializerError(e);
				}
			})));

		/**
		 * Get Scala singleton mods
		 */
		Map<NovaMod, Loadable> scalaModsMap = scalaClassesMap.entrySet().stream()
															 .collect(Collectors.toMap(Entry::getKey, entry -> {
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
								f.set(loadable, diep.getInjector().get().resolve(se.jbee.inject.Dependency.dependency(f.getType())));
							} catch (Exception e) {
							}
						}
						f.setAccessible(false);
					}

					return loadable;
				} catch (Exception ex) {
					System.out.println("Failed to load NOVA Scala mod: " + entry.getKey().name());
					throw new ExceptionInInitializerError(ex);
				}
			}));

		mods.putAll(scalaModsMap);

		/**
		 * Re-order mods based on dependencies
		 */
		orderedMods = new ArrayList<>();
		orderedMods.addAll(
			mods.entrySet().stream()
				.sorted((o1, o2) -> {
					// Split string by @ and versions
					Map<String, String> loadAfter = Arrays.stream(o1.getKey().dependencies())
						.map(s -> s.split("@", 1))
														  .collect(Collectors.toMap(s -> s[0], s -> s.length > 1 ? s[1] : ""));
					// TODO: Compare version requirements.
					return loadAfter.containsKey(o2.getKey().id()) ? 1 : 0;
				})
				.map(Entry::getValue)
				.collect(Collectors.toList())
		);

		Game.instance.logger.info("NOVA Mods Loaded: " + mods.size());

		/**
		 * Initialize all the NOVA mods.
		 */
		orderedMods.stream().forEachOrdered((mod) -> {
			try {
				mod.preInit();
			} catch (Throwable t) {
				Game.instance.logger.error("Critical error caught during pre initalization phase", t);
				throw new NovaException(t);
			}
		});
	}

	@Override
	public void init() {
		orderedMods.stream().forEachOrdered((mod) -> {
			try {
				mod.init();
			} catch (Throwable t) {
				Game.instance.logger.error("Critical error caught during initalization phase", t);
				throw new NovaException(t);
			}
		});
	}

	@Override
	public void postInit() {
		orderedMods.stream().forEachOrdered((mod) -> {
			try {
				mod.postInit();
			} catch (Throwable t) {
				Game.instance.logger.error("Critical error caught during post initalization phase", t);
				throw new NovaException(t);
			}
		});
	}

	public Set<NovaMod> getLoadedMods() {
		return mods.keySet();
	}

	public Map<NovaMod, Loadable> getLoadedModMap() {
		return mods;
	}

	public Map<NovaMod, Class<? extends Loadable>> getModClasses() {
		return classesMap;
	}

	public Map<NovaMod, Class<?>> getScalaClassesMap() {
		return scalaClassesMap;
	}

	public Map<NovaMod, List<MavenDependency>> getNeededDeps() {
		if (neededDeps == null) throw new IllegalStateException("Dependencies have not been generated");
		return neededDeps;
	}

	/**
	 * Get the dependencies. Separated from preInit due to issues with ordering in case mods need to download mods before the preInit method is called.
	 * The wrapper just needs to call this method right before it downloads the dependencies.
	 */
	public void generateDependencies() {
		neededDeps = new HashMap<>(); // This should be cleaned every time this method is run.

		Stream.concat(classesMap.values().stream(), scalaClassesMap.values().stream())
			.forEach(this::generateAndAddDependencies);
	}


	private void generateAndAddDependencies(Class<?> mod) {
		List<MavenDependency> deps;
		if (mod.isAnnotationPresent(Dependency.class)) {
			Dependency dependency = mod.getAnnotation(Dependency.class);
			deps = Collections.singletonList(new MavenDependency(dependency));
		} else if (mod.isAnnotationPresent(Dependencies.class)) {
			Dependency[] dependencies = mod.getAnnotation(Dependencies.class).value();
			deps = Arrays.stream(dependencies).map(MavenDependency::new).collect(Collectors.toList());
		} else return;

		neededDeps.put(mod.getAnnotation(NovaMod.class), deps);
	}

}
