package nova.core.wrapper.mc17.depmodules;

import nova.core.render.RenderManager;
import nova.core.wrapper.mc17.wrapper.render.BWClientRenderManager;
import se.jbee.inject.bind.BinderModule;

public class RenderModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RenderManager.class).to(BWClientRenderManager.class);
	}

}
