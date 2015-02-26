package nova.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.deps.MavenDependency;
import nova.core.deps.DependencyProvider;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.util.exception.NovaException;

/**
 * The main class that launches NOVA mods.
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher implements Loadable {

	private final Set<Class<?>> modClasses;
	private final DependencyInjectionEntryPoint diep;

	private Map<NovaMod, Loadable> mods;

	private Map<NovaMod, MavenDependency[]> neededDeps;

	private ArrayList<Loadable> orderedMods;
	private Map<NovaMod, Class<? extends Loadable>> classesMap;
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
		this.modClasses = modClasses;

		classesMap = modClasses.stream()
			.filter(Loadable.class::isAssignableFrom)
			.map(clazz -> (Class<? extends Loadable>) clazz.asSubclass(Loadable.class))
			.filter(clazz -> clazz.getAnnotation(NovaMod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(NovaMod.class), Function.identity())); //Map<NovaMod, Class<? extends Loadable>>

		classesMap.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(diep::install);

	}

	@Override
	public void preInit() {

		// Test integrity of the GuiFactory
		Game.instance.guiComponentFactory.validate();

		/**
		 * Create instances.
		 */
		mods = classesMap
			.entrySet()
			.stream()
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
					throw new ExceptionInInitializerError(e);
				}
			})));

		/**
		 * Handle Scala singleton mods
		 */
		Map<NovaMod, Loadable> scalaModsMap = modClasses.stream()
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
						Class singletonClass = Class.forName(c.getCanonicalName() + "$");
						Field field = singletonClass.getField("MODULE$");
						return (Loadable) field.get(null);
					} catch (Exception e) {
						throw new ExceptionInInitializerError("Failed to load NOVA mod: " + c);
					}
				}
			));

		mods.putAll(scalaModsMap);

		/**
		 * Re-order mods based on dependencies
		 */
		orderedMods = new ArrayList<>();
		orderedMods.addAll(
			mods.entrySet()
				.stream()
				.sorted((o1, o2) -> {
					// Split string by @ and versions
					Map<String, String> loadAfter = Arrays.asList(o1.getKey().dependencies())
						.stream()
						.map(s -> s.split("@", 1))
						.collect(Collectors.toMap(s -> s[0], s -> s[1]));

					// TODO: Compare version requirements.
					return loadAfter.containsKey(o2.getKey().id()) ? 1 : 0;
				})
				.map(entry -> entry.getValue())
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

	public Map<NovaMod, MavenDependency[]> getNeededDeps() {
		return this.neededDeps;
	}

	/**
	 * Get the dependencies. Separated from preInit due to issues with ordering in case mods need to download mods before the preInit method is called.
	 * The wrapper just needs to call this method right before it downloads the dependencies.
	 */
	public void generateDependencies() {
		neededDeps = new HashMap<>(); // This should be cleaned every time this method is run.

		classesMap.keySet().stream()
			.forEach(this::generateAndAddDependencies);
	}


	private void generateAndAddDependencies(NovaMod mod) {
		if (mod.getClass().isAssignableFrom(DependencyProvider.class)) {
			// TODO: Fix this up. I mean, it *should* work atm, idk.
			try {
				neededDeps.put(mod, (MavenDependency[])mod.getClass().getMethod("getDependencies").getDefaultValue());
			} catch (NoSuchMethodException ex) {
				ex.printStackTrace();
			}
		}
	}

}
