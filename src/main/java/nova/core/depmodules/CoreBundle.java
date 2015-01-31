package nova.core.depmodules;

import com.google.common.collect.Sets;

import nova.core.di.DICoreModule;
import nova.core.di.OptionalModule;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Collections;
import java.util.Set;

public class CoreBundle extends BootstrapperBundle {
	private static Set<Class<? extends Bundle>> coreModules = Sets.newHashSet();

	public static Set<Class<? extends Bundle>> getAllCoreModules() {
		return Collections.unmodifiableSet(coreModules);
	}

	private static void add(Class<? extends Bundle> module) {
		coreModules.add(module.asSubclass(Bundle.class));
	}

	static {
		/** 
		 * Managers
		 */
		add(BlockModule.class);
		add(ItemModule.class);
		add(FluidModule.class);
		add(WorldModule.class);
		add(EntityModule.class);
		add(RenderModule.class);
		/**
		 * General 
		 */
		add(UtilModule.class);
		
		/**
		 * DI Internal
		 */
		add(OptionalModule.class);
		add(DICoreModule.class);
	}

	@Override
	protected void bootstrap() {
		coreModules.stream().forEach(this::install);
	}

}
