package nova.internal.core.depmodules;

import nova.core.component.fluid.FluidManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class FluidModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(FluidManager.class).toConstructor();
	}

}
