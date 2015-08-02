package nova.wrapper.mc1710.depmodules;

import nova.core.game.InputManager;
import nova.wrapper.mc1710.util.MCInputManager;
import se.jbee.inject.bind.BinderModule;

public class KeyModule extends BinderModule {

	@Override
	protected void declare() {
		bind(InputManager.class).to(MCInputManager.class);
	}

}
