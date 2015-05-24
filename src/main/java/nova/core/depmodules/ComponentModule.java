package nova.core.depmodules;

import nova.core.component.ComponentManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class ComponentModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(ComponentManager.class).toConstructor();
	}

}
