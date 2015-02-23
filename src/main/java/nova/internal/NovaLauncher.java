package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.deps.DependencyRepoProvider;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import se.jbee.inject.Dependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class that launches NOVA mods.
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher implements Loadable {

	private final Set<Class<?>> modClasses;
	private final DependencyInjectionEntryPoint diep;

	private Map<NovaMod, Loadable> mods;

	private Map<NovaMod, ArrayList<String[]>> dependencies;
	private Map<String, String> dependencyVersion;
	private Map<String, String[]> dependencyRepos;
	private Set<String> dependencyIds;

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
						.map(clazz -> (Object) diep.getInjector().get().resolve(Dependency.dependency(clazz)))
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
		orderedMods.stream().forEachOrdered(Loadable::preInit);
	}

	@Override
	public void init() {
		orderedMods.stream().forEachOrdered(Loadable::init);
	}

	@Override
	public void postInit() {
		orderedMods.stream().forEachOrdered(Loadable::postInit);
	}

	public Set<NovaMod> getLoadedMods() {
		return mods.keySet();
	}

	public Map<NovaMod, Loadable> getLoadedModMap() {
		return mods;
	}

	public Map<String, String> getDependencyVersions() {
		return this.dependencyVersion;
	}

	public Map<NovaMod, ArrayList<String[]>> getDependencyRepos() {
		return this.dependencies;
	}

	public String getDependencyVersion(String modid) {
		return this.dependencyVersion.keySet().contains(modid) ? this.dependencyVersion.get(modid) : null;
	}

	public String[] getDependencyRepo(String modid) {
		return this.dependencyRepos.keySet().contains(modid) ? this.dependencyRepos.get(modid) : null;
	}

	public Set<String> getDependencyIds() {
		return this.dependencyIds;
	}

	public Map<NovaMod, Class<? extends Loadable>> getModClasses() {
		return classesMap;
	}

	/**
	 * Get the dependencies. Separated from preInit due to issues with ordering in case mods need to download mods before the preInit method is called.
	 * The wrapper just needs to call this method right before it downloads the dependencies.
	 */
	public void generateDependencies() {

		if (dependencies == null) {
			dependencies = new HashMap<>();
			dependencyVersion = new HashMap<>();
			dependencyIds = new HashSet<>();
		}

		classesMap.keySet().stream()
			.forEach(this::generateAndAddDependencies);
	}

	private void generateAndAddDependencies(NovaMod mod) {

		if (mod.getClass().isAssignableFrom(DependencyRepoProvider.class)) {

			ArrayList<String[]> dependencyLocations = new ArrayList<>();
			Map<String, String[]> dependencyRepos = new HashMap<>();

			DependencyRepoProvider provider = (DependencyRepoProvider) mod;

			for (String modid : mod.dependencies()) {
				if (modid.contains("?")) {
					String modWORequired = modid.replace('?', ' ').replaceAll(" ", "");
					if (modid.contains("@")) {
						if (provider.getModRepo(modid.substring(0, modid.indexOf("@") - 1)) != null) {
							dependencyLocations.add(provider.getModRepo(modid.substring(0, modid.indexOf("@") - 1)));
							dependencyVersion.put(modWORequired.substring(0, modid.indexOf("@") - 1), modWORequired.substring(modWORequired.indexOf("@")));
							dependencyRepos.put(modWORequired.substring(0, modid.indexOf("@") - 1), provider.getModRepo(modid.substring(0, modid.indexOf("@") - 1)));
							dependencyIds.add(modWORequired.substring(0, modid.indexOf("@") - 1));
						}
					} else {
						if (provider.getModRepo(modWORequired) != null) {
							dependencyLocations.add(provider.getModRepo(modWORequired));
							dependencyRepos.put(modWORequired, provider.getModRepo(modWORequired));
							dependencyIds.add(modWORequired);
						}
					}
				}
			}

			dependencies.put(mod, dependencyLocations);
			this.dependencyRepos = dependencyRepos;

		}
	}

}
