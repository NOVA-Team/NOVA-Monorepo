package nova.core.depmodules;

import nova.core.nativewrapper.NativeManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

public class NativeModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(NativeManager.class).toConstructor();
	}
}
