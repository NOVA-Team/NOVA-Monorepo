package nova.bootstrap;

import nova.core.depmodules.CoreBundle;
import nova.core.di.WrapperBundle;
import nova.core.game.Game;

import java.util.Optional;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.bootstrap.Bootstrap;


public class DependencyInjectionEntryPoint {

	private State state = State.ASLEEP;
	private Optional<Injector> injector = Optional.empty();
	
	/**
	 * @return current injector instance.
	 */
	public Optional<Injector> getInjector() {
		return injector;
	}

	/**
	 * First method of NOVA's launching chain. Initializes core modules.
	 */
	public void preInit() {
		if (state != State.ASLEEP) {
			throw new IllegalStateException("EntryPoint#preInit() has to be called first.");
		}
		injector = Optional.of(Bootstrap.injector(CoreBundle.class)); //Primary bindings.
		state = State.PREINIT;
	}

	/**
	 * Second stage of NOVA's launching chain.
	 * In this stage modules added to {@link WrapperBundle} are being installed in core injector.
	 */
	public void init() {
		if (state != State.PREINIT) {
			throw new IllegalStateException("EntryPoint#init() has to be called after preInit.");
		}
		injector = Optional.of(Bootstrap.injector(WrapperBundle.class)); //swap to secondary bindings
		state = State.INIT;
	}

	/**
	 * Third stage of NOVA's launching chain.
	 * In this stage modules added to {@link ModsBundle} are being installed in core injector.
	 * Alternating module composition in core injector after this stage is not possible.
	 * @return Game instance {@link Game}. Use it for future injections and general management.
	 */
	public Game postInit() {
		if (state != State.INIT) {
			throw new IllegalStateException("EntryPoint#postInit() has to be called after init.");
		}
		injector = Optional.of(Bootstrap.injector(WrapperBundle.class)); //swap to secondary bindings.
		state = State.POSTINIT;
		return injector.map(injector -> injector.resolve(Dependency.dependency(Game.class))).orElseThrow(IllegalStateException::new);
	}

	private enum State {
		ASLEEP, PREINIT, INIT, POSTINIT;
	}
}
