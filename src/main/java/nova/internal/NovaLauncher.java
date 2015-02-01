package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import se.jbee.inject.Dependency;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher implements Loadable {

	private final Set<Class<?>> modClasses;
	private final DependencyInjectionEntryPoint diep;

	private Map<NovaMod, Loadable> mods;
	private ArrayList<Loadable> orderedMods;
	private Map<NovaMod, Class<? extends Loadable>> classesMap;
	// TODO: A lot of work and clean up has to be done to ensure this class is
	// not susceptible to
	// other classes touching it.

	/**
	 * Creates NovaLauncher.
	 * 
	 * @param modClasses mods to instantialize.
	 *            
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, Set<Class<?>> modClasses) {
		this.diep = diep;
		this.modClasses = modClasses;

		classesMap = modClasses.stream()
			.filter(Loadable.class::isAssignableFrom)
			.map(clazz -> (Class<? extends Loadable>) clazz.asSubclass(Loadable.class))
			.filter(clazz -> clazz.getAnnotation(NovaMod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(NovaMod.class), Function.identity()));

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

		/**
		 * Initialize all the NOVA mods.
		 */
		orderedMods.stream().forEachOrdered(Loadable::preInit);
		System.out.println("NOVA Mods Loaded: " + mods.size());
	}

	@Override
	public void init() {
		orderedMods.stream().forEach(Loadable::init);
	}

	@Override
	public void postInit() {
		orderedMods.stream().forEach(Loadable::postInit);
	}

	public Set<NovaMod> getLoadedMods() {
		return mods.keySet();
	}

}
