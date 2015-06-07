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
	private Optional<Injector> injector = Optional.empty();

	private Set<Class<? extends Bundle>> bundles = Sets.newHashSet();

	public DependencyInjectionEntryPoint() {

		install(CoreBundle.class);
	}

	/**
	 * @return current injector instance.
	 */
	public Optional<Injector> getInjector() {
		return injector;
	}

	/**
	 * @return current state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * Installs bundle in core Injector. Works until, finalization later throws
	 * {@link IllegalStateException}.
	 *
	 * @param bundle Bundle
	 */
	public void install(Class<? extends Bundle> bundle) {
		if (state != State.PREINIT) {
			throw new IllegalStateException("This function may only be used before DependencyInjectionEntryPoint initialization.");
		}
		bundles.add(bundle);
	}

	/**
	 * Removes bundle from core Injector. Works until finalization, later throws
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
	 *
	 * @return Game instance {@link Game}. Use it for future injections and
	 *         general management.
	 */
	public Game init() {
		if (state != State.PREINIT) {
			throw new IllegalStateException("EntryPoint#postInit() has to be only once.");
		}

		DIEPBundle.bundles = bundles;

		injector = Optional.of(Bootstrap.injector(DIEPBundle.class));
		state = State.POSTINIT;
		return injector.map(injector -> injector.resolve(Dependency.dependency(Game.class))).orElseThrow(IllegalStateException::new);
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
