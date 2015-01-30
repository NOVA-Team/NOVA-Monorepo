package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The main class that launches NOVA mods.
 *
 * @author Calclavia, Kubuxu
 */
public class NovaLauncher implements Loadable {

	private final List<Class<?>> modClasses;
	private final DependencyInjectionEntryPoint diep;

	private Map<NovaMod, Loadable> mods;
	private ArrayList<Loadable> orderedMods;

	// TODO: A lot of work and clean up has to be done to ensure this class is not susceptible to
	// other classes touching it.

	/**
	 * Creates NovaLauncher.
	 *
	 * @param modClasses mods to instantialize.
	 * @param diep is required as we are installing additional modules to it.
	 */
	public NovaLauncher(DependencyInjectionEntryPoint diep, List<Class<?>> modClasses) {
		this.diep = diep;
		this.modClasses = modClasses;

		Map<NovaMod, Class<? extends Loadable>> classesMap = modClasses.stream()
			.filter(clazz -> Loadable.class.isAssignableFrom(clazz))
			.map(clazz -> (Class<? extends Loadable>) clazz.asSubclass(Loadable.class))
			.filter(clazz -> clazz.getAnnotation(NovaMod.class) != null)
			.collect(Collectors.toMap((clazz) -> clazz.getAnnotation(NovaMod.class), Function.identity()));

		classesMap.keySet().stream()
			.flatMap(mod -> Arrays.stream(mod.modules()))
			.forEach(bundle -> diep.install(bundle));

		List<Loadable> modInstances = classesMap.values().stream()
			.map(calzz -> {
				try {
					return (Loadable) calzz.newInstance();
				} catch (Exception e) {
					throw new ExceptionInInitializerError(e);
				}
			})
			.collect(Collectors.toList());

		/**
		 * TODO: Re-order mods based on dependencies
		 */

		orderedMods = new ArrayList<Loadable>();
		orderedMods.addAll(modInstances);

	}

	public void preInit() {

		/**
		 * Initialize all the NOVA mods.
		 */
		orderedMods.stream().forEach(Loadable::preInit);

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
