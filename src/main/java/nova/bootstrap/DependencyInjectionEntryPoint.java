package nova.bootstrap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import nova.core.depmodules.All;
import nova.core.game.Game;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

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
	 * Has to be called before any 
	 */
	public void preInit(){
		if(state != State.ASLEEP){
			throw new IllegalStateException("EntryPoint#preInit() has to be called first.");
		}
		
		injector = Optional.of(Guice.createInjector(All.getAllCoreModules())); //Primary bindings.
		
		state = State.PREINIT;
	}
	
	/**
	 * Second stage of NOVA's launching chain.
	 * @param additionalModules modules provided by architecture wrappers that could not be detected automatically.
	 */
	public void init(Optional<Collection<Module>> additionalModules){
		if(state != State.PREINIT){
			throw new IllegalStateException("EntryPoint#init() has to be called after preInit.");
		}
		
		Set<Module> secondaryModules = new HashSet<>();	//TODO: Detect modules in wrappers.
		
		additionalModules.ifPresent(modules -> secondaryModules.addAll(modules));
		injector = injector.map(inje -> inje.createChildInjector(secondaryModules)); //swap to secondary bindings
		
		state = State.INIT;
	}
	
	/**
	 * Third stage of NOVA's launching chain.
	 * @param additionalModules modules provided by mods that could not be detected automatically.
	 * @return Game instance {@link Game}. Use it for future injections and general management. 
	 */
	public Game postInit(Optional<Collection<Module>> additionalModules){
		if(state != State.INIT){
			throw new IllegalStateException("EntryPoint#postInit() has to be called after init.");
		}
		
		Set<Module> tertiaryModules = new HashSet<>(); //TODO: Detect modules in mods.
		
		additionalModules.ifPresent(modules -> tertiaryModules.addAll(modules));
		injector = injector.map(inje -> inje.createChildInjector(tertiaryModules)); //swap to tertiary bindings
		
		state = State.POSTINIT;
		
		return injector.map(inje -> inje.getInstance(Game.class)).orElseThrow(IllegalStateException::new);
	}	
	
	private enum State {
		ASLEEP, PREINIT, INIT, POSTINIT;
	}
}
