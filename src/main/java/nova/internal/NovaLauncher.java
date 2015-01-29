package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The main class that launches NOVA mods.
 * @author Calclavia
 */
public class NovaLauncher implements Loadable {

	private final List<Class> modClasses;
	private Map<NovaMod, Loadable> mods;
	private ArrayList<Loadable> orderedMods;

	//TODO: A lot of work and clean up has to be done to ensure this class is not susceptible to other classes touching it.

	public NovaLauncher(List<Class> modClasses) {
		this.modClasses = modClasses;
	}

	public void preInit() {

		/**
		 * Initiate Dependency Injection
		 */
		DependencyInjectionEntryPoint ep = new DependencyInjectionEntryPoint();

		ep.preInit();
		ep.init();
		Game.instance = Optional.of(ep.postInit());

		/**
		 * Instantiate nova mods.
		 */
		List<Loadable> modInstances = modClasses
			.stream()
			.map(c -> {
				try {
					return c.newInstance();
				} catch (Exception e) {
					throw new ExceptionInInitializerError(e);
				}
			})
			.filter(m -> m instanceof Loadable)
			.map(m -> (Loadable) m)
			.collect(Collectors.toList());


		mods = new HashMap<NovaMod, Loadable>();
		modInstances.stream().forEach(mod -> {
			NovaMod an = mod.getClass().getAnnotation(NovaMod.class);
			if (an != null) {
				mods.put(an, mod);
			}
		});

		/**
		 * TODO: Re-order mods based on dependencies
		 */

		orderedMods = new ArrayList<Loadable>();
		orderedMods.addAll(modInstances);

		/**
		 * Initialize all the NOVA mods.
		 */
		orderedMods.stream().forEach(Loadable::preInit);
		
		System.out.println("NOVA Mods Loaded: " + mods.size());
	}

	@Override
	public void init() { orderedMods.stream().forEach(Loadable::init);
	}

	@Override
	public void postInit() {
		orderedMods.stream().forEach(Loadable::postInit);
	}

	public Set<NovaMod> getLoadedMods() {
		return mods.keySet();
	}
}
