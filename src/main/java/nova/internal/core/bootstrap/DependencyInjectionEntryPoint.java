package nova.internal.core.bootstrap;

import com.google.common.collect.Sets;
import nova.internal.core.depmodules.CoreBundle;
import nova.internal.core.Game;
import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Optional;
import java.util.Set;

public class DependencyInjectionEntryPoint {

	private State state = State.PREINIT;

	// When this is null no mods are loaded.
	private Injector injector = null;

	private Set<Class<? extends Bundle>> bundles = Sets.newHashSet();

	// Game bound to this DIEP.
	private Game game = null;

	public DependencyInjectionEntryPoint() {
		install(CoreBundle.class);
	}

	/**
	 * @return current injector instance.
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * @return current state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Installs bundle in core Injector. Can be used after initialization.
	 *
	 * @param bundle Bundle
	 */
	public void install(Class<? extends Bundle> bundle) {
		if (state != State.PREINIT) {
			bundles.add(bundle);
			DIEPBundle.bundles = bundles;
			game.changeInjector(Bootstrap.injector(DIEPBundle.class));
		}
		bundles.add(bundle);
	}

	/**
	 * Removes bundle from core Injector. Works until initialization, later throws
	 * {@link IllegalStateException}.
	 *
	 * @param bundle Bundle
	 * @return whether module being uninstalled was installed.
	 */
	public boolean uninstall(Class<? extends Bundle> bundle) {
		if (state != State.PREINIT) {
			throw new IllegalStateException("This function may only be used before DependencyInjectionEntryPoint initialization.");
		}
		return bundles.remove(bundle);
	}

	/**
	 * In this method modules added to DependencyInjectionEntryPoint are being
	 * installed in core injector. Alternating module composition in core
	 * injector after initialization is not possible.
	 * Sets Game.instance
	 */
	public Game init() {
		if (state != State.PREINIT) {
			throw new IllegalStateException("EntryPoint#postInit() has to be only once.");
		}

		DIEPBundle.bundles = bundles;

		injector = Bootstrap.injector(DIEPBundle.class);
		state = State.POSTINIT;
		game = injector.resolve(Dependency.dependency(Game.class));
		game.changeInjector(injector);
		Game.setGame(game);
		return game;
	}

	private enum State {
		PREINIT, POSTINIT
	}

	public static final class DIEPBundle extends BootstrapperBundle {

		private static Set<Class<? extends Bundle>> bundles;

		@Override
		protected void bootstrap() {
			bundles.stream().forEach(this::install);
		}

	}

}
