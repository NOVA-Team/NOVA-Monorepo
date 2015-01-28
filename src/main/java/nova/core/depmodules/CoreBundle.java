package nova.core.depmodules;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nova.core.di.OptionalModule;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

public class CoreBundle extends BootstrapperBundle{
	private static Set<Class<? extends Bundle>> coreModules = new HashSet<>();

	public static Set<Class<? extends Bundle>> getAllCoreModules() {
		return Collections.unmodifiableSet(coreModules);
	}

	private static void add(Class<? extends Bundle> module) {
		coreModules.add(module.asSubclass(Bundle.class));
	}
	
	static {
		add(BlockModule.class);
		add(ItemModule.class);
		add(WorldModule.class);
		add(EntityModule.class);
		
		add(UtilModule.class);
		add(OptionalModule.class);
	}

	@Override
	protected void bootstrap() {
		coreModules.stream().forEach(module -> this.install(module));
	}
	
	

}
