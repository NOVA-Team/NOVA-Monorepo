package nova.core.depmodules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import nova.core.world.WorldManager;

class WorldModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WorldManager.class).in(Singleton.class);
	}

}
