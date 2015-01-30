package nova.core.depmodules;

import nova.core.entity.EntityManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class EntityModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(EntityManager.class).toConstructor();
	}

}
