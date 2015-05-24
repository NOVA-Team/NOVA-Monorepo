package nova.core.depmodules;

import com.google.common.collect.Sets;
import nova.core.di.DICoreModule;
import nova.core.di.LoggerModule;
import nova.core.di.OptionalModule;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Bundle;

import java.util.Collections;
import java.util.Set;

public class CoreBundle extends BootstrapperBundle {
	private static Set<Class<? extends Bundle>> coreModules = Sets.newHashSet();

	static {
		/**
		 * Managers
		 */
		add(BlockModule.class);
		add(ItemModule.class);
		add(FluidModule.class);
		add(EventModule.class);
		add(WorldModule.class);
		add(EntityModule.class);
		add(RenderModule.class);
		add(DictionaryModule.class);

		add(RecipesModule.class);
		add(CraftingModule.class);
		add(GuiModule.class);
		add(NativeModule.class);
		add(ComponentModule.class);

		/**
		 * General
		 */
		add(UtilModule.class);

		/**
		 * DI Internal
		 */
		add(OptionalModule.class);
		add(DICoreModule.class);
		add(LoggerModule.class);
	}

	public static Set<Class<? extends Bundle>> getAllCoreModules() {
		return Collections.unmodifiableSet(coreModules);
	}

	private static void add(Class<? extends Bundle> module) {
		coreModules.add(module.asSubclass(Bundle.class));
	}

	@Override
	protected void bootstrap() {
		coreModules.stream().forEach(this::install);
	}

}
