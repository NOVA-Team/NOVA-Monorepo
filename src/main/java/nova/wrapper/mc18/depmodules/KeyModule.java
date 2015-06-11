package nova.wrapper.mc18.depmodules;

import nova.core.gui.InputManager;
import nova.wrapper.mc18.util.MCInputManager;
import se.jbee.inject.bind.BinderModule;

public class KeyModule extends BinderModule {

	@Override
	protected void declare() {
		bind(InputManager.class).to(MCInputManager.class);
	}

}
