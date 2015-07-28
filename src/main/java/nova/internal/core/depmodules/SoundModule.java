package nova.internal.core.depmodules;

import nova.core.sound.SoundManager;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.util.Scoped;

class SoundModule extends BinderModule {

	@Override
	protected void declare() {
		per(Scoped.APPLICATION).bind(SoundManager.class).toConstructor();
	}

}
