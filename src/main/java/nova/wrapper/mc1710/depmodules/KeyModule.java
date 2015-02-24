package nova.wrapper.mc1710.depmodules;

import nova.core.gui.KeyManager;
import nova.wrapper.mc1710.util.MCKeyManager;
import se.jbee.inject.bind.BinderModule;

public class KeyModule extends BinderModule {

	@Override
	protected void declare() {
		bind(KeyManager.class).to(MCKeyManager.class);
	}

}
