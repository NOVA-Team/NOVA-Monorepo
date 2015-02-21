package nova.wrapper.mc1710.depmodules;

import nova.core.util.SaveManager;
import nova.wrapper.mc1710.manager.MCSaveManager;
import se.jbee.inject.bind.BinderModule;

public class SaveModule extends BinderModule {

	@Override
	protected void declare() {
		bind(SaveManager.class).to(MCSaveManager.class);
	}

}
