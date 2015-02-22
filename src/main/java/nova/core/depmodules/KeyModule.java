package nova.core.depmodules;

import nova.core.gui.KeyManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class KeyModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(KeyManager.class).toConstructor();
	}

}
