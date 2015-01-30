package nova.core.depmodules;

import nova.core.world.WorldManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class WorldModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(WorldManager.class).toConstructor();
	}

}

