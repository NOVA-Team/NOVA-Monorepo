package nova.core.depmodules;

import nova.core.fluid.FluidManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class FluidModule extends BinderModule {
	
	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(FluidManager.class).toConstructor();
	}
	
}
