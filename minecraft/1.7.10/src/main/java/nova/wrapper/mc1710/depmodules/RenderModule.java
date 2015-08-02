package nova.wrapper.mc1710.depmodules;

import nova.core.render.RenderManager;
import nova.wrapper.mc1710.wrapper.render.BWClientRenderManager;
import nova.wrapper.mc1710.wrapper.render.BWRenderManager;
import se.jbee.inject.bind.BinderModule;

public class RenderModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RenderManager.class).to(BWClientRenderManager.class);
	}

}
