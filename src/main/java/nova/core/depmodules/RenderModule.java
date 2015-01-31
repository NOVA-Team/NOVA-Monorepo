package nova.core.depmodules;

import nova.core.render.RenderManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class RenderModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(RenderManager.class).toConstructor();
	}

}

