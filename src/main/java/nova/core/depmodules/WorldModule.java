package nova.core.depmodules;

import nova.core.world.WorldManager;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class WorldModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WorldManager.class).in(Singleton.class);
	}

}
