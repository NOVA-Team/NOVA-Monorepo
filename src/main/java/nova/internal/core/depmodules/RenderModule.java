package nova.internal.core.depmodules;

import nova.core.render.RenderManager;
import se.jbee.inject.bind.BinderModule;

class RenderModule extends BinderModule {

	@Override
	protected void declare() {
		require(RenderManager.class);
	}

}
