package nova.wrapper.mc18.depmodules;

import nova.core.render.RenderManager;
import nova.wrapper.mc18.wrapper.render.BWRenderManager;
import se.jbee.inject.bind.BinderModule;

public class RenderModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RenderManager.class).to(BWRenderManager.class);
	}

}
