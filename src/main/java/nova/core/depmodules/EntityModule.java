package nova.core.depmodules;

import se.jbee.inject.bind.BinderModule;


import se.jbee.inject.util.Scoped;
import nova.core.entity.EntityManager;

class EntityModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(EntityManager.class).toConstructor();
	}

}
