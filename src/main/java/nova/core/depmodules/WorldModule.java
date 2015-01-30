package nova.core.depmodules;

import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;
import nova.core.world.WorldManager;

class WorldModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(WorldManager.class).toConstructor();
	}

}

