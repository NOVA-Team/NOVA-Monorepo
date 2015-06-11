package nova.wrapper.mc18.depmodules;

import nova.wrapper.mc18.util.WrapperEventManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class WrapperEventModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(WrapperEventManager.class).toConstructor();
	}

}
