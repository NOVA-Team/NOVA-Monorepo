package nova.internal;

import nova.bootstrap.DependencyInjectionEntryPoint;
import nova.core.game.Game;
import nova.core.loader.Loadable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The main class that launches NOVA mods.
 * @author Calclavia
 */
public class NovaLauncher implements Loadable {

	private final List<Class> modClasses;
	private List<Loadable> mods;

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
		 * TODO: Re-order mods based on dependencies
		 */

		/**
		 * Instantiate nova mods. 
		 */
		mods = modClasses
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

		/**
		 * Initialize all the NOVA mods.
		 */
		mods.stream().forEach(Loadable::preInit);
		
		System.out.println("NOVA Mods Loaded: " + mods.size());
	}

	@Override
	public void init() {
		mods.stream().forEach(Loadable::init);
	}

	@Override
	public void postInit() {

		mods.stream().forEach(Loadable::postInit);
	}
}
